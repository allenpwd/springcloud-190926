package pwd.allen.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lenovo
 * @create 2019-12-18 12:08
 **/
@Data
public class User implements Serializable {
    private String name;
    private Integer age;
    private Date createDate;
}
