package pwd.allen.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lenovo
 * @create 2019-12-18 12:08
 **/
@Data
public class User implements Serializable, Cloneable {
    private String name;
    private Integer age;
    private Date createDate;

    @Override
    public User clone() {
        try {
            return (User)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
