package Chapter11.Section1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

/**
 * 多生产者和多消费者场景
 * 伪代码，思路更重要
 */
public class MsgQueueMnager {
    public final BlockingQueue<Message> messageQueue;

    private static volatile MsgQueueMnager msgQueueMnager;

    private MsgQueueMnager(){
        messageQueue = new LinkedTransferQueue<>();
    }

    /**
     * 对象实例化
     * @return
     */
    public static MsgQueueMnager getInstance(){
        if (msgQueueMnager==null){
            synchronized (MsgQueueMnager.class){
                if (msgQueueMnager==null){
                    msgQueueMnager = new MsgQueueMnager();
                }
            }
        }
        return msgQueueMnager;
    }

    public void put(Message msg){
        try {
            messageQueue.put(msg);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    public Message take(){
        try{
            return messageQueue.take();
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
        return null;
    }

    /**
     * 分发消息，负责将消息从大队列里塞到小队列里
     */
    static class DispatchMessageTask implements Runnable{
        @Override
        public void run() {
            BlockingQueue<Message> subQueue;
            for (;;){
                //获取数据msg
                //如果没有数据，则阻塞在这里

                while ((subQueue=getInstance().getSubQueue())==null){
                    try{
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                }
                //把消息放到小队列里
                try{
                    //put操作
                    subQueue.put(new Message());
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * 获取一个子队列
     * @return
     */
    public BlockingQueue<Message> getSubQueue(){
        //此处省略
        return null;
    }

    static class Message{
        String data;
    }
}
