package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author 门那粒沙
 * @create 2019-09-26 22:31
 **/
@EnableEurekaServer
@SpringBootApplication
public class EurekaMain {

    public static void main(String[] args) {
        SpringApplication.run(EurekaMain.class, args);
    }
}
