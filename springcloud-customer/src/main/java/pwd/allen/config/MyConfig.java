package pwd.allen.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author lenovo
 * @create 2019-09-29 15:24
 **/
@Configuration
public class MyConfig {

    /**
     * 配置注入 RestTemplate 的 Bean，并通过 @LoadBalanced 注解表明开启负载均衡功能
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
