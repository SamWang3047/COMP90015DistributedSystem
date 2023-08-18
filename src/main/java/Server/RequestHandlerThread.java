package Server;

import java.io.*;
import java.net.*;

class RequestHandlerThread implements Runnable{
    private Dictionary dictionary;
    private Socket clientSocket;

    private DictionaryServer server;

    public RequestHandlerThread(Socket client, Dictionary dictionary, DictionaryServer server) {
        this.clientSocket = client;
        this.dictionary = dictionary;
        this.server = server;
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

