SpringCloud Bus使用轻量级消息代理将分布式系统的节点连接起来。然后可以使用此代理广播状态更改(例如配置更改)或其他管理指令。
### 使用步骤
- 依赖
```xml
<!-- bus-RabbitMQ -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
```
- 配置rabbitmq连接信息
```yaml
spring:
  #配置rabbitmq
  rabbitmq:
    host: localhost
    username: guest
    password: guest
    port: 5672
```
- 如果要提供发送更新任务，需暴露bus-refresh端点
```yaml
management:
  endpoints:
    web:
      exposure:
        include:
          - bus-refresh #该端点用于向bus消息总线发布更新信息，比如发到rabbitmq，所有监听该rabbitmq的client都会收到消息从而可以更新配置
```

### 相关端点
- /actuator/bus-refresh：会刷新总线上其他服务实例
- /actuator/bus-refresh/{destination}：定位具体要刷新的应用程序，总线上的各应用示例会根据destination属性判断是否自己符合，不是则忽略该消息