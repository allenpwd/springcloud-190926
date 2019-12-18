package pwd.allen.config;

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
     *
     * 实现：给@LoadBalanced标注的RestTemplate添加LoadBalancerInterceptor拦截器
     * 当RestTemplate发起请求时会被拦截器的intercept方法拦截，方法里使用LoadBalancerClientd去根据服务名选择服务实例并发起实际的请求。
     * 选择服务实例有多种策略，可参考接口的实现类{@link com.netflix.loadbalancer.ILoadBalancer}，默认{@link com.netflix.loadbalancer.ZoneAwareLoadBalancer}
     *
     * 源码可以参考：{@link org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration}
     * {@link org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor}
     * {@link org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient}
     *
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
