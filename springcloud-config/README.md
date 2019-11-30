### 分布式配置中心服务端
支持配置服务放在配置服务的内存中（即本地），也支持放在远程 Git 仓库中

### 使用步骤
（1）引入依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```
（2）配置类增加 @EnableConfigServer 注解，开启配置服务器功能
（3）springcloud配置文件
```yaml
spring:
  application:
    name: hello-spring-cloud-config
  cloud:
    config:
      label: master
      server:
        git:
          uri: https://github.com/topsale/spring-cloud-config
          search-paths: respo
          username:
          password:

server:
  port: 8888

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```

### 启动后可通过url查看配置
http://ip:port/{application}/{profile}[/{label}]
http://ip:port/{application}-{profile}.yml
http://ip:port/{label}/{application}-{profile}.yml
http://ip:port/{application}-{profile}.properties
http://ip:port/{label}/{application}-{profile}.properties