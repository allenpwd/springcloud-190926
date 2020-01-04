package pwd.allen.config;

import com.netflix.zuul.FilterFileManager;
import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.groovy.GroovyCompiler;
import com.netflix.zuul.groovy.GroovyFileFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 门那粒沙
 * @create 2020-01-04 16:55
 **/
@Configuration
@EnableConfigurationProperties({DynamicFilterProperties.class})
public class DynamicFilterConfig {

    /**
     * 动态加载过滤器，每隔5秒，从项目根目录config/filter/pre和config/fllter/post两个目录获取Groovy定义的过滤器，并进行编译和动态加载
     *
     * 已经加载的groovy即使删掉文件也无法从运行的API网关中移除这个过滤器，不过可以让shouldFilter返回false
     * 这种动态过滤器无法注入Spring容器中加载的实例
     *
     * @param properties
     * @return
     */
    @Bean
    public FilterLoader filterLoader(DynamicFilterProperties properties) {
        FilterLoader filterLoader = FilterLoader.getInstance();
        filterLoader.setCompiler(new GroovyCompiler());
        FilterFileManager.setFilenameFilter(new GroovyFileFilter());
        try {
            FilterFileManager.init(properties.getInterval(),
                    properties.getRoot() + "/pre",
                    properties.getRoot() + "/post");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return filterLoader;
    }
}
