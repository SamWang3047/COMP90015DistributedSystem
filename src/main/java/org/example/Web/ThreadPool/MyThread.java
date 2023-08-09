package org.example.Web.ThreadPool;

// 服务端程序
// 客户端程序与多线程版客户端程序一样
import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// 通过实现Runnable接口来创建多线程
class MyThread implements Runnable{
    private Socket clientSocket;

    MyThread(Socket client) {
        clientSocket = client;
    }

    public void run()  {
        try {
            InputStream inStream = clientSocket.getInputStream();
            OutputStream outStream = clientSocket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
            PrintWriter out = new PrintWriter(outStream);

            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println("Message from this client:" + line);
                out.println(line);
                out.flush();
            }
        }catch (Exception e) {
            System.out.println("Exception");
            return;
        }
        finally {
            try {
                clientSocket.close();
            }
            catch (Exception e){
                e.printStackTrace();
                return;
            }
        }
    }
}

