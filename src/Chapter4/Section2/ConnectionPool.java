package Chapter4.Section2;

import Chapter4.Section2.ConnectionDriver;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * 连接池的定义
 */
public class ConnectionPool {
    //双向队列
    private LinkedList<Connection> pool = new LinkedList<>();
    public ConnectionPool(int initialSize){
        if(initialSize>0){
            for (int i = 0; i < initialSize; i++) {
                pool.addLast(ConnectionDriver.createConnection());
            }
        }
    }
    public void releaseConnection(Connection connection){
        if (connection!=null){
            synchronized (pool){
                // 连接释放后需要进行通知，这样其他消费者能够感知到连接池中已经归还了一个连接
                pool.addLast(connection);
                pool.notifyAll();
            }
        }
    }
    public Connection fetchConnection(long mills) throws InterruptedException{
        synchronized (pool){
            if (mills<=0){
                while (pool.isEmpty()){
                    pool.wait();
                }
                return pool.removeFirst();//移除并返回此队列的第一个元素
            }else{
                long future = System.currentTimeMillis()+mills;
                long remaining = mills;
                while(pool.isEmpty()&&remaining>0){
                    pool.wait(remaining);
                    remaining = future-System.currentTimeMillis();
                }
                Connection result = null;
                if (!pool.isEmpty()){
                    result = pool.removeFirst();
                }
                return result;
            }
        }
    }
}
