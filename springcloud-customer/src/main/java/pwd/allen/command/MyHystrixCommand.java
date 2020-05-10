package pwd.allen.command;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.springframework.web.client.RestTemplate;

/**
 * 使用HystrixCommand封装具体的服务依赖调用逻辑，返回单个结果
 *
 * 使用这个命令执行请求的方式：
 *      同步执行：execute()，从依赖的服务返回一个单一的结果，或发生错误后抛出异常
 *      异步执行：queue()，返回Future对象，调用该对象的get方法来阻塞获取结果
 *
 * 使用缓存：减轻线程消耗，请求缓存在run和construct执行之前生效
 *      如何开启：重载getCacheKey()方法，返回非null的key
 *
 * @author lenovo
 * @create 2019-12-27 15:04
 **/
public class MyHystrixCommand extends HystrixCommand<String> {

    private static final HystrixCommandKey COMMAND_KEY = HystrixCommandKey.Factory.asKey("helloHC");

    private RestTemplate restTemplate;

    private String name;

    public MyHystrixCommand(RestTemplate restTemplate, String name) {
        //只有withGroupKey静态函数可以创建Setter实例，故groupKey是必须的参数
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("helloService"))
                .andCommandKey(COMMAND_KEY)//设置命令名称，默认为类名
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()//设置命令属性
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)//设置执行的隔离策略
                        .withExecutionTimeoutEnabled(true)//是否开启执行超时处理
                        .withExecutionTimeoutInMilliseconds(1000)//执行超时时间，默认1000毫秒
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(10)//隔离策略使用信号量时最大并发请求量
                        .withFallbackEnabled(true)//是否启用服务降级
                        .withCircuitBreakerRequestVolumeThreshold(10)//至少有10个请求，熔断器才进行错误率的计算
                        .withCircuitBreakerSleepWindowInMilliseconds(5000)//熔断器中断请求5秒后会进入半打开状态,放部分流量过去重试
                        .withCircuitBreakerErrorThresholdPercentage(50)//错误率达到50开启熔断保护
                )
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("helloHC"))//设置线程池key，用于划分线程池，默认使用命令组
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()//设置线程池属性
                        .withCoreSize(10))
        );
        this.restTemplate = restTemplate;
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        String url = "http://HELLOSERVICE/hello/{1}";
        return restTemplate.getForObject(url, String.class, name);
    }

    @Override
    protected String getFallback() {
        //获取执行时抛出的异常
        Throwable executionException = getExecutionException();
        return String.format("fallback from %s，name=%s，throwable=%s", this.getClass().getName(), name, executionException.toString());
    }

    @Override
    protected String getCacheKey() {
        String cacheKey = this.getClass().getName() + "_" + name;
        return null;//妈个鸡，报错：Request caching is not available. Maybe you need to initialize the HystrixRequestContext，需要先初始化请求上下文，先不用了
    }

    /**
     * 根据cacheKey刷新缓存
     * @param cacheKey
     */
    public static void flushCache(String cacheKey) {
        HystrixRequestCache.getInstance(COMMAND_KEY, HystrixConcurrencyStrategyDefault.getInstance())
                .clear(cacheKey);
    }
}
