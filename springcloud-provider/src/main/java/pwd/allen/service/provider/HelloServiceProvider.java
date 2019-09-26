package pwd.allen.service.provider;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pwd.allen.service.HelloService;

/**
 * @author 门那粒沙
 * @create 2019-09-26 21:34
 **/
@RestController
public class HelloServiceProvider implements HelloService {

    @Override
    public String sayHello(@PathVariable String name) {
        return "hello!" + name;
    }
}
