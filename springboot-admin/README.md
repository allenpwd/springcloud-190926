收集各个微服务系统的健康状态、会话数量、并发数、服务资源、延迟等度量信息

### Server
（1）增加依赖
```xml
<dependency>
    <groupId>org.jolokia</groupId>
    <artifactId>jolokia-core</artifactId>
</dependency>
<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-server</artifactId>
</dependency>
```
（2）通过 @EnableAdminServer 注解开启 Admin 功能
（3）application.yml
```yaml
management:
  endpoint:
    health:
      show-details: always
  endpoints:
    web:
      exposure:
        # 注意：此处在视频里是 include: ["health", "info"] 但已无效了，请修改
        include: health,info
```

### Client
（1）增加依赖
```xml
<dependency>
    <groupId>org.jolokia</groupId>
    <artifactId>jolokia-core</artifactId>
</dependency>
<dependency>
    <groupId>de.codecentric</groupId>
    <artifactId>spring-boot-admin-starter-client</artifactId>
</dependency>
```
（2）application.yml配置admin服务端地址
```yaml
spring:
  boot:
    admin:
      client:
        url: http://localhost:8084
```