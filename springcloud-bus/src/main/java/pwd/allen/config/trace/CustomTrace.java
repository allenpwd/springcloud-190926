package pwd.allen.config.trace;

import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;

/**
 * @author lenovo
 * @create 2020-01-08 15:30
 **/
public final class CustomTrace {
    private final Date timestamp;
    private final Map<String, Object> info;

    public CustomTrace(Date timestamp, Map<String, Object> info) {
        Assert.notNull(timestamp, "Timestamp must not be null");
        Assert.notNull(info, "Info must not be null");
        this.timestamp = timestamp;
        this.info = info;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public Map<String, Object> getInfo() {
        return this.info;
    }
}
