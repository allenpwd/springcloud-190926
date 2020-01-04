package pwd.allen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lenovo
 * @create 2020-01-02 17:02
 **/
@RestController()
public class LocalController {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RouteLocator routeLocator;

    @GetMapping("localForward/**")
    public Object localForward(HttpServletRequest request) {

        String action = request.getParameter("action");
        if ("refresh".equals(action)) {
            applicationContext.publishEvent(new RoutesRefreshedEvent(routeLocator));
        }

        String rel = String.format("本地转发，requestURI=%s", request.getRequestURI());
        return rel;
    }
}
