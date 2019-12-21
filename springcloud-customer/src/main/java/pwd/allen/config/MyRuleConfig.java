package pwd.allen.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * 自定义Ribbon客户端的方式有：
 *   1）注解@RibbonClient指定某个服务使用某个客户端配置
 *   2）配置文件中使用属性<clientName>.ribbon<key>=<value>，具体属性可以参考{@link org.springframework.cloud.netflix.ribbon.PropertiesFactory}和{@link com.netflix.client.config.CommonClientConfigKey}
 *
 * @author 门那粒沙
 * @create 2019-09-30 22:18
 **/
//@Configuration
public class MyRuleConfig {

    /**
     * 自定义负载均衡策略，如果放入spring容器中则会覆盖ribbon默认的负载均衡规则
     * @return
     */
    @Bean
    public IRule rule() {
        return new RandomRule();
    }
}
