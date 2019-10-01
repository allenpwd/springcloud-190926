package pwd.allen.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;

/**
 * @author 门那粒沙
 * @create 2019-09-30 22:18
 **/
public class MyRuleConfig {

    /**
     * 覆盖ribbon默认的负载均衡规则
     * @return
     */
    @Bean
    public IRule rule() {
        return new RandomRule();
    }
}
