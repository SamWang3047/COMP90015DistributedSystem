package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    public static void main(String[] args) throws Exception  {
        Socket clientSocket = null;
        ServerSocket listenSocket = new ServerSocket(8189);
        /*
            创建ThreadPoolExecutor
            参数意义：
                corePoolSize：线程池初始线程数目 6
                maximumPoolSize：线程池最大允许线程数目 12
                keepAliveTime：线程持续时间 100ms
                capacity：等待队列长度 10
                只有在线程数超过初始线程数目并且等待队列满的情况下才会继续创建新线程

         */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(6,12,100, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10));
        System.out.println("Server listening at 8189");
        while(true) {
            clientSocket = listenSocket.accept();
            System.out.println("Accepted connection from client");
            MyThread t = new MyThread(clientSocket);
            // 执行线程函数
            executor.execute(t);
        }
    }
}

