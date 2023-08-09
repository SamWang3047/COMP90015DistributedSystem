package org.example.Web;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception  {
        // ClientSocket 为通信socket
        Socket clientSocket = null;
        // 监听8189端口 ServerSocket为监听socket
        ServerSocket listenSocket = new ServerSocket(8189);

        System.out.println("Server listening at 8189");
        while(true) {
            // 从连接队列中取出通信socket
            clientSocket = listenSocket.accept();
            System.out.println("Accepted connection from client");
            // 创建新线程并执行线程函数
            MyThread t = new MyThread(clientSocket);
            t.start();
        }
    }
}