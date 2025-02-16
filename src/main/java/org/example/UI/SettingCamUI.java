package org.example.UI;

import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.Mat;
import org.example.Main;
import org.example.camera.ColorScanner;
import org.example.camera.Photographer;
import org.example.camera.RubiksCubeDetection;
import org.example.camera.SettingCamera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SettingCamUI extends JFrame {
    JPanel video = new JPanel();
    JPanel microVideo = new JPanel();
    private Photographer photographer;
    int nowPort = 0;
    ImagePanel imagePanel;
    boolean ferst=true;
    public SettingCamUI() {

        setTitle("Image setting");
        setSize(1150, 600);
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
                Main.scanner.detector.setting.camPort = nowPort;
                Main.scanner.detector.setting.point = imagePanel.nowPoint;
                photographer.end();
                try {
                    SettingCamera.saveToFile(Main.scanner.detector.setting,"setting");
                } catch (IOException ex) {
                    new MyException("не удалось сохранить настройки");
                }

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
        JPanel text = new JPanel();
        JTextField camNum = new JTextField("выберите камеру");
        text.add(camNum);
        buttonPanel.add(text);
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
                nowPort = selectedNumber;
                RubiksCubeDetection.photo = new Photographer(selectedNumber);
                photographer = RubiksCubeDetection.photo;
                timer.start();
                updateMiniImage();
            }
        });
        buttonPanel.add(comboBox);
        Main.scanner = new ColorScanner(false);
        addPicselBut(buttonPanel, 1, 4);
        addPicselBut(buttonPanel, 4, 7);
        return buttonPanel;

    }

    void addPicselBut(JPanel buttonPanel, int start, int finish) {
        JPanel panel = new JPanel();
        for (int i = start; i < finish; i++) {
            JButton button = new JButton(String.valueOf(i));
            int finalI = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    imagePanel.nowPointSwitch = (finalI);
                }
            });
            panel.add(button);
        }
        buttonPanel.add(panel);

    }

    JPanel edgesPanel() {
        //updateMiniImage();
        return microVideo;
    }

    private void updateImage() {
        Mat mat = photographer.getNext();
        BufferedImage image = matToBufferedImage(mat);
        imagePanel = new ImagePanel(image,this);
        if (ferst){
//285.0, 65.0, 343.0, 70.0, 394.0, 61.0, 288.0, 151.0, 344.0, 158.0, 389.0, 147.0
            ImagePanel.nowPoint = Main.scanner.detector.setting.point;
            ferst=false;
        }
        video.removeAll();
        video.add(imagePanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    void updateMiniImage() {
        Mat srcMat = new Mat(4, 1, opencv_core.CV_32FC2);
        Mat srcMat2 = new Mat(4, 1, opencv_core.CV_32FC2);
        RubiksCubeDetection.updateSrcMat(ImagePanel.nowPoint, srcMat, srcMat2);
        Mat micro1 = RubiksCubeDetection.getTransform(photographer.getNext(), srcMat, RubiksCubeDetection.dstMat);
        Mat micro2 = RubiksCubeDetection.getTransform(photographer.getNext(), srcMat2, RubiksCubeDetection.dstMat);
        BufferedImage image1 = matToBufferedImage(micro1);
        JLabel imagePanel1 = new JLabel();
        imagePanel1.setIcon(new ImageIcon(image1));
        BufferedImage image2 = matToBufferedImage(micro2);
        JLabel imagePanel2 = new JLabel();
        imagePanel2.setIcon(new ImageIcon(image2));
        microVideo.removeAll();
        microVideo.add(imagePanel1, BorderLayout.WEST);
        microVideo.add(imagePanel2, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private static BufferedImage matToBufferedImage(Mat mat) {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        return converter.convert(new OpenCVFrameConverter.ToMat().convert(mat));
    }

}