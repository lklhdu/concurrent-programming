package Chapter8.Section2;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest2 {
    static CyclicBarrier cyclicBarrier = new CyclicBarrier(2,new myThread());

    public static void main(String[] args) {
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    cyclicBarrier.await();
                }catch (Exception e){
                }
                System.out.println(1);
            }
        }).start();
        try{
            cyclicBarrier.await();
        }catch (Exception e){
        }
        System.out.println(2);
    }
    static class myThread implements Runnable{
        @Override
        public void run() {
            System.out.println(3);
        }
    }
}
