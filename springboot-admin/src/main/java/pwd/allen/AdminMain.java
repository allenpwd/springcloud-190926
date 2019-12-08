package pwd.allen;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author 门那粒沙
 * @create 2019-12-08 20:51
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableAdminServer
public class AdminMain {

    public static void main(String[] args) {
        SpringApplication.run(AdminMain.class, args);
    }
}
