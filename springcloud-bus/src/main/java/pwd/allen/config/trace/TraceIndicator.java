package pwd.allen.config.trace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import pwd.allen.config.trace.CustomeTraceRepository;

/**
 * 把CustomeTraceRepository里记录的数据暴露到health中
 *
 * health的名称是trace，逻辑在 {@link org.springframework.boot.actuate.health.HealthIndicatorNameFactory}
 *
 * @author lenovo
 * @create 2019-12-11 12:42
 **/
@Component("traceHealthIndicator")
public class TraceIndicator extends AbstractHealthIndicator {

    @Autowired
    private CustomeTraceRepository repository;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {

        builder.up().withDetail("datas", repository.findAll());
        builder.up().withDetail("desc", "自定义总线的事件追踪");

    }
}
