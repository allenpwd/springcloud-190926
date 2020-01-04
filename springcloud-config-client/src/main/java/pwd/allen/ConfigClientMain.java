package pwd.allen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 集中化配置的客户端
 *
 * 从集中化配置服务端获取外部配置文件并加载到ApplicationContext实例中，
 *  该配置内容的优先级高于客户端jar包内部的配置文件，所以可以覆盖jar包中重复的内容
 *
 * @author 门那粒沙
 * @create 2019-10-02 16:18
 **/
@SpringBootApplication
public class ConfigClientMain {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClientMain.class, args);
    }
}
