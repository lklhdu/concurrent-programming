package Chapter8.Section2;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {
    static CyclicBarrier cyclicBarrier =new CyclicBarrier(2);

    public static void main(String[] args) {
        //子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cyclicBarrier.await();
                }catch (Exception e){
                }
                System.out.println(1);
            }
        }).start();
        //主线程
        try{
            cyclicBarrier.await();
        }catch (Exception e){
        }
        System.out.println(2);
    }
}
