package pwd.allen.command;

import com.netflix.hystrix.*;
import org.springframework.web.client.RestTemplate;

/**
 * 封装具体的服务依赖调用逻辑
 *
 * 使用这个命令执行请求的方式：
 *      同步执行：execute()，返回结果
 *      异步执行：queue()，返回Future<String>，调用该对象的get方法来阻塞获取结果
 *
 * @author lenovo
 * @create 2019-12-27 15:04
 **/
public class MyHystrixCommand extends HystrixCommand<String> {

    private RestTemplate restTemplate;

    private String name;

    private Throwable throwable;

    public MyHystrixCommand(RestTemplate restTemplate, String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("helloService"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("other"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()//设置命令属性
                        .withCircuitBreakerRequestVolumeThreshold(10)//至少有10个请求，熔断器才进行错误率的计算
                        .withCircuitBreakerSleepWindowInMilliseconds(5000)//熔断器中断请求5秒后会进入半打开状态,放部分流量过去重试
                        .withCircuitBreakerErrorThresholdPercentage(50)//错误率达到50开启熔断保护
                        .withExecutionTimeoutEnabled(true))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()//设置线程池属性
                        .withCoreSize(10))
        );
        this.restTemplate = restTemplate;
        this.name = name;
    }

    @Override
    protected String run() throws Exception {
        String url = "http://HELLOSERVICE/other/{1}";
        return restTemplate.getForObject(url, String.class, name);
    }

    @Override
    protected String getFallback() {
        return String.format("fallback from %s，name=%s，throwable=%s", this.getClass().getName(), name, throwable.toString());
    }

    @Override
    protected Exception getExceptionFromThrowable(Throwable t) {
        this.throwable = t;
        return super.getExceptionFromThrowable(t);
    }
}
