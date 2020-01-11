package pwd.allen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lenovo
 * @create 2020-01-08 15:52
 **/
@RestController
public class TestController {

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("test")
    public Object test() {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        System.out.println("看看容器内容");
        return beanDefinitionNames;
    }
}
