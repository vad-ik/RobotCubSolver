package org.example.camera;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.UI.MyException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SaveSettings {
    private float[] point = new float[12];
    private int camPort;
    private String comPort;


    public static void saveToFile(SaveSettings config, String filename) throws IOException {
        Gson gson = new GsonBuilder().create();
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(config, writer);
        }
    }

    public static SaveSettings loadFromFile(String filename) {
        Gson gson = new GsonBuilder().create();
        try {
            FileReader reader = new FileReader(filename);

            return gson.fromJson(reader, SaveSettings.class);
        } catch (Exception e) {

            new MyException("не удалось экспортировать настройки, они были заменены на настройки по умолчанию");
            SaveSettings set = new SaveSettings();

            set.setPoint(new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
            set.setCamPort(0);
            set.setComPort("COM3");
            return set;
        }
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