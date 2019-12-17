package pwd.allen.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pwd.allen.service.HelloService;

/**
 * 演示 三种调用服务的方式
 *
 * 从注册中心获取了所有服务清单，所以客户端可以根据自己的需要决定具体调用哪个实例
 *
 * @author 门那粒沙
 * @create 2019-09-26 22:46
 **/
@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 使用feign 负载均衡
     *
     * Feign 是自带熔断器的，但默认是关闭的，打开方式：feign.hystrix.enabled=true
     *
     * @param name
     * @return
     */
    @GetMapping("/helloFeign/{name}")
    public String helloFeign(@PathVariable("name") String name) {
        return helloService.sayHello(name);
    }

    /**
     * ribbon
     * 使用 LoadBalancerClient（RibbonAutoConfiguration自动配置） 负载均衡
     * 通过应用名（不区分大小写）获取host和port
     * @param name
     * @return
     */
    @HystrixCommand(fallbackMethod = "helloFallback")
    @GetMapping("/helloLBC/{name}")
    public String helloLBC(@PathVariable("name") String name) {
        RestTemplate restTemplate = new RestTemplate();
        ServiceInstance helloservice = loadBalancerClient.choose("helloservice");
        String url = String.format("http://%s:%s/hello/%s", helloservice.getHost(), helloservice.getPort(), name);
        return restTemplate.getForObject(url, String.class);
    }

    /**
     * ribbon
     * 使用 标注了@LoadBalanced注解的 RestTemplate 负载均衡
     * @param name
     * @return
     */
    @HystrixCommand(fallbackMethod = "helloFallback")
    @GetMapping("/helloLB/{name}")
    public String helloLB(@PathVariable("name") String name) {
        String url = String.format("http://HELLOSERVICE/hello/%s", name);
        return restTemplate.getForObject(url, String.class);
    }

    /**
     * fallback方法要和被降级的方法有相同参数，否则：fallback method wasn't found
     * @param name
     * @return
     */
    public String helloFallback(String name) {
        return "fallback for hello, name=" + name;
    }

}
