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
 *      断路器开启的情况：当满足一定的阀值的时候（默认10秒内超过20个请求次数），且当失败率达到一定的时候（默认10秒内超过50%的请求失败）
 *      断路器开启后所有请求都不会进行转发，而是直接调用降级fallback，一段时间后（休眠时间窗，默认5秒），断路器半开状态，尝试转发请求，如果成功则关闭断路器，否则继续开启，休眠时间窗重新计时。
 *
 *
 * 开启方式：
 *  传统的方式，即直接把处理逻辑封装成HystrixCommand或者HystrixObservableCommand，不需要额外处理
 *  feign方式，配置feign.hystrix.enabled=true
 *  注解方式，@EnableCircuitBreaker
 *
 * @author lenovo
 * @create 2019-12-27 9:12
 **/
//开启基于注解的断路器，@EnableHystrix注解里面继承了这个注解；即使没有这个注解，基于传统方式或者feign的断路器也能用
@EnableCircuitBreaker
@Configuration
public class HystrixConfig {
}
