package pwd.allen.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * @author 门那粒沙
 * @create 2019-12-29 10:03
 **/
@EnableFeignClients(basePackages = {"pwd.allen.service"})
@Configuration
public class FeignConfig {
}
