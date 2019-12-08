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