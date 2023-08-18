package Client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class ExecuteThread extends Thread{
    private int state;
    private int command;
    private String word;
    private String meaning;
    private Socket socket;
    private String address;
    private int port;
    private String[] resultArr = {"", ""};

    private JSONObject requestJSON() {
        JSONObject requestJson = new JSONObject();
        requestJson.put("command", String.valueOf(command));
        requestJson.put("word", word);
        requestJson.put("meaning", meaning);
        return requestJson;
    }
    private JSONObject parseResString(String res) {
        JSONObject resJSON = null;
        try {
            JSONParser parser = new JSONParser();
            resJSON = (JSONObject) parser.parse(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resJSON;
    }

    public String[] getResult() {
        return resultArr;
    }

    public ExecuteThread(String address, int port, int command, String word, String meaning) {
        this.address = address;
        this.port = port;
        this.state = StateLib.FAIL;
        this.command = command;
        this.word = word;
        this.meaning = meaning;
        socket = null;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);  // has problem
            DataInputStream reader = new DataInputStream(socket.getInputStream());
            DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
            writer.writeUTF(requestJSON().toJSONString());
            writer.flush();
            String res = reader.readUTF();
            JSONObject resJSON = parseResString(res);
            state = Integer.parseInt(resJSON.get("state").toString());
            if (state == StateLib.SUCCESS) {
                meaning = (String) resJSON.get("meaning");
            }
            reader.close();
            writer.close();
        } catch (UnknownHostException e) {
            state = StateLib.UNKNOWN_HOST;
            System.out.println("Error: UNKNOWN HOST!");
        } catch (ConnectException e) {
            state = StateLib.CONNECTION_REFUSED;
            System.out.println("Error: CONNECTION REFUSED!");
        } catch (SocketTimeoutException e) {
            state = StateLib.TIMEOUT;
            System.out.println("Timeout!");
        } catch (SocketException e) {
            state = StateLib.IO_ERROR;
            System.out.println("Error: I/O ERROR!");
        } catch (IOException e) {
            state = StateLib.IO_ERROR;
            System.out.println("Error: I/O ERROR!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        resultArr[0] = String.valueOf(state);
        resultArr[1] = meaning;
    }


    public void setState(int state) {
        this.state = state;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String[] getResultArr() {
        return resultArr;
    }

    public void setResultArr(String[] resultArr) {
        this.resultArr = resultArr;
    }
}
