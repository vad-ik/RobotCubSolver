package org.example.UI;

import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.example.Main;
import org.example.camera.ColorScanner;
import org.example.camera.Photographer;
import org.example.camera.RubiksCubeDetection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class SettingCamUI extends JFrame {
    JPanel video = new JPanel();
    private Photographer photographer;
    int nowPort=0;
    float[] nowPoint = new float[12];
    public SettingCamUI() {

        setTitle("Image Click Example");
        setSize(800, 600);
//         Таймер для обновления изображения с камеры
        Timer timer = new Timer(500, e -> updateImage());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        photographer = RubiksCubeDetection.photo;

        add(video, BorderLayout.EAST);
        add(leftBorder(timer), BorderLayout.WEST);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Main.scanner.detector.setting.camPort=nowPort;
                Main.scanner.detector.setting.point=nowPoint;
                timer.stop();
            }
        });
    }



    JPanel leftBorder(Timer timer) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        JPanel button = buttonPanel(timer);
        JPanel edges = edgesPanel();
        mainPanel.add(button);
        mainPanel.add(edges);
        return mainPanel;
    }

    JPanel buttonPanel(Timer timer) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        JTextField camNum = new JTextField("выберите камеру");
        buttonPanel.add(camNum);
        // Создаем выпадающий список
        JComboBox<Integer> comboBox = new JComboBox<>();


        int numberOfCameras = Photographer.getNumberOfConnectedCameras();
        RubiksCubeDetection.photo = new Photographer(0);
//        timer.start();
        for (int i = 0; i < numberOfCameras; i++) {
            comboBox.addItem(i); // Добавляем элементы от 0 до N
        }

        // Добавляем обработчик событий
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                RubiksCubeDetection.photo.end();
                int selectedNumber = (int) comboBox.getSelectedItem();
                nowPort=selectedNumber;
                RubiksCubeDetection.photo = new Photographer(selectedNumber);
                photographer = RubiksCubeDetection.photo;
                timer.start();
            }
        });
        buttonPanel.add(comboBox);
        Main.scanner = new ColorScanner(false);

        return buttonPanel;

    }

    JPanel edgesPanel() {
        return new JPanel();
    }

    private void updateImage() {
        Mat mat = photographer.getNext();
        BufferedImage image = matToBufferedImage(mat);
        ImagePanel imagePanel = new ImagePanel(image);
        video.removeAll();
        video.add(imagePanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        return converter.convert(new OpenCVFrameConverter.ToMat().convert(mat));
    }

}