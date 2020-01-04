package pwd.allen.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 动态过滤器配置类
 *
 * @author 门那粒沙
 * @create 2020-01-04 17:00
 **/
@Data
@ConfigurationProperties("zuul.dynamic-filter")
public class DynamicFilterProperties {

    /**
     * 过滤器目录
     */
    private String root;
    /**
     * 刷新间隔时间
     */
    private Integer interval;
}
