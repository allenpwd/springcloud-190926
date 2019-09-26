package pwd.allen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pwd.allen.service.HelloService;

/**
 * @author 门那粒沙
 * @create 2019-09-26 22:46
 **/
@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping("/hello/{name}")
    public String sayHello(@PathVariable("name") String name) {
        return "i am super man! " + name;
    }

}
