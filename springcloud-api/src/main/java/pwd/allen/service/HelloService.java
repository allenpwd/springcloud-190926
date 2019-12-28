package pwd.allen.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
 * fallbackFactory指定服务降级时调用的对应类的工厂类
 * fallback指定服务降级时调用的对应类
 *
 * @author 门那粒沙
 * @create 2019-09-26 21:29
 **/
@FeignClient(value = "helloservice", fallbackFactory = HelloServiceFallbackFactory.class)
public interface HelloService {

    @GetMapping("/hello/{name}")
    public String sayHello(@PathVariable("name") String name);

    @PostMapping("/hello/getUser")
    public User getUser(@RequestBody Map map_param);

    @PostMapping("/hellos")
    public List<String> sayHellos(@RequestBody List<String> names);
}
