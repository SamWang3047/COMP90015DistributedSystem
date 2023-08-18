package Server;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;

public class Dictionary {
    private String path = "Dictionary.json";
    private JSONObject dictionaryJSON;
    public Dictionary(String path) {
        this.path = path;
        initialization();
    }
    private void initialization() {
        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader(path);
            // Parse the JSON file
            dictionaryJSON = (JSONObject) parser.parse(reader);
            reader.close();
        } catch (IOException e) {
            //File not found
            e.printStackTrace();
        } catch (ParseException e) {
            //ParseException
            e.printStackTrace();
        }
    }
    public synchronized boolean isWordExist(String wordName) {
        return dictionaryJSON.containsKey(wordName);
    }
    public synchronized String query(String wordName) {
        return dictionaryJSON.get(wordName).toString();
    }
    public synchronized boolean add(String wordName, String wordMeaning) {
        if (dictionaryJSON.containsKey(wordName)) {
            return false;
        } else {
            dictionaryJSON.put(wordName, wordMeaning);
            updateLocalJSON();
            return true;
        }
    }

    public synchronized boolean remove(String wordName) {
        if (dictionaryJSON.containsKey(wordName)) {
            dictionaryJSON.remove(wordName);
            updateLocalJSON();
            return true;
        } else {
            return false;
        }
    }

    public synchronized void updateLocalJSON() {
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(dictionaryJSON.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public JSONObject getDictionaryJSON() {
        return dictionaryJSON;
    }

    public void setDictionaryJSON(JSONObject dictionaryJSON) {
        this.dictionaryJSON = dictionaryJSON;
    }
}

