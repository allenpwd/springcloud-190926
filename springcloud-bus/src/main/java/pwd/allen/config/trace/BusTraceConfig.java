package pwd.allen.config.trace;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pwd.allen.config.trace.CustomTraceRepositoryImpl;
import pwd.allen.config.trace.CustomeTraceRepository;
import pwd.allen.config.trace.TraceListener;

/**
 * springcloud新版没法直接开启事件追踪
 *
 * 自定义事件追踪
 *  配置条件：spring.cloud.bus.trace.enabled=true
 *  模拟TraceListener记录事件追踪
 *
 * @author lenovo
 * @create 2020-01-08 14:53
 **/
@ConditionalOnProperty(value = "spring.cloud.bus.trace.enabled", matchIfMissing = false)
@Configuration
public class BusTraceConfig {

    /**
     * 用于 保存监听器监听到的数据
     *
     * @return
     */
    @Bean
    public CustomeTraceRepository customeTraceRepository() {
        return new CustomTraceRepositoryImpl();
    }

    /**
     * ack和sent事件监听器
     *
     * @return
     */
    @Bean
    public TraceListener traceListener(CustomeTraceRepository repository) {
        return new TraceListener(repository);
    }
}
