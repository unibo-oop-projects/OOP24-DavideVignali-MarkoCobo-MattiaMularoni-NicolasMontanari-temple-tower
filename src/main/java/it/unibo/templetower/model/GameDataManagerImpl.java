package it.unibo.templetower.model;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import it.unibo.templetower.util.SystemInfo;

public class GameDataManagerImpl {
    private List<Floor> floors;
    private String floorsPath;
    private SystemInfo os = new SystemInfo();

    public void setFloorPath(String floorsPath){
        this.floorsPath = floorsPath;
    }

    public boolean verifyPath(String testPath){
        try (FileReader reader = new FileReader(testPath)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray floorsArray = jsonObject.getAsJsonArray("floors");
            return floorsArray != null && floorsArray.size() > 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
