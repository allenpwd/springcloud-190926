package pwd.allen;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigClientTest {

    @Autowired
    Environment environment;

    @Value("${info.pwd.test}")
    private String msg;

    @Test
    public void test() {
        System.out.println(msg);
    }

}
