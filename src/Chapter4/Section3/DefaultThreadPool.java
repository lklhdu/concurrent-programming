package Chapter4.Section3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {
    // 线程池最大限制数
    private static final int MAX_WORKER_NUMBERS = 10;
    // 线程池默认的数量
    private static final int DEFAULT_WORKER_NUMBERS =5;
    // 线程池最小的数量
    private static final int MIN_WORKER_NUMBERS = 1;
    //工作列表，将会向里面插入工作；是工作者线程和客户端线程的连接媒介
    private final LinkedList<Job> jobs = new LinkedList<>();
    //工作者列表
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<Worker>());//线程安全的List
    //工作者线程的数量
    private int workerNum = DEFAULT_WORKER_NUMBERS;
    //线程编号生成
    private AtomicLong threadNum = new AtomicLong();

    public DefaultThreadPool(){
        initializeWorkers(DEFAULT_WORKER_NUMBERS);
    }

    @Override
    public void execute(Job job) {
        if (job!=null){
            synchronized (jobs){
                //添加一个工作，然后进行通知
                jobs.addLast(job);
                jobs.notify();
            }
        }
    }

    @Override
    public void shutdown() {
        for(Worker worker:workers){
            worker.shundown();
        }
    }

    @Override
    public void addWorkers(int num) {
        synchronized (jobs){
            if (num+this.workerNum>MAX_WORKER_NUMBERS){
                num = MAX_WORKER_NUMBERS-this.workerNum;
            }
            initializeWorkers(num);
            this.workerNum += num;
        }
    }

    @Override
    public void removeWorker(int num) {
        synchronized (jobs){
            if (this.workerNum-num<=0){
                throw new IllegalArgumentException("beyond workNum");
            }
            int count = 0;
            while (count<num){
                Worker worker = workers.get(count);
                if (workers.remove(worker)){
                    worker.shundown();
                    count++;
                }
            }
            this.workerNum-=count;
        }
    }

    @Override
    public int getJobSize() {
        return jobs.size();
    }

    //初始化线程工作者
    private void initializeWorkers(int workerNum){
        for (int i = 0; i < workerNum; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            Thread thread = new Thread(worker,
                    "ThreadPool-Worker-"+threadNum.incrementAndGet());
            thread.start();
        }
    }

    //工作者，负责消费任务
    class Worker implements Runnable{
        private volatile boolean running = true;

        @Override
        public void run() {
            while (running){
                Job job = null;
                synchronized (jobs){
                    while (jobs.isEmpty()){
                        try{
                            jobs.wait();
                        }catch (InterruptedException e){
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    job = jobs.removeFirst();
                }
                if (job!=null){
                    try {
                        job.run();
                    }catch (Exception e){
                    }
                }
            }
        }
        public void shundown(){
            running = false;
        }
    }
}
