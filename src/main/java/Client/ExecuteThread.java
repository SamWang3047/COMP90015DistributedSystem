package Client;

import org.json.simple.JSONObject;

import java.net.Socket;

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
