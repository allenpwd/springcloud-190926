### Zipkin
Zipkin是 Twitter I的一个开源项目,它基于 Google Dapper实现。我们可以使用它来收
集各个服务器上请求链路的跟踪数据,并通过它提供的 REST API接口来辅助查询跟踪数
据以实现对分布式系统的监控程序,从而及时发现系统中出现的延退升高问题并找出系统
性能瓶颈的根源。除了面向开发的API接口之外,它还提供了方便的UI组件来帮助我们
直观地搜索跟踪信息和分析请求链路明细,比如可以查询某段时间内各用户请求的处理时
间等。

### 概念
- Trace ID：当请求发送到分布式系统的入口端点时,只需要服务跟踪框架为该请求创建一个唯一的跟踪标识,
同时在分布式系统内部流转的时候,框架始终保持传递该唯一标识,直到返回给请求方为止,
这个唯一标识就是Trace ID,通过 Trace ID的记录,我们就能将所有请求过程的日志关联起来。
- Span ID：为了统计各处理单元的时间延迟,当请求到达各个服务组件时,或是处理逻辑到达
某个状态时,也通过一个唯一标识来标记它的开始、具体过程以及结束,该标识就是Span ID。
对于每个Span来说,它必须有开始和结束两个节点,通过记录开始Span和结束Span的时间戬,
就能统计出该Span的时间延迟,除了时间戬记录之外,它还可以包含一些其他元数据,
比如事件名称、请求信息等。

### 相关组件
- Collector：收集器组件,它主要处理从外部系统发送过来的跟踪信息,将这些信息
转换为 Zipkin内部处理的Span格式,以支持后续的存储、分析、展示等功能。
- Storage：存储组件,它主要处理收集器接收到的跟踪信息,默认会将这些信息存储在内存中。
我们也可以修改此存储策略,通过使用其他存储组件将跟踪信息存储到数据库中。
- RESTFUL API：API组件,它主要用来提供外部访问接口。比如给客户端展示跟踪信
息,或是外接系统访问以实现监控等。
- Web UI：UI组件,基于API组件实现的上层应用。通过UI组件,用户可以方便而
又直观地査询和分析跟踪信息

### 使用步骤
服务端：收集链路数据，提供UI界面
（1）引入依赖
```xml
<!--链路追踪-->
<dependency>
    <groupId>io.zipkin.java</groupId>
    <artifactId>zipkin-server</artifactId>
</dependency>
<dependency>
    <groupId>io.zipkin.java</groupId>
    <artifactId>zipkin-autoconfigure-ui</artifactId>
</dependency>
```
（2）@EnableZipkinServer注解启动Zipkin Server
（3）推荐配置服务端口号为9411，自动化配置默认连接的是这个端口
客户端
（1）引入依赖
```xml
<!--链路追踪-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-sleuth-zipkin</artifactId>
</dependency>
```
（2）配置zipkin-server
```yaml
spring:
  zipkin:
    base-url: http://localhost:9411
  sleuth:
    sampler:
      #配置接口全部采样，默认0.1
      probability: 1.0
```


Spring Cloud Sleuth实现了在各个微服务的日志信息中添加跟踪信息的功能。
但是,由于日志文件都离散地存储在各个服务实例的文件系统之上,仅仅通过査看日志文件来分析我们的请求链路依然是一件相当麻烦的事,
所以我们还需要一些工具来帮助集中收集、存储和搜索这些跟踪信息。
引入基于日志的分析系统是一个不错的选择,比如ELK平台,它可以轻松地帮助我们收集和存储这些跟踪日志,
同时在需要的时候我们也可以根据 Trace ID来轻松地搜索出对应请求链路相关的明细日志。
但ELK平台中的数据分析维度缺少对请求链路中各阶段时间延迟的关注。

