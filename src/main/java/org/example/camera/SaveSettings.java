package org.example.camera;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.UI.MyException;

import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SaveSettings {
    private Map<String, Color> colorMap;
    private float[] point = new float[12];
    private int camPort;
    private String comPort;


    public static void saveToFile(SaveSettings config, String filename) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Color.class, new ColorTypeAdapter())
                .create();
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(config, writer);
        }
    }

    public static SaveSettings loadFromFile(String filename) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Color.class, new ColorTypeAdapter())
                .create();
        try {
            FileReader reader = new FileReader(filename);

            return gson.fromJson(reader, SaveSettings.class);
        } catch (Exception e) {

            new MyException("не удалось экспортировать настройки, они были заменены на настройки по умолчанию");
            SaveSettings set = new SaveSettings();

            set.setPoint(new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
            set.setCamPort(0);
            set.setComPort("COM3");
            set.setColorMap(new HashMap<>());
            set.getColorMap().put("white", new Color(255, 255, 255));
            set.getColorMap().put("orange", new Color(255, 165, 0));
            set.getColorMap().put("green", new Color(0, 128, 0));
            set.getColorMap().put("red", new Color(255, 0, 0));
            set.getColorMap().put("blue", new Color(0, 0, 255));
            set.getColorMap().put("yellow", new Color(205, 255, 50));
            return set;
        }


    }

    public Map<String, Color> getColorMap() {
        return colorMap;
    }

    public void setColorMap(Map<String, Color> colorMap) {
        this.colorMap = colorMap;
    }

    public float[] getPoint() {
        return point;
    }

    public void setPoint(float[] point) {
        this.point = point;
    }

    public int getCamPort() {
        return camPort;
    }

    public void setCamPort(int camPort) {
        this.camPort = camPort;
    }

    public String getComPort() {
        return comPort;
    }

    public void setComPort(String comPort) {
        this.comPort = comPort;
    }
}