package org.example.Client;
// 客户端程序
import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) throws Exception {

        String userInput = null;
        String echoMessage = null;

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        // 创建通信socket，连接服务端程序
        Socket socket = new Socket(InetAddress.getLocalHost(), 8189);
        System.out.println("Connected to Server");

        InputStream inStream = socket.getInputStream();
        OutputStream outStream = socket.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
        PrintWriter out = new PrintWriter(outStream);


        while(!(userInput=stdIn.readLine()).equals("quit"))
        {
            out.println(userInput);
            out.flush();
            echoMessage = in.readLine();
            System.out.println("Echo from server: " + echoMessage);
        }

        socket.close();

    }
}

