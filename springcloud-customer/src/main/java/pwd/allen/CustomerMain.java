package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
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

