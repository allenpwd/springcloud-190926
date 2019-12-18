import org.junit.Test;
import pwd.allen.entity.User;

/**
 * @author 门那粒沙
 * @create 2019-12-18 23:13
 **/
public class PwdTest {

    @Test
    public void test() {
        User user = new User();
        user.setName("pwd");
        user.setAge(12);

        User user1 = user.clone();

        System.out.println(user1);
    }
}
