package Chapter10.Section4;

import java.util.concurrent.*;

/**
 * 任务。包括被执行任务需要实现的接口：Runnable接口或Callable接口
 * 异步计算的结果。包括接口Future和实现Future接口的FutureTask类
 */

public class FutureTaskTest {
    private final ConcurrentMap<Object, Future<String>> taskCache = new ConcurrentHashMap<>();
    private String executionTask(final String taskName)throws InterruptedException, ExecutionException {
        while (true){
            Future<String> future = taskCache.get(taskName); //1.1,2.1
            if (future==null){
                Callable<String> task = new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return taskName;
                    }
                };
                FutureTask<String> futureTask = new FutureTask<String>(task); //1.2新建任务
                future = taskCache.putIfAbsent(taskName,futureTask); //1.3
                if (future==null){
                    future=futureTask;
                    futureTask.run();
                } //1.4执行任务
            }
            try {
                return future.get();  //1.5，2.2线程在此等待任务执行完成
            }catch (CancellationException e){
                taskCache.remove(taskName,future);
            }
        }
    }
}