package pwd.allen.stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

/**
 * @author lenovo
 * @create 2020-01-09 17:14
 **/
@EnableBinding(Sink.class)
public class SinkReceiver {
    private static final Logger logger = LoggerFactory.getLogger(SinkReceiver.class);

    @StreamListener(Sink.INPUT)
    public void receive(Object payLoad) {
        System.out.println("Received:" + payLoad);
    }
}
