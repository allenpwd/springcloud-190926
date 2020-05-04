package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @EnableDiscoveryClient基于spring-cloud-commons, @EnableEurekaClient基于spring-cloud-netflix
 * 如果选用的注册中心是eureka，那么就推荐@EnableEurekaClient，如果是其他的注册中心，那么推荐使用@EnableDiscoveryClient
 *
 * @author 门那粒沙
 * @create 2019-09-26 21:37
 **/
//@SpringBootApplication
//@EnableDiscoveryClient
//@EnableCircuitBreaker   //开启断路器
@SpringCloudApplication   //这个注解相当于上面三个注解？
public class ProviderMain {

    public static void main(String[] args) {
        SpringApplication.run(ProviderMain.class, args);
    }
}
