package pwd.allen.service.provider;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pwd.allen.service.HelloService;

/**
 * @author 门那粒沙
 * @create 2019-09-26 21:34
 **/
@RestController
public class HelloServiceProvider implements HelloService {

    @Value("port=${server.port}")
    private String message;


    /**
     * 可以继承HelloService接口的GetMapping注解，但是参数上的注解需要重新定义
     *
     * HystrixCommand fallbackMethod属性指定服务降级时调用的方法，缺点：只能指定一个方法
     *
     * @param name
     * @return
     */
    @HystrixCommand(fallbackMethod = "sayHelloFallback")
    @Override
    public String sayHello(@PathVariable String name) {
        if ("error".equals(name)) throw new RuntimeException("测试下断路器");
        return "hello!" + name + ", this is " + this.getClass().getName() + "-" + message;
    }

    /**
     * fallback方法要和被降级的方法有相同参数，否则：fallback method wasn't found
     * @param name
     * @return
     */
    public String sayHelloFallback(String name) {
        return "this is callback for sayHello";
    }

    @GetMapping("/other/{name}")
    public String other(@PathVariable String name) {
        return "this is another interface not from HelloService! your name is :" + name;
    }
}
