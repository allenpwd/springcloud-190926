package pwd.allen.service.provider;

import org.springframework.beans.factory.annotation.Value;
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
     * @param name
     * @return
     */
    @Override
    public String sayHello(@PathVariable String name) {
        return "hello!" + name + ", this is " + this.getClass().getName() + "-" + message;
    }
}
