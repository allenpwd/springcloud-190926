package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import pwd.allen.config.MyRuleConfig;

/**
 * @author 门那粒沙
 * @create 2019-09-26 22:43
 **/
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"pwd.allen.service"})
//为指定应用自定义ribbon配置类，在配置类中声明IRule实现类，配置类不能被ComponentScan到，否则会覆盖所有的
@RibbonClient(name="helloservice", configuration=MyRuleConfig.class)
public class CustomerMain {
    public static void main(String[] args) {
        SpringApplication.run(CustomerMain.class, args);
    }
}
