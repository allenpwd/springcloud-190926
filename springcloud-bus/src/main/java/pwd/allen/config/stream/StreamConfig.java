package pwd.allen.config.stream;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.support.GenericMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * spring cloud stream
 * 通过绑定器Binder关联应用程序与消息中间件，起到隔离作用，使不同消息中间件的实现细节对应用程序来说是透明的。
 *
 * 原理：
 *  启动时会在RabbitMQ创建一个名为Input的Exchange交换器，
 *  定义输出通道需返回MessageChannel接口（定义了向消息通道发送消息的方法）对象；
 *  定义输入通道需返回SubscribableChannel接口（定义了维护消息通道订阅者的方法）对象；
 *
 * 相关注解
 *  {@link EnableBinding} ：指定一个或多个定义了@Input或@Output注解的接口，以此实现对消息通道（Channel）的绑定
 *  {@link StreamListener}：将被修饰的方法注册为消息中间件上数据流的时间监听器
 *
 * @author lenovo
 * @create 2020-01-09 17:14
 **/
@EnableBinding(value = {Processor.class})  //启动消息驱动功能，绑定Processor接口，绑定input和output两个消息通道
public class StreamConfig {

    /**
     * 将receive方法注册为input消息通道的监听处理器
     * 有以下两种指定方式
     *
     * 如果参数是Object接收到的是字节数组，若想返回字符串，可以将类型改成String，或者增加个@Transformer标注的消息转换方法
     *
     * @param payLoad
     */
    @StreamListener(Sink.INPUT)
//    @ServiceActivator(inputChannel = Sink.INPUT)    //spring integration原生支持的方式
    public void receive(Object payLoad) {
        System.out.println("Received:" + payLoad);
    }

    /**
     * 使用spring integration原生的@InboundChannelAdapter注解来注入输出消息通道
     * 效果：以10秒的频率向output通道输出当前时间
     *
     * @InboundChannelAdapter: 指定方法为output通道的输出绑定
     * poller属性：设置为轮询执行
     *
     * @return
     */
    @Bean
    @InboundChannelAdapter(value = SinkOutPut.OUTPUT, poller = @Poller(fixedDelay = "10000"))
    public MessageSource<Date> timerMessageSource() {
        return () -> new GenericMessage<>(new Date());
    }

    /**
     * 对output通道的消息进行转换
     * 效果：对上面定义的10秒输出当前时间的格式进行自定义
     *
     * inputChannel：指定要转换消息的输入通道
     * outputChannel；指定转换后的消息的输出通道
     *
     * @param message
     * @return
     */
    @Transformer(inputChannel = SinkOutPut.OUTPUT, outputChannel = SinkOutPut.OUTPUT)
    public Object transform4Date(Date message) {
        return new SimpleDateFormat("自定义格式：yyyy-MM-dd HH:mm:ss").format(message);
    }

    @Transformer(inputChannel = Sink.INPUT, outputChannel = Sink.INPUT)
    public Object transform4ByteArr(byte[] message) {
        return "经过转换后-》" + new String(message);
    }
}
