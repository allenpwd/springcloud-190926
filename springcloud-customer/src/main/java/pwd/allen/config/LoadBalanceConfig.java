package pwd.allen.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 *
 * Ribbon主要配置
 *   IClientConfig：客户端配置，比如{@link com.netflix.client.config.DefaultClientConfigImpl}
 *   IRule：负载均衡策略，比如{@link com.netflix.loadbalancer.ZoneAvoidanceRule}
 *   IPing：实例检查策略
 *   ServerList<Server>：服务实例清单的维护机制
 *   ServerListFilter<Server>：服务实例清单的过滤器
 *   ILoadBalancer：负载均衡器
 *
 * 如果Ribbon结合了Eureka，Eureka会对Ribbon自动配置{@link com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList}
 * {@link com.netflix.niws.loadbalancer.NIWSDiscoveryPing}
 * 参考{@link org.springframework.cloud.netflix.ribbon.eureka.EurekaRibbonClientConfiguration}
 *
 * @author lenovo
 * @create 2019-09-29 15:24
 **/
@EnableFeignClients(basePackages = {"pwd.allen.service"})
//为helloservice应用自定义ribbon配置类，在配置类中声明IRule实现类；配置类不能被ComponentScan到，否则会覆盖所有的
@RibbonClient(name="helloservice", configuration=MyRuleConfig.class)
@Configuration
public class LoadBalanceConfig {

    /**
     * 配置注入 RestTemplate 的 Bean，并通过 @LoadBalanced 注解通过拦截器给组件加上负载均衡功能
     *
     * 实现：给@LoadBalanced标注的RestTemplate添加LoadBalancerInterceptor拦截器
     * 当RestTemplate发起请求时会被拦截器的intercept方法拦截，方法里使用LoadBalancerClient去根据服务名选择服务实例（{@link org.springframework.cloud.client.ServiceInstance}）并发起实际的请求。
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
