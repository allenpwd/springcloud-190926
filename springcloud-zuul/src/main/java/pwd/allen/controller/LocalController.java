package pwd.allen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lenovo
 * @create 2020-01-02 17:02
 **/
@RestController()
public class LocalController {

    @GetMapping("localForward/**")
    public Object localForward(HttpServletRequest request) {
        String rel = String.format("本地转发，requestURI=%s", request.getRequestURI());
        return rel;

    }
}
