package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 注册中心存储了两层Map结构，第一层的key是服务名；第二层的key是实例名
 *
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
