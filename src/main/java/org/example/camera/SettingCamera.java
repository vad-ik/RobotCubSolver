package org.example.camera;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingCamera {

    public float[] point = new float[12];
    public int camPort;

    public static void saveToFile(SettingCamera config, String filename) throws IOException {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(config, writer);
        }
    }

    public static SettingCamera loadFromFile(String filename) throws IOException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filename)) {
            return gson.fromJson(reader, SettingCamera.class);
        }
    }
}