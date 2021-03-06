Hystrix使用命令模式（继承HystrixCommand类）来包裹具体的服务调用逻辑（run方法），并在命令模式中添加了服务调用失败后的降级逻辑（getFallback）。
同时在Command的构造方法中可以定义当前服务线程池和熔断器的相关参数。因此在使用了Command模式构建了服务对象之后，服务便拥有了熔断器和线程池的功能。

Hystrix的Metrics中保存了当前服务的健康状况，包括服务调用总次数和服务调用失败次数等。根据Metrics的计数，熔断器从而能计算出当前服务的调用失败率，用来和设定的阈值比较从而决定熔断器的状态切换逻辑。因此Metrics的实现非常重要。

Hystrix使用RxJava的Observable.window()实现滑动窗口。RxJava的window使用后台线程创建新桶，避免了并发创建桶的问题。同时RxJava的单线程无锁特性也保证了计数变更时的线程安全。从而使代码更加简洁。