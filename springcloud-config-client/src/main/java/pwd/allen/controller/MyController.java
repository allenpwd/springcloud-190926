package pwd.allen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 门那粒沙
 * @create 2019-10-02 16:50
 **/
@RestController
public class MyController {

    @Value("${info.pwd.test}")
    private String msg;

    @Value("${server.port}")
    private Integer port;

    @Autowired
    private Environment environment;

    @GetMapping("/config")
    public Object config() {
        return "msg:" + msg + "--port:" + port;
    }
}
