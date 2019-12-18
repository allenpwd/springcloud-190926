package pwd.allen.service;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import pwd.allen.entity.User;

import java.util.Map;

/**
 * 为整个服务接口指定服务降级的调用类，服务降级时在客户端使用的，即服务调用方
 * 指定方式：@FeignClient(value = "应用名", fallbackFactory = HelloServiceFallbackFactory.class)
 * 此外还需要在feign负载均衡调用方 配置：feign.hystrix.enabled: true
 *
 * @author 门那粒沙
 * @create 2019-10-01 17:36
 **/
@Component
public class HelloServiceFallbackFactory implements FallbackFactory<HelloService> {

    @Override
    public HelloService create(Throwable throwable) {
        return new HelloService() {
            @Override
            public String sayHello(String name) {
                return "fallback from " + this.getClass().getName();
            }

            @Override
            public User getUser(Map map_param) {
                User user = new User();
                user.setName("fallback：缺省");
                return user;
            }
        };
    }
}
