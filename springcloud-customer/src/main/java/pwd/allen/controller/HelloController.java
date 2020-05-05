package pwd.allen.controller;

import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.google.common.collect.Lists;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import pwd.allen.command.MyHystrixCommand;
import pwd.allen.command.MyHystrixObservableCommand;
import pwd.allen.entity.User;
import pwd.allen.service.HelloService;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 演示 三种调用服务的方式
 *
 * 从注册中心获取了所有服务清单，所以客户端可以根据自己的需要决定具体调用哪个实例，实现客户端负载均衡
 *
 * @author 门那粒沙
 * @create 2019-09-26 22:46
 **/
@Slf4j
@RestController
public class HelloController {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private HelloService helloService;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 使用feign 负载均衡
     *
     * Feign 是自带熔断器的，但默认是关闭的，打开方式：feign.hystrix.enabled=true
     *
     * @param name
     * @return
     */
    @GetMapping("/helloFeign/{name}")
    public Map helloFeign(@PathVariable("name") String name, @RequestHeader("User-Agent") String userAgent) {

        //RequestContextHolder里面有两个ThreadLocal保存当前线程下的request
        //代码：org.springframework.web.servlet.FrameworkServlet.initContextHolders
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        log.info("User-Agent:{}", userAgent);

        HashMap<String, Object> map_rel = new HashMap<>();
        map_rel.put("hello", helloService.sayHello(name));

        Map<String, Object> map_param = new HashMap<>();
        map_param.put("name", name);
        map_rel.put("hello/getUser", helloService.getUser(map_param));

        map_rel.put("hellos", helloService.sayHellos(Lists.newArrayList(name, name + "2")));

        return map_rel;
    }

    /**
     * ribbon
     * 使用 LoadBalancerClient（RibbonAutoConfiguration自动配置） 负载均衡
     * 特点：通过应用名（不区分大小写）获取host和port
     *
     * @HystrixCommand指定熔断器配置
     *  fallbackMethod：服务降级时回调的方法，需要参数（可以加个Throwable参数接受错误信息）、返回值一致
     *  commandKey：命令名，好像默认是方法名
     *  groupKey：命令组名，用于组织统计命令的告警、仪表盘等信息，命令线程的划分默认也是根据组
     *  threadPoolKey：线程池划分名，默认命令组名
     *
     *
     * @CacheResult：设置请求缓存，默认缓存key为所有参数即name
     *      指定缓存key：自身的属性cacheKeyMethod，或者@CacheKey，前者优先；若没指定则使用所有参数
     *
     * @CacheRemove：清除请求命令的缓存
     *
     * @param name
     * @return
     */
//    @CacheResult//妈个鸡，报错：Request caching is not available. Maybe you need to initialize the HystrixRequestContext，需要先初始化请求上下文，先不用了
    @HystrixCommand(fallbackMethod = "helloFallback", commandKey = "helloLBC", groupKey = "helloGroup", threadPoolKey = "helloLBC")
    @GetMapping("/helloLBC/{name}")
    public Map helloLBC(@PathVariable("name") String name) {

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
     *  ignoreExceptions：指定要忽略的异常类型；
     *      除了HystrixBadRequestException之外其他异常均会被Hystrix认为命令执行失败并触发服务降级
     *      ignoreExceptions指定的异常会被封装成HystrixBadRequestException抛出，所以能避免触发服务降级
     *  observableExecutionMode：指定执行方式；EAGER使用observe()，默认这种；LAZY使用toObservable()
     *
     * @param name
     * @return
     */
    @HystrixCommand(fallbackMethod = "helloFallback", ignoreExceptions = {CloneNotSupportedException.class}, observableExecutionMode = ObservableExecutionMode.EAGER)
    @GetMapping("/helloLB/{name}")
    public Map helloLB(@PathVariable("name") String name) {

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

        return map_rel;
    }


    /**
     * 注解方式使用HystrixObservableCommand实现命令封装
     * 支持请求多次，返回的是结果集合
     * 其中一个请求出错只会影响请求本身的返回结果和后面的请求，前面已请求成功的结果不会受到影响
     *
     * 这个方式已被标记为废弃，所以不推荐
     *
     * @param name
     * @return
     */
    @HystrixCommand(fallbackMethod = "helloFallback")
    @GetMapping("/helloLB2/{name}")
    public Observable<Map> helloLB2(@PathVariable("name") String name) {

        return Observable.create(new Observable.OnSubscribe<Map>() {
            @Override
            public void call(Subscriber<? super Map> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()) {
                        HashMap<String, Object> map_rel = new HashMap<>();

                        String url = "http://HELLOSERVICE/hello/{1}";
                        User user_param = new User();
                        user_param.setName(name);
                        User user_rel = restTemplate.postForObject("http://HELLOSERVICE/hello/getUser", user_param, User.class);
                        map_rel.put("user", user_rel);
                        subscriber.onNext(map_rel);

                        //测试下第二个调用出错的情况，结果：第一个结果照常返回，第二个返回服务降级的结果
                        user_param.setName("error");
                        user_rel = restTemplate.postForObject("http://HELLOSERVICE/hello/getUser", user_param, User.class);
                        map_rel = new HashMap<>();
                        map_rel.put("user", user_rel);
                        subscriber.onNext(map_rel);

                        //因为前面报错，所以这个不会执行和返回
                        user_param.setName(name + "2");
                        user_rel = restTemplate.postForObject("http://HELLOSERVICE/hello/getUser", user_param, User.class);
                        map_rel = new HashMap<>();
                        map_rel.put("user", user_rel);
                        subscriber.onNext(map_rel);

                        subscriber.onCompleted();
                    }
                } catch (RestClientException e) {
                    subscriber.onError(e);
                }
            }
        });
    }



    /**
     * fallback方法要和被降级的方法在同一个类中；fallback方法的修饰符没有特别要求，public private protected都可
     * fallback方法要和被降级的方法有相同参数，否则报错：fallback method wasn't found
     *
     * @param name
     * @e 获取触发服务降级的具体异常内容
     * @return
     */
    public Map helloFallback(String name, Throwable e) {
        e.printStackTrace();
        Map<String, Object> map_rel = new HashMap<>();
        map_rel.put("fallback", "name=" + name);
        map_rel.put("error", e.toString());
        return map_rel;
    }


    /**
     * 断路器的另一种方式：实现HystrixCommand接口
     *
     * 需要把逻辑封装到命令中，没有注解方式简洁，不推荐
     * @param name
     * @return
     */
    @GetMapping("/helloHC/{name}")
    public String helloHC(@PathVariable String name) {
        String rel = null;
        MyHystrixCommand command = new MyHystrixCommand(restTemplate, name);

        //方式一：同步执行
        rel = command.execute();

        //方式二：异步执行
//        Future<String> queue = command.queue();
//        try {
//            rel = queue.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        //返回Hot observable，命令会立刻执行，observable每次被订阅时会重放它的行为
//        Observable<String> observable_hot = command.observe();

        //返回Cold observable，命令不会立刻执行，只有所有订阅者都订阅它后才执行
//        Observable<String> observable_cold = command.toObservable();

        return rel;
    }

    /**
     * 断路器的另一种方式：实现HystrixObservableCommand接口
     *
     * 没有注解方式简洁，不推荐
     * @param name
     * @return
     */
    @GetMapping(value = "/helloHOC/{name}")
    public List<String> helloHOC(@PathVariable String name) {
        MyHystrixObservableCommand command = new MyHystrixObservableCommand(restTemplate, name);

        //返回Hot observable，命令会立刻执行，observable每次被订阅时会重放它的行为，这种有时候会漏结果
//        Observable<String> observable = command.observe();

        //返回Cold observable，命令不会立刻执行，只有所有订阅者都订阅它后才执行
        Observable<String> observable = command.toObservable();

        List<String> list_rel = new ArrayList<>();

        //TODO 你妈了个逼的 这里有问题，还没给list_rel输入结果 list_rel就被返回了，导致页面看到的是空数组，后来重启服务又好了，感觉和jrebel热部署有关

        observable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("聚合完了所有的查询请求!");
                System.out.println(list_rel);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("出错了：" + t.toString());
                t.printStackTrace();
            }

            @Override
            public void onNext(String str) {
                list_rel.add(str);
            }
        });

        return list_rel;
    }


    @GetMapping("/bean/{beanName}")
    public Object getBean(@PathVariable String beanName) {
        log.info(beanName);
        log.debug(beanName);
        log.warn(beanName);
        List<Object> list = new ArrayList<>();
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            if (beanDefinitionName.toLowerCase().contains(beanName.toLowerCase())) {
                list.add(applicationContext.getBean(beanDefinitionName));
            }
        }
        return ReflectionToStringBuilder.toString(list);
    }

}
