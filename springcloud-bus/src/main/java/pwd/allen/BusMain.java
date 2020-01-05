package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 门那粒沙
 * @create 2020-01-05 23:18
 **/
@EnableDiscoveryClient
@SpringBootApplication
public class BusMain {
    public static void main(String[] args) {
        SpringApplication.run(BusMain.class, args);
    }
}
