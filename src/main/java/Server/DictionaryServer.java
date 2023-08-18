package Server;

import GUI.ServerUI;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DictionaryServer {
    private int port = 10240;
    private final int DEFAULT_CORE_POOL_SIZE = 5;
    private final int MAXIMUM_POOL_SIZE = 12;
    private final int KEEP_ALIVE_TIME = 1000;
    private final int BLOCKING_QUEUE_SIZE = 10;
    private Dictionary dictionary;
    private ServerSocket serverSocket;
    private ThreadPoolExecutor threadPool;
    private ServerUI ui;
    private int numClient;

    /*
        创建ThreadPoolExecutor
        参数意义：
            corePoolSize：线程池初始线程数目 6
            maximumPoolSize：线程池最大允许线程数目 12
            keepAliveTime：线程持续时间 100ms
            capacity：等待队列长度 10
            只有在线程数超过初始线程数目并且等待队列满的情况下才会继续创建新线程

     */
    private DictionaryServer(String port, String path) {
        this.port = Integer.parseInt(port);
        this.dictionary = new Dictionary(path);
        this.ui = null;
        this.threadPool = new ThreadPoolExecutor(
                DEFAULT_CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(BLOCKING_QUEUE_SIZE));
        this.numClient = 0;
    }
    private void run() {
        try {
            serverSocket = new ServerSocket(port);
            printStats();
            this.ui = new ServerUI(InetAddress.getLocalHost().getHostAddress(), String.valueOf(port), dictionary.getPath());
            ui.getFrame().setVisible(true);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept(); // Accept client connection
                    numClient++;
                    printLog("Server: A client connect.\n Current Num of client: " + String.valueOf(numClient));
                    threadPool.execute(new RequestHandlerThread(clientSocket, dictionary, this)); // Use thread pool to handle request
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printStats() throws UnknownHostException {
        InetAddress ip = InetAddress.getLocalHost();
        System.out.println("Server Running...");
        System.out.println("Current IP address : " + ip.getHostAddress());
        System.out.println("Port = " + port);
        System.out.println("Waiting for client connection...\n--------------");
    }

    private void printLog(String str) {
        System.out.println(str);
        if (ui != null) {
            ui.getLogArea().append(str + '\n');
        }
    }

    public synchronized void disconnect() {
        numClient--;
        printLog("Server: A client has disconnected."+ "\n" +
                "Server: Number of clients: " + numClient + "\n");
    }


    public static void main(String[] args) throws Exception {
        try {
            if (Integer.parseInt(args[0]) <= 1024 || Integer.parseInt(args[0]) >= 49151) {
                System.out.println("Invalid Port Number: Port number should be between 1024 and 49151!");
                System.exit(-1);
            }
            DictionaryServer dictionaryServer = new DictionaryServer(args[0], args[1]);
            dictionaryServer.run();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Lack of Parameters:\nPlease run like \"java - jar DictServer.jar <port> <dictionary-file>\"!");
        } catch (NumberFormatException e) {
            System.out.println("Invalid Port Number: Port number should be between 1024 and 49151!");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Socket clientSocket = null;
//        ServerSocket listenSocket = new ServerSocket(8189);
//
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(6, 12, 100, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));
//        System.out.println("Server listening at 8189");
//        while (true) {
//            clientSocket = listenSocket.accept();
//            System.out.println("Accepted connection from client");
//            RequestHandlerThread t = new RequestHandlerThread(clientSocket);
//            // 执行线程函数
//            executor.execute(t);
//        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPoolExecutor threadPool) {
        this.threadPool = threadPool;
    }

    public ServerUI getUi() {
        return ui;
    }

    public void setUi(ServerUI ui) {
        this.ui = ui;
    }
}

