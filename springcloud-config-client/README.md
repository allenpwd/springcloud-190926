演示分布式配置中心客户端，从码云上读取配置，包括服务端口号

### 使用步骤
（1）引入依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```
（2）在bootstrap.yml中配置要读取的config server
```yaml
spring:
  cloud:
    config:
      name: myConfig #需要从github上读取的资源名称，不用加上yml等资源后缀
      profile: dev #本次访问的配置项
      label: master
      uri: http://localhost:7004 #springcloud config服务端地址
```