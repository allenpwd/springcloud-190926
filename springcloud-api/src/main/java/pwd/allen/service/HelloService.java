package pwd.allen.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pwd.allen.config.FeignBuilderConfig;
import pwd.allen.entity.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 把微服务公共部分抽出来作为接口
 * 不推荐这样做，因为这样使得微服务提供方和消费方相互关联，不解耦
 *
 * 可以使用Feign自带的注解或者JAX-RS注解，Feign还支持Spring MVC注解（@RequestParam @PathVariable @RequestHeader的value不可省略，因为Feign不会像Spring MVC那样以参数名作为默认值）
 *
 * @FeignClient：绑定接口与服务
 *  value/name：服务名，不区分大写,用于服务发现，当有url时这个属性只是一个名字
 *  url：url一般用于调试，可以手动指定@FeignClient调用的地址
 *  fallbackFactory：指定服务降级时调用的对应类的工厂类
 *  fallback：指定服务降级时调用的对应类，指定的类必须实现@FeignClient标记的接口
 *  configuration: Feign配置类，可以自定义Feign的Encoder、Decoder、LogLevel、Contract
 *  decode404：当发生http 404错误时，如果该字段位true，会调用decoder进行解码，否则抛出FeignException
 *
 * 绑定Feign的接口中，@RequestParam、@RequestHeader这些可以指定参数名的注解的value不能省略
 *
 *
 * @author 门那粒沙
 * @create 2019-09-26 21:29
 **/
@FeignClient(value = "helloservice", fallbackFactory = HelloServiceFallbackFactory.class, configuration = FeignBuilderConfig.class)
public interface HelloService {

    @GetMapping("/hello/{name}")
    public String sayHello(@PathVariable("name") String name);

    @PostMapping("/hello/getUser")
    public User getUser(@RequestBody Map map_param);

    /**
     * 可以用自定义的实体类做为接收参数，但是必须有无参构造函数，否则feign根据JSON转实体类会报错
     * @param names
     * @return
     */
    @PostMapping("/hellos")
    public List<String> sayHellos(@RequestBody List<String> names);
}
