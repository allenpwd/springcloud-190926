package pwd.allen.config.stream;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.*;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.handler.annotation.SendTo;
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
 *  {@link StreamListener}：将被修饰的方法注册为消息中间件上数据流的事件监听器
 *  {@link SendTo}：消息处理后返回的内容以消息的方式发送到指定的输出通道
 *
 * @author lenovo
 * @create 2020-01-09 17:14
 **/
@EnableBinding(value = {Processor.class})  //启动消息驱动功能，绑定Processor接口，绑定input和output两个消息通道
public class StreamConfig {

    /**
     * 将receive方法注册为input消息通道的监听处理器，当输入消息通道中有消息到达时会触发receive方法对消息进行消费

     * 有以下两种指定方式
     *  （1）StreamListener：功能更强大，内置了消息转换功能，
     *      比如JSON串转实体类（需配置属性spring.cloud.stream.bindings.input.content-type=application/json）
     *      原理：在消息消费逻辑执行之前，消息转换机制会根据消息头信息中声明的消息类型找到对应的消息转换器并实现对消息的自动转换。
     *  （2）ServiceActivator：spring integration原生支持的方式
     *
     * 如果参数是Object接收到的是字节数组，若想返回字符串，可以将类型改成String，或者增加个@Transformer标注的消息转换方法
     *
     * @param payLoad
     */
    @SendTo(Processor.OUTPUT)
    @StreamListener(Sink.INPUT)
//    @ServiceActivator(inputChannel = Sink.INPUT, outputChannel = Processor.OUTPUT)    //spring integration原生支持的方式
    public Object receive(Object payLoad) {
        String rel = "Received:" + payLoad;
        System.out.println(rel);
        return rel;
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

    /**
     * 对input输入通道进行消息转换
     * 问题：第二次之后才有转换 暂不知原因
     *
     * @param message
     * @return
     */
    @Transformer(inputChannel = Sink.INPUT, outputChannel = Sink.INPUT)
    public Object transform4ByteArr(byte[] message) {
        return "经过转换后-》" + new String(message);
    }
}
