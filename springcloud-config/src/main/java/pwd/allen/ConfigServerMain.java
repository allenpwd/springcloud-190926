package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 为分布式系统中的基础设施和微服务应用提供集中化的外部配置支持
 *
 * 这里是服务端，一个分布式配置中心，
 *  用来连接配置仓库并为客户端提供获取配置信息、加密解密信息等访问接口
 *
 * 如果是从远程仓库获取配置，会存储一份在本地文件系统中，如果远程仓库无法获取时还能从本地返回
 *
 * 启动之后访问配置url方式：label可以看做git分支，比如master;
 *  可参考 {@link org.springframework.cloud.config.server.environment.EnvironmentController}
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
@EnableDiscoveryClient
@EnableConfigServer //为微服务架构提供集中化的外部配置支持
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})//要禁用security可以打开这个注解
public class ConfigServerMain {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerMain.class, args);
    }
}
