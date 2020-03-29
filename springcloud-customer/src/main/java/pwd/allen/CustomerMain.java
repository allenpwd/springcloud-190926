package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @EnableDiscoveryClient基于spring-cloud-commons, @EnableEurekaClient基于spring-cloud-netflix
 *  如果选用的注册中心是eureka，那么就推荐@EnableEurekaClient，如果是其他的注册中心，那么推荐使用@EnableDiscoveryClient
 *
 * @author 门那粒沙
 * @create 2019-09-26 22:43
 **/
//@EnableEurekaClient
@EnableDiscoveryClient
@SpringBootApplication
public class CustomerMain {
    public static void main(String[] args) {
        SpringApplication.run(CustomerMain.class, args);
    }
}

