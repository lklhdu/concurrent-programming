package Chapter6;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class CountTask extends RecursiveTask<Integer> {
    private static final int THRESHOLD = 2;//阙值
    private int begin;
    private int end;
    public CountTask(int begin,int end){
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        boolean cancompute = (end-begin)<=THRESHOLD;
        if (cancompute){
            for (int i = begin; i <=end ; i++) {
                sum+=i;
            }
        }else {
            int middle = (begin+end)/2;
            CountTask leftTask = new CountTask(begin,middle);
            CountTask rightTask = new CountTask(middle+1,end);
            //执行子任务
            leftTask.fork();
            rightTask.fork();
            //等待子任务执行完，并得到结果
            int leftResult = leftTask.join();
            int rightResult = rightTask.join();
            sum = leftResult+rightResult;
        }
        return sum;
    }

    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();//ForkJoinTask需要通过ForkJoinPool来执行
        CountTask countTask = new CountTask(1,4);
        //执行一个任务
        Future<Integer> result = forkJoinPool.submit(countTask);
        try{
            System.out.println(result.get());
        }catch (InterruptedException e){
        }catch (ExecutionException e){
        }
    }
}
