package pwd.allen.config.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author 门那粒沙
 * @create 2020-01-11 17:09
 **/
public interface SinkOutPut {

    String OUTPUT = "output";

    @Output(SinkOutPut.OUTPUT)
    MessageChannel output();
}
