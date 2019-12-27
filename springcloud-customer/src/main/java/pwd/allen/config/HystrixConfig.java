package pwd.allen.config;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Configuration;

/**
 * Hystrix是Netflix开源的一款容错框架

 * 特点：
 *      使用命令模式将所有对外部服务（或依赖关系）的调用包装在HystrixCommand或HystrixObservableCommand对象中，对发送请求的对象和执行请求的对象进行解耦；
 *      每个依赖都维护着一个线程池（或信号量），线程池被耗尽则拒绝请求（而不是让请求排队）。
 *      记录请求成功，失败，超时和线程拒绝。
 *      服务错误百分比超过了阈值，熔断器开关自动打开，一段时间内停止对该服务的所有请求。
 *      请求失败，被拒绝，超时或熔断时执行降级逻辑。
 *      近实时地监控指标和配置的修改。
 *
 *
 * @author lenovo
 * @create 2019-12-27 9:12
 **/
//开启断路器，@EnableHystrix注解里面继承了这个注解
@EnableCircuitBreaker
@Configuration
public class HystrixConfig {
}
