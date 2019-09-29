package pwd.allen.controller;

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
     * @param name
     * @return
     */
    @GetMapping("/helloFeign/{name}")
    public String helloFeign(@PathVariable("name") String name) {
        return helloService.sayHello(name);
    }

    /**
     * 使用 LoadBalancerClient 负载均衡
     * 通过应用名（不区分大小写）获取host和port
     * @param name
     * @return
     */
    @GetMapping("/helloLBC/{name}")
    public String helloLBC(@PathVariable("name") String name) {
        RestTemplate restTemplate = new RestTemplate();
        ServiceInstance helloservice = loadBalancerClient.choose("helloservice");
        String url = String.format("http://%s:%s/hello/%s", helloservice.getHost(), helloservice.getPort(), name);
        return restTemplate.getForObject(url, String.class);
    }

    /**
     * 使用 标注了@LoadBalanced注解的 RestTemplate 负载均衡
     * @param name
     * @return
     */
    @GetMapping("/helloLB/{name}")
    public String helloLB(@PathVariable("name") String name) {
        String url = String.format("http://HELLOSERVICE/hello/%s", name);
        return restTemplate.getForObject(url, String.class);
    }

}