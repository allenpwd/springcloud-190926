package pwd.allen.config.stream;

import org.springframework.cloud.stream.annotation.rxjava.EnableRxJavaProcessor;
import org.springframework.cloud.stream.annotation.rxjava.RxJavaProcessor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.Transformer;

/**
 * 基于RxJava的响应式编程来处理消息的输入输出
 *
 * @author 门那粒沙
 * @create 2020-01-12 8:54
 **/
@EnableRxJavaProcessor  //标识该类提供一个RxJavaProcessor实现bean，替代@EnableBinding(value = {Processor.class})
public class StreamConfig2 {

    /**
     * 采用observable观察者模式来消费和输出内容
     *
     * @return
     */
    @Bean
    public RxJavaProcessor<String, String> processor() {
        RxJavaProcessor<String, String> rxJavaProcessor = null;

        //实例一：接收到消息然后输出
        //第一个map处理输入消息，第二个map处理返回消息（不写的话直接返回）
        rxJavaProcessor = observable -> observable.map(s -> {
            System.out.println("received：" + s);
            return s;
        }).map(o -> String.valueOf("From Input Channel Return：" + o));

        //示例二：接收到5条消息之后才将处理结果返回给输出通道
//        rxJavaProcessor = observable -> observable.map(s -> {
//            System.out.println("received：" + s);
//            return s;
//        }).buffer(5).map(o -> String.valueOf("From Input Channel Return：" + o));

        return rxJavaProcessor;
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
