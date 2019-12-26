package pwd.allen.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import pwd.allen.entity.User;
import pwd.allen.service.HelloService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * 演示 三种调用服务的方式
 *
 * 从注册中心获取了所有服务清单，所以客户端可以根据自己的需要决定具体调用哪个实例，实现客户端负载均衡
 *
 * @author 门那粒沙
 * @create 2019-09-26 22:46
 **/
@RestController
public class HelloController {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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
    public Map<String, Object> helloFeign(@PathVariable("name") String name, @RequestHeader("User-Agent") String userAgent) {

        System.out.println("User-Agent:" + userAgent);

        HashMap<String, Object> map_rel = new HashMap<>();
        map_rel.put("hello", helloService.sayHello(name));
        Map<String, Object> map_param = new HashMap<>();
        map_param.put("name", name);
        map_rel.put("hello/getUser", helloService.getUser(map_param));

        return map_rel;
    }

    /**
     * ribbon
     * 使用 LoadBalancerClient（RibbonAutoConfiguration自动配置） 负载均衡
     * 特点：通过应用名（不区分大小写）获取host和port
     *
     * @HystrixCommand指定熔断器配置
     *  fallbackMethod：服务降级时回调的方法，需要参数（可以加多个Throwable参数接受错误信息）、返回值一致
     *  commandKey：命令分组
     *  groupKey：命令组名
     *  threadPoolKey：线程池划分名
     *
     * @param name
     * @return
     */
    @CacheResult//设置请求缓存，默认缓存key为所有参数即name
    @HystrixCommand(fallbackMethod = "helloFallback", commandKey = "helloLBC", groupKey = "helloGroup", threadPoolKey = "helloLBC")
    @GetMapping("/helloLBC/{name}")
    public Map<String, Object> helloLBC(@PathVariable("name") String name) {

        HashMap<String, Object> map_rel = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        ServiceInstance helloservice = loadBalancerClient.choose("helloservice");

        //根据服务实例处理URI
//        try {
//            URI uri = loadBalancerClient.reconstructURI(helloservice, new URI("http://HELLOSERVICE/hello/"));
//            System.out.println(uri);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }

        //region 调用get请求
        String url = String.format("http://%s:%s/hello/%s", helloservice.getHost(), helloservice.getPort(), name);
        String rel_get = restTemplate.getForObject(url, String.class);
        map_rel.put("hello", rel_get);
        //endregion

        //region 调用post请求
        url = String.format("http://%s:%s/hello/getUser", helloservice.getHost(), helloservice.getPort());
        User user_param = new User();
        user_param.setName(name);
        User user_rel = restTemplate.postForObject(url, user_param, User.class);
        map_rel.put("hello/getUser", user_rel);
        //endregion

        return map_rel;
    }

    /**
     * ribbon
     * 使用 标注了@LoadBalanced注解的 RestTemplate 负载均衡
     *
     * @HystrixCommand熔断器配置
     *  ignoreExceptions：指定要忽略的异常类型，即抛出这些错不触发服务降级
     *
     * @param name
     * @return
     */
    @HystrixCommand(fallbackMethod = "helloFallback", ignoreExceptions = {HystrixBadRequestException.class})
    @GetMapping("/helloLB/{name}")
    public Map<String, Object> helloLB(@PathVariable("name") String name) {

        HashMap<String, Object> map_rel = new HashMap<>();

        //region 调用get请求
        //{1}占位符会被替换成第一个参数
        String url = "http://HELLOSERVICE/hello/{1}";
        String rel = null;
        //方法一
//        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class, name);
//        System.out.println(String.format("statusCode=%s", entity.getStatusCode()));
//        rel = entity.getBody();
        //方法二
//        rel = restTemplate.getForObject(url, String.class, name);
        //方法三
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url).buildAndExpand(name).encode();
        rel = restTemplate.getForObject(uriComponents.toUri(), String.class);
        map_rel.put("hello", rel);
        //endregion

        //region 调用post请求
        User user_param = new User();
        user_param.setName(name);
        User user_rel = restTemplate.postForObject("http://HELLOSERVICE/hello/getUser", user_param, User.class);
        map_rel.put("hello/getUser", user_rel);
        //endregion

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return map_rel;
    }

    /**
     * fallback方法要和被降级的方法有相同参数，否则报错：fallback method wasn't found
     * @param name
     * @e 获取触发服务降级的具体异常内容
     * @return
     */
    public Map<String, Object> helloFallback(String name, Throwable e) {
        e.printStackTrace();
        Map<String, Object> map_rel = new HashMap<>();
        map_rel.put("fallback", "name=" + name);
        map_rel.put("error", e.toString());
        return map_rel;
    }

}
