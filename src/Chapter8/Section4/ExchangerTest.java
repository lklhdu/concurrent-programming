package Chapter8.Section4;

import java.util.concurrent.*;

public class ExchangerTest {
    private static final Exchanger<String> exchanger = new Exchanger<String>();
    private static ExecutorService ThreadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String A = "银行流水A";
                    exchanger.exchange(A);
                }catch (InterruptedException e){
                }
            }
        });
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    String B = "银行流水A";
                    String A = exchanger.exchange(B);//到达同步点后交换数据
                    System.out.println("A和B的数据是否一致: "+A.equals(B));
                }catch (InterruptedException e){
                }
            }
        });
        ThreadPool.shutdown();
    }
}
