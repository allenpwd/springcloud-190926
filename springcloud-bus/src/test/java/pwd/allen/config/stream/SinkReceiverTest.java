package pwd.allen.config.stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 门那粒沙
 * @create 2020-01-11 14:40
 **/
@SuppressWarnings("ALL")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StreamConfig.class)
public class SinkReceiverTest {

    /**
     * 方式一
     * 直接注入绑定消息通道的接口实例
     */
    @Autowired
    private Processor processor;

    /**
     * 方式二
     * 直接注入名为output的消息输入通道
     */
    @Qualifier(Source.OUTPUT)
    @Autowired
    private MessageChannel messageChannel;

    /**
     * 使用绑定output消息通道的消息生成者发送消息
     */
    @Test
    public void send() {
        //两个对象是一样的
        Assert.assertEquals(processor.output(), messageChannel);
        processor.output().send(MessageBuilder.withPayload("From processor").build());
        messageChannel.send(MessageBuilder.withPayload("From messageChannel").build());
        System.out.println("发送消息到名称为output的exchange");
    }
}