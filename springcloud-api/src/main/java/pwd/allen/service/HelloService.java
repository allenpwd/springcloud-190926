package pwd.allen.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 门那粒沙
 * @create 2019-09-26 21:29
 **/
@FeignClient(value = "helloservice")
public interface HelloService {

    @GetMapping("/hello/{name}")
    public String sayHello(@PathVariable("name") String name);
}
