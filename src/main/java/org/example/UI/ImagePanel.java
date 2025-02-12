package org.example.UI;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ImagePanel extends JLabel {
    private BufferedImage image;

    public ImagePanel(BufferedImage image) {
        this.image = image;
        setIcon(new ImageIcon(image));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println("Clicked at: (" + x + ", " + y + ")");
                // Здесь можно добавить логику для обработки координат
            }
        });
    }
}