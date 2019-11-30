package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 集中化配置
 *
 *
 * 启动之后访问配置url方式：label可以看做git分支，比如master
 *  http://ip:port/{application}/{profile}[/{label}]
 *  http://ip:port/{application}-{profile}.yml
 *  http://ip:port/{label}/{application}-{profile}.yml
 *  http://ip:port/{application}-{profile}.properties
 *  http://ip:port/{label}/{application}-{profile}.properties
 *
 * @author lenovo
 * @create 2019-09-30 11:37
 **/
@SpringBootApplication
@EnableConfigServer //为微服务架构提供集中化的外部配置支持
public class ConfigServerMain {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerMain.class, args);
    }
}
