package org.example.camera;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.awt.Color;
import java.io.IOException;

public class ColorTypeAdapter extends TypeAdapter<Color> {
    @Override
    public void write(JsonWriter out, Color color) throws IOException {
        if (color == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name("red").value(color.getRed());
        out.name("green").value(color.getGreen());
        out.name("blue").value(color.getBlue());
        out.endObject();
    }

    @Override
    public Color read(JsonReader in) throws IOException {
        in.beginObject();
        int red = 0, green = 0, blue = 0;
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "red":
                    red = in.nextInt();
                    break;
                case "green":
                    green = in.nextInt();
                    break;
                case "blue":
                    blue = in.nextInt();
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        return new Color(red, green, blue);
    }
}