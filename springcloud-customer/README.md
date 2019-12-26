### 服务调用方式
#### ribbon + restTemplate
Ribbon 是一个负载均衡客户端，可以很好的控制 http 和 tcp 的一些行为。
（1）依赖ribbon
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
</dependency>
```
（2）配置注入 RestTemplate 的 Bean，并通过 @LoadBalanced 注解表明开启负载均衡功能
（3）自定义ribbon规则
    方式一：@RibbonClient
    方式二：application.yml配置

#### feign
可使用 Feign 注解和 JAX-RS 注解。Feign 支持可插拔的编码器和解码器。Feign 默认集成了 Ribbon，并和 Eureka 结合，默认实现了负载均衡的效果
（1）依赖feign
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```
（2）通过 @EnableFeignClients 注解开启 Feign 功能


### 熔断器
Hystrix是Netflix开源的一款容错框架。
#### ribbon
（1）添加依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```
（2）增加 @EnableHystrix 注解
（3）在 Ribbon 调用方法上增加 @HystrixCommand 注解并指定 fallbackMethod 熔断方法
#### feign
Feign 是自带熔断器的，但默认是关闭的。需要在配置文件中配置打开它
（1）增加配置
```yaml
feign:
  hystrix:
    enabled: true
```
（2）在@FeignClient的fallback属性指定对应接口的熔断类，或者fallbackFactory属性指定实现熔断的工厂类（实现FallbackFactory）


### 参考链接
https://blog.csdn.net/loushuiyifan/article/details/82702522