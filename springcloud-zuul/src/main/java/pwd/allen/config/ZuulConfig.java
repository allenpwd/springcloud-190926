package pwd.allen.config;

import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lenovo
 * @create 2020-01-02 14:22
 **/
@Configuration
public class ZuulConfig {

    /**
     * 自定义默认的路由映射规则
     *
     * 将匹配servicePattern正则的服务名的路由路径根据routePattern去生成
     * 不匹配的则仍然按照默认的路由映射规则，即采用完整服务名作为前缀的路由表达式
     *
     * @return
     */
    @Bean
    public PatternServiceRouteMapper serviceRouteMapper() {
//        String servicePattern = "(?<name>^.+)-(?<version>v.+$)";
//        String routePattern = "${version}/${name}";
        //例子：hellocustomer -> hello/customer
        String servicePattern = "(?<test>^.{5})(?<name>.+$)";
        String routePattern = "${test}/${name}";
        return new PatternServiceRouteMapper(servicePattern, routePattern);
    }
}
