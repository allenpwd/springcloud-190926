package pwd.allen.service.provider;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pwd.allen.entity.User;
import pwd.allen.service.HelloService;

import java.util.Date;
import java.util.Map;

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
     * @param name
     * @return
     */
    @GetMapping("/other/{name}")
    public String other(@PathVariable String name) {
        return "this is another interface not from HelloService! your name is :" + name;
    }

    @Override
    public User getUser(@RequestBody Map map_param) {
        User user = new User();
        user.setAge(18);
        if (map_param.containsKey("name")) {
            String name = (String) map_param.get("name");
            if ("error".equals(name)) throw new RuntimeException("测试下断路器");
            user.setName(name);
        } else {
            user.setName("奥利给");
        }
        user.setCreateDate(new Date());
        return user;
    }

    /**
     * fallback方法要和被降级的方法有相同参数，否则：fallback method wasn't found
     * @param name
     * @return
     */
    public String sayHelloFallback(String name, Throwable e) {
        return String.format("this is callback for sayHello,the error is %s", e.toString());
    }
}
