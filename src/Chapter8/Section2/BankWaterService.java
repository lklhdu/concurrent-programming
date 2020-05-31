package Chapter8.Section2;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 模拟一个CyclicBarrier的应用场景
 */
public class BankWaterService implements Runnable {
    /**
     * 创建4个屏障，处理完后执行当前类的run方法
     */
    private CyclicBarrier cyclicBarrier = new CyclicBarrier(4,this);
    /**
     *创建一个线程数固定为4的线程池
     */
    private Executor executor = Executors.newFixedThreadPool(4);

    private ConcurrentHashMap<String,Integer> sheetBankWaterCount = new ConcurrentHashMap<>();
    private void count(){
        for (int i = 0; i < 4; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    //计算当前sheet的数据
                    sheetBankWaterCount.put(Thread.currentThread().getName(),1);
                    try{
                        cyclicBarrier.await();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void run() {
        int result = 0;
        //汇总每个sheet的计算结果
        for (Map.Entry<String,Integer> sheet:sheetBankWaterCount.entrySet()){
            result+=sheet.getValue();
        }
        sheetBankWaterCount.put("result",result);
        System.out.println(result);
    }

    public static void main(String[] args) {
        BankWaterService bankWaterService = new BankWaterService();
        bankWaterService.count();
    }
}
