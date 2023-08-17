package Server;

import java.util.HashMap;

public class Dictionary {
    private String path = "Dictionary.json";
    private HashMap<String, String> dictMap;



    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HashMap<String, String> getDictMap() {
        return dictMap;
    }

    public void setDictMap(HashMap<String, String> dictMap) {
        this.dictMap = dictMap;
    }
}
