package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * @author 门那粒沙
 * @create 2019-12-28 23:29
 **/
@EnableTurbine
@EnableDiscoveryClient
@SpringBootApplication
public class TurbineMain {
    public static void main(String[] args) {
        SpringApplication.run(TurbineMain.class, args);
    }
}
