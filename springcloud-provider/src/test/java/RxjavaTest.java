import org.junit.Test;
import rx.Observable;
import rx.Observer;

import java.util.ArrayList;

/**
 * @author 门那粒沙
 * @create 2020-05-11 21:01
 **/
public class RxjavaTest {

    /**
     * Observable 和 Observer
     * 能够发射0或n个数据，并以成功或者错误事件终止
     *  Observable：被观察者，决定什么时候触发事件以及触发怎样的事件
     *  Observer：观察者，可以在未来的某个时刻响应Observable的通知而不需要阻塞等待Observable发射数据
     *  subscribe：订阅，将创建的Observable和Observer连接起来，只有使用了subscribe被观察者才会开始发送数据
     */
    @Test
    public void Observable() {
        ArrayList<Integer> list = new ArrayList<>();

        Observable<String> observable4Str = Observable.<String>unsafeCreate(subscriber -> {
            subscriber.onNext("123");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            subscriber.onNext("37854");
            subscriber.onCompleted();
        });
        //将传递的数据类型进行转换
        Observable<Integer> observable4Int = observable4Str.map(s -> Integer.parseInt(s));
        //调用subscribe后阻塞直到数据流完成
        observable4Int.subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("completed");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("error:" + throwable);
            }

            @Override
            public void onNext(Integer i) {
                System.out.println("get:" + i);
                list.add(i);
            }
        });
        System.out.println(list);
    }
}
