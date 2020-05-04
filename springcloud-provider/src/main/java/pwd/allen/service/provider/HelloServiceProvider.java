package pwd.allen.service.provider;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pwd.allen.entity.User;
import pwd.allen.service.HelloService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 门那粒沙
 * @create 2019-09-26 21:34
 **/
@DefaultProperties(defaultFallback = "globalFallback", commandProperties = @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="1000"))
@RestController
public class HelloServiceProvider implements HelloService {

    @Value("port=${server.port}")
    private String message;


    /**
     * 服务聚合
     * @HystrixCollapser指定合并请求器，在100毫秒内的请求合并起来发给sayHellos批量查询，结果再拆分给各个请求
     * 注意：@HystrixCollapser和@HystrixCommand不能同时用
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
            if ("error".equals(name)) {
                throw new RuntimeException("测试下断路器");
            }
            String rel = String.format("hello!%s,this is %s,message=%s", name, this.getClass().getName(), message);
            list.add(rel);
        }
        return list;
    }

    /**
     * 这里@HystrixCommand没指定fallback属性，会采用@DefaultProperties的defaultFallback指定的fallback方法
     * 指定hystrix超时时间为1s，默认1s
     * @param map_param
     * @return
     */
    @HystrixCommand //如果这里注释掉的话，这个方法就不会有断路处理
    @Override
    public User getUser(@RequestBody Map map_param) {

        //TODO 报错 暂时注释掉
        //No thread-bound request found: Are you referring to request attributes outside of an actual web request, or processing a request outside of the originally receiving thread?
        // If you are actually operating within a web request and still receive this message, your code is probably running outside of DispatcherServlet/DispatcherPortlet:
        // In this case, use RequestContextListener or RequestContextFilter to expose the current request
//        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        User user = new User();
        user.setAge(18);
        if (map_param.containsKey("name")) {
            String name = (String) map_param.get("name");
            if ("error".equals(name)) {
                throw new RuntimeException("测试下断路器");
            }
            else if ("timeout".equals(name)) {
                //模拟下超时
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
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

    /**
     * 默认的全局fallback方法，如果@HystrixCommand没有特别指定fallback属性，则使用该默认的fallback方法
     * 返回值需要是 被降级的方法的返回值 的类型或者子类型，否则报错：Fallback method 'xxx(xxx)' must return: class xxx or its subclass
     * @param e
     * @return
     */
    public User globalFallback(Throwable e) {
        e.printStackTrace();
        User user = new User();
        user.setName("this is a global fallback:" + e.toString());
        return user;
    }
}
