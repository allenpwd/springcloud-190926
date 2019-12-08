package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin.server.internal.EnableZipkinServer;

/**
 * @author 门那粒沙
 * @create 2019-12-05 23:46
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableZipkinServer
public class ZipKinMain {

    public static void main(String[] args) {
        SpringApplication.run(ZipKinMain.class, args);
    }
}
