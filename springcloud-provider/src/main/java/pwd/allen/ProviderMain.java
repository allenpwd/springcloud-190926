package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author 门那粒沙
 * @create 2019-09-26 21:37
 **/
@SpringBootApplication
@EnableEurekaClient
public class ProviderMain {

    public static void main(String[] args) {
        SpringApplication.run(ProviderMain.class, args);
    }
}
