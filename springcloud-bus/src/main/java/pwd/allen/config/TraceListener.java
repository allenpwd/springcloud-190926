package pwd.allen.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.cloud.bus.event.AckRemoteApplicationEvent;
import org.springframework.cloud.bus.event.SentApplicationEvent;
import org.springframework.context.event.EventListener;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A listener for sends and acks of remote application events. Inserts a record for each
 * signal in the {@link HttpTraceRepository}.
 *
 * @author Dave Syer
 */
public class TraceListener {

    private static Log log = LogFactory.getLog(org.springframework.cloud.bus.event.TraceListener.class);

    private CustomeTraceRepository repository;

    public TraceListener(CustomeTraceRepository repository) {
        this.repository = repository;
    }

    @EventListener
    public void onAck(AckRemoteApplicationEvent event) {
        Map<String, Object> trace = getReceivedTrace(event);
        this.repository.add(trace);
    }

    @EventListener
    public void onSend(SentApplicationEvent event) {
        Map<String, Object> trace = getSentTrace(event);
        this.repository.add(trace);
    }

    protected Map<String, Object> getSentTrace(SentApplicationEvent event) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("signal", "spring.cloud.bus.sent");
        map.put("type", event.getType().getSimpleName());
        map.put("id", event.getId());
        map.put("origin", event.getOriginService());
        map.put("destination", event.getDestinationService());
        if (log.isDebugEnabled()) {
            log.debug(map);
        }
        return map;
    }

    protected Map<String, Object> getReceivedTrace(AckRemoteApplicationEvent event) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("signal", "spring.cloud.bus.ack");
        map.put("event", event.getEvent().getSimpleName());
        map.put("id", event.getAckId());
        map.put("origin", event.getOriginService());
        map.put("destination", event.getAckDestinationService());
        if (log.isDebugEnabled()) {
            log.debug(map);
        }
        return map;
    }
}
