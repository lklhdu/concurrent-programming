package Chapter8.Section2;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest3 {
    static CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    public static void main(String[] args) throws InterruptedException,
            BrokenBarrierException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    cyclicBarrier.await();
                }catch (Exception e){
                }
            }
        });
        thread.start();
        thread.interrupt();
        try{
            cyclicBarrier.await();
        }catch (Exception e){
        }
        System.out.println(cyclicBarrier.isBroken());
    }
}
