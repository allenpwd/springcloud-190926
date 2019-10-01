package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * 开启实时调用监控，
 * 访问地址为host:port/hystrix，
 * 监控服务调用：输入要监控的服务的/hystrix.stream，只能监控hystrix覆盖的方法
 *
 * @author 门那粒沙
 * @create 2019-10-01 23:06
 **/
@SpringBootApplication
@EnableHystrixDashboard //
public class DashBoardMain {
    public static void main(String[] args) {
        SpringApplication.run(DashBoardMain.class, args);
    }
}
