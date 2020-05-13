import org.junit.Test;
import rx.Observable;
import rx.Observer;

/**
 * @author 门那粒沙
 * @create 2020-05-11 21:01
 **/
public class RxjavaTest {

    @Test
    public void test() {
        Observable<String> observable4Str = Observable.<String>unsafeCreate(subscriber -> {
            subscriber.onNext("123");
        });
        Observable<Integer> observable4Int = observable4Str.map(s -> Integer.parseInt(s));
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
            }
        });
    }
}
