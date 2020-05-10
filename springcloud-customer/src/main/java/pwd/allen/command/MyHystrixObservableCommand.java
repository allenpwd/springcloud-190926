package pwd.allen.command;

import com.netflix.hystrix.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;

/**
 * 使用MyHystrixObservableCommand封装具体的服务依赖调用逻辑，返回多个结果
 * 执行请求的方式：
 *      observe()：返回Observable对象（一个Hot Observable），它代表操作的多个结果
 *      toObservable()：返回Observable对象（一个Cold Observable）
 *
 * @author 门那粒沙
 * @create 2019-12-28 9:06
 **/
public class MyHystrixObservableCommand extends HystrixObservableCommand<String> {

    private RestTemplate restTemplate;

    private String name;

    //TODO 怎么没得设置线程池
    public MyHystrixObservableCommand(RestTemplate restTemplate, String name) {
        super(HystrixObservableCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("helloService"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("helloHOC"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()//设置命令属性
                        .withCircuitBreakerRequestVolumeThreshold(10)//至少有10个请求，熔断器才进行错误率的计算
                        .withCircuitBreakerSleepWindowInMilliseconds(5000)//熔断器中断请求5秒后会进入半打开状态,放部分流量过去重试
                        .withCircuitBreakerErrorThresholdPercentage(50)//错误率达到50开启熔断保护
                        .withExecutionTimeoutEnabled(true))
        );
        this.restTemplate = restTemplate;
        this.name = name;
    }

    @Override
    protected Observable<String> construct() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()) {
                        String url = "http://HELLOSERVICE/hello/{1}";
                        String rel = restTemplate.getForObject(url, String.class, name);
                        subscriber.onNext(rel);

                        //支持多次发送请求
                        rel = restTemplate.getForObject(url, String.class, name + "2");
                        subscriber.onNext(rel);
                        subscriber.onCompleted();
                    }
                } catch (RestClientException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    protected Observable<String> resumeWithFallback() {
        //获取执行时抛出的异常
        Throwable executionException = getExecutionException();
        String rel = String.format("fallback from %s，name=%s，throwable=%s", this.getClass().getName(), name, executionException.toString());
        return Observable.just(rel);
    }

}
