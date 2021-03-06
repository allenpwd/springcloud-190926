Zuul是Netflix开源的微服务网关，可以和Eureka、Ribbon、Hystrix等组件配合使用。
Spring Cloud对Zuul进行了整合与增强，Zuul默认使用的HTTP客户端是Apache HTTPClient。

Zuul的主要功能是路由转发和过滤器。

- 它作为系统的统一入口,屏蔽了系统内部各个微服务的细节。
- 它可以与服务治理框架结合,实现自动化的服务实例维护以及负载均衡的路由转发。
- 它可以实现接口权限校验与微服务业务逻辑的解耦。
- 通过服务网关中的过滤器,在各生命周期中去校验请求的内容,将原本在对外服务
层做的校验前移,保证了微服务的无状态性,同时降低了微服务的测试难度,让服
务本身更集中关注业务逻辑的处理。
