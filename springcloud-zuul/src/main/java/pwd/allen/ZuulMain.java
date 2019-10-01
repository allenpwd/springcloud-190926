package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * zuul包含了对请求的路由和过滤两个最主要的功能
 *  路由：将外部请求转发到具体的微服务实例上，是实现外部访问统一入口的基础
 *  过滤器：对请求的处理过程进行干预，是实现请求校验、服务聚合等功能的基础
 *
 * 原理：Zuul和Eureka进行整合，将zuul自身注册为Eureka服务治理下的应用，同时从Eureka中获得其他微服务的信息，以后访问微服务都通过zuul跳转后获得
 * 步骤：
 *      引入spring-cloud-starter-eureka、spring-cloud-starter-zuul
 *      添加@EnableZuulProxy
 *
 * @author 门那粒沙
 * @create 2019-10-01 23:06
 **/
@SpringBootApplication
@EnableZuulProxy
public class ZuulMain {
    public static void main(String[] args) {
        SpringApplication.run(ZuulMain.class, args);
    }
}
