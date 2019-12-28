package pwd.allen.config;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @EnableHystrixDashboard 开启仪表盘监控页面
 *
 * 开启单个实例的监控断点（hystrix.stream）
 *  bashboard1.x 只需@EnableHystrixDashboard即可
 *  bashboard2.x版本需要增加一个 HystrixMetricsStreamServlet 的配置
 *
 * @author 门那粒沙
 * @create 2019-11-25 20:54
 **/
@EnableHystrixDashboard
@Configuration
public class DashBoardConfig {

    @Bean
    public ServletRegistrationBean getServlet() {
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
}
