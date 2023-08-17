package org.example.Web;

// 服务端程序
import java.io.*;
import java.net.*;

// 通过扩展Thread类来创建多线程
class MyThread extends Thread {
    private Socket clientSocket;

    MyThread(Socket client) {
        clientSocket = client;
    }

    public void run()  {
        try {
            InputStream inStream = clientSocket.getInputStream();
            OutputStream outStream = clientSocket.getOutputStream();
            /*
             InputStream：以字节为单位 InputStreamReader：以字符为单位
             BufferedReader： 以行为为单位进行处理（\r \r\n)
             */
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
            PrintWriter out = new PrintWriter(outStream);

            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println("Thread Number:"+ this.getName());
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







