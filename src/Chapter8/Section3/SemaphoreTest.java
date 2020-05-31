package Chapter8.Section3;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {
    private static final int THREAD_COUNT = 30;
    private static Executor executor = Executors.newFixedThreadPool(THREAD_COUNT);
    private static Semaphore s =new Semaphore(10);

    public static void main(String[] args) {
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        s.acquire();
                        System.out.println("data save");
                        s.release();
                        System.out.println(s.availablePermits());
                    }catch (InterruptedException e){
                    }
                }
            });
        }
    }
}
