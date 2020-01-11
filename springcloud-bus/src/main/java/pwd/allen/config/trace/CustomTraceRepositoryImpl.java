package pwd.allen.config.trace;

import java.util.*;

/**
 * @author lenovo
 * @create 2020-01-08 15:31
 **/
public class CustomTraceRepositoryImpl implements CustomeTraceRepository {
    private int capacity = 100;
    private boolean reverse = true;
    private final List<CustomTrace> traces = new LinkedList();

    public void setReverse(boolean reverse) {
        List arg1 = this.traces;
        synchronized (this.traces) {
            this.reverse = reverse;
        }
    }

    public void setCapacity(int capacity) {
        List arg1 = this.traces;
        synchronized (this.traces) {
            this.capacity = capacity;
        }
    }

    public List<CustomTrace> findAll() {
        List arg0 = this.traces;
        System.out.println(this.traces.size());
        synchronized (this.traces) {
            return Collections.unmodifiableList(new ArrayList(this.traces));
        }
    }

    public void add(Map<String, Object> map) {
        CustomTrace trace = new CustomTrace(new Date(), map);
        List arg2 = this.traces;
        synchronized (this.traces) {
            while (this.traces.size() >= this.capacity) {
                this.traces.remove(this.reverse ? this.capacity - 1 : 0);
            }

            if (this.reverse) {
                this.traces.add(0, trace);
            } else {
                this.traces.add(trace);
            }

        }
    }
}
