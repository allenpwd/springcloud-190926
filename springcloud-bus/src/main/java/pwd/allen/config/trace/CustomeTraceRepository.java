package pwd.allen.config.trace;

import java.util.List;
import java.util.Map;

/**
 * 模范HttpTraceRepository弄一个事件追踪的
 *
 * @author lenovo
 * @create 2020-01-08 15:34
 **/
public interface CustomeTraceRepository {

    /**
     * Find all {@link CustomTrace} objects contained in the repository.
     * @return the results
     */
    List<CustomTrace> findAll();

    /**
     * Adds a trace to the repository.
     * @param trace the trace to add
     */
    void add(Map<String, Object> trace);

}
