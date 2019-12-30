package pwd.allen.service.provider;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pwd.allen.entity.User;
import pwd.allen.service.HelloService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author 门那粒沙
 * @create 2019-09-26 21:34
 **/
@RestController
public class HelloServiceProvider implements HelloService {

    @Value("port=${server.port}")
    private String message;


    /**
     * 服务聚合
     * 指定合并请求器，在100毫秒内的请求合并起来发给sayHellos批量查询，结果再拆分给各个请求
     *
     * 问题：scope默认为request，会报空指针，HystrixRequestContext没有初始化，使用hystrix缓存也会这样，暂时设置为GLOBAL绕过这个坑
     *
     * @param name
     * @return
     */
    @HystrixCollapser(batchMethod = "sayHellos", collapserProperties = {
            @HystrixProperty(name = "timerDelayInMilliseconds", value = "100")//指定合并时间窗，默认10毫秒
    }, scope = com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL)
    @Override
    public String sayHello(@PathVariable String name) {
        return null;
    }

    /**
     * 可以继承HelloService接口的PostMapping注解，但是参数上的注解需要重新定义
     *
     * HystrixCommand fallbackMethod属性指定服务降级时调用的方法，缺点：只能指定一个方法
     *
     * @param names
     * @return
     */
    @HystrixCommand(fallbackMethod = "sayHellosFallback")
    @Override
    public List<String> sayHellos(@RequestBody List<String> names) {
        ArrayList<String> list = new ArrayList<>();
        for (String name : names) {
            if ("error".equals(name)) throw new RuntimeException("测试下断路器");
            String rel = String.format("hello!%s,this is %s,message=%s", name, this.getClass().getName(), message);
            list.add(rel);
        }
        return list;
    }

    @Override
    public User getUser(@RequestBody Map map_param) {

        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        try {
            //模拟下超时
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

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

    /**
     * fallback方法要和被降级的方法有相同参数，否则：fallback method wasn't found
     * @param names
     * @return
     */
    public List<String> sayHellosFallback(List<String> names, Throwable e) {
        ArrayList<String> list = new ArrayList<>();
        list.add(String.format("this is callback for sayHellos,the error is %s, args=%s", e.toString(), names));
        return list;
    }
}
