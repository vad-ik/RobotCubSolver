package org.example.UI;

import org.example.camera.RubiksCubeDetection;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ImagePanel extends JLabel {
    private BufferedImage image;

    static float[] nowPoint = new float[12];
    static int nowPointSwitch=-1;

    public ImagePanel(BufferedImage image,SettingCamUI UI ) {
        this.image = image;
        setIcon(new ImageIcon(image));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (nowPointSwitch!=-1) {
                    nowPoint[nowPointSwitch * 2 - 2] = x;
                    nowPoint[nowPointSwitch * 2 - 1] = y;
                    System.out.println(Arrays.toString(nowPoint));
                    UI.updateMiniImage();
                }
                // Здесь можно добавить логику для обработки координат
            }
        });
    }
}