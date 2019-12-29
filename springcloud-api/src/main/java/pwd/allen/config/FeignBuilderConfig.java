package pwd.allen.config;

import feign.Feign;
import feign.Logger;
import feign.hystrix.HystrixFeign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * 自定义一个Feign的配置类
 *
 * 使用方式：@FeignClient的configuration属性指定
 *
 * @author 门那粒沙
 * @create 2019-12-29 22:45
 **/
@Configuration
public class FeignBuilderConfig {

    /**
     * 关闭hystrix
     *
     * @return
     */
//    @Bean     //注释掉的话可使之失效
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }

    /**
     * 开启所有日志，fegin默认是none
     *
     *  BASIC：仅记录请求方法、URL、响应状态码和执行时间
     *  HEADERS：多记录请求和响应的头信息
     *  FULL：多记录请求体、元数据
     *
     * @return
     */
//    @Bean       //先注释掉
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
