import feign.Feign;
import feign.Logger;
import feign.hystrix.HystrixFeign;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import pwd.allen.service.HelloService;

import java.util.HashMap;

/**
 * 先把ProviderMain服务提供者启动，然后用Feign测试provider接口
 *
 * @author 门那粒沙
 * @create 2020-05-04 15:40
 **/
public class FeignTest {

    private HelloService helloService;

    @Before
    public void init() {
        ObjectFactory<HttpMessageConverters> objectFactory = () -> {
            return new HttpMessageConverters(new MappingJackson2HttpMessageConverter());
        };

        helloService = Feign.builder()
                .contract(new SpringMvcContract())  //不设置这个会报错：feign Method sayHello not annotated with HTTP method type (ex. GET, POST)
                .logLevel(Logger.Level.NONE)        //设置了无效啊，还是打印一堆日志
                .encoder(new SpringEncoder(objectFactory))
                .decoder(new SpringDecoder(objectFactory))
                .target(HelloService.class, "http://localhost:8001");
    }

    @Test
    public void testGet() {
        System.out.println(helloService.sayHello("朱一旦"));
    }

    /**
     * 要设置编码解码器，才能正常传输map参数
     */
    @Test
    public void testPost() {
        HashMap<String, String> map_param = new HashMap<>();
        map_param.put("name", "王世充");
        System.out.println(helloService.getUser(map_param));
    }
}
