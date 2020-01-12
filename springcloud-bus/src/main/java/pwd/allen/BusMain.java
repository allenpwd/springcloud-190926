package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.bus.event.*;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import pwd.allen.config.stream.StreamConfig2;

/**
 * 消息总线
 * 相关事件
 *  {@link RefreshRemoteApplicationEvent}：用于远程刷新应用的配置信息。
 *  {@link AckRemoteApplicationEvent}：用于告知某个事件消息已被接收
 *  {@link EnvironmentChangeRemoteApplicationEvent}：动态更新消息总线上每个节点的spring环境属性。
 *  {@link SentApplicationEvent}：用于发送信号来表示一个远程事件已经在系统中被发送到某个地方，它不是一个远程事件，所以不会被发送到消息总线上
 *
 * 事件监听器
 *  基于ApplicationListener
 *      {@link RefreshListener}：监控RefreshRemoteApplicationEvent事件，刷新配置属性
 *      {@link EnvironmentChangeListener}：监控EnvironmentChangeRemoteApplicationEvent事件，遍历事件中的Map对象来更新到EnvironmentManager中
 *  基于@EventListener
 *      {@link TraceListener}：监听AckRemoteApplicationEvent和SentApplicationEvent，记录发送和接收到的Ack事件信息，
 *          记录跟踪信息的接口默认实现是spring-boot-actuator模块的InMemoryTraceRepository（内存存储方式），需配置：spring.cloud.bus.trace.enabled=true
 *
 *
 * @author 门那粒沙
 * @create 2020-01-05 23:18
 **/
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = StreamConfig2.class))
@EnableDiscoveryClient
@SpringBootApplication
public class BusMain {
    public static void main(String[] args) {
        SpringApplication.run(BusMain.class, args);
    }
}
