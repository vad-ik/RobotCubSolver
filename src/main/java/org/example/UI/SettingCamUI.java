package org.example.UI;

import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.Mat;
import org.example.Main;
import org.example.camera.Photographer;
import org.example.camera.RubiksCubeDetection;
import org.example.camera.SaveSettings;

import javax.swing.*;
import java.awt.*;
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
    boolean first = true;
    ImageIcon icon1 = new ImageIcon();
    ImageIcon icon2 = new ImageIcon();

    public SettingCamUI() {

        setTitle("Настройки");
        setSize(1150, 600);
//         Таймер для обновления изображения с камеры
        Timer timer = new Timer(50, e -> updateImage());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        timer.start();
        photographer = RubiksCubeDetection.photo;

        add(video, BorderLayout.EAST);
        add(leftBorder(timer), BorderLayout.WEST);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    SaveSettings.saveToFile(Main.scanner.detector.setting, "setting");
                } catch (IOException ex) {
                    new MyException("не удалось сохранить настройки");
                }
                Main.scanner.detector.setting.setCamPort(nowPort);
                Main.scanner.detector.setting.setPoint(ImagePanel.nowPoint);
                RubiksCubeDetection.photo.end();
                photographer.end();
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


        java.util.List<String> numberOfCameras = Photographer.getConnectedCameras();
        if (RubiksCubeDetection.photo == null) {
            RubiksCubeDetection.photo = new Photographer(0);
            photographer = RubiksCubeDetection.photo;
            timer.start();
        }
        JComboBox<String> comboBox = new JComboBox<>(numberOfCameras.toArray(new String[0]));


        // Добавляем обработчик событий
        comboBox.addActionListener(_ -> {
            timer.stop();
            int selectedNumber = comboBox.getSelectedIndex();
            nowPort = selectedNumber;
            if (RubiksCubeDetection.photo != null) {
                RubiksCubeDetection.photo.end();
            }
            RubiksCubeDetection.photo = new Photographer(selectedNumber);

            photographer = RubiksCubeDetection.photo;
            timer.start();
            updateMiniImage();
        });
        buttonPanel.add(comboBox);

        addPixelBut(buttonPanel, 1, 4);
        addPixelBut(buttonPanel, 4, 7);
        return buttonPanel;
    }

    void addPixelBut(JPanel buttonPanel, int start, int finish) {
        JPanel panel = new JPanel();
        for (int i = start; i < finish; i++) {
            JButton button = new JButton(String.valueOf(i));
            int finalI = i;
            button.addActionListener(_ -> ImagePanel.nowPointSwitch = (finalI));
            panel.add(button);
        }
        buttonPanel.add(panel);
    }

    JPanel edgesPanel() {


        JLabel imagePanel1 = new JLabel();
        JLabel imagePanel2 = new JLabel();

        imagePanel1.setIcon(icon1);
        imagePanel2.setIcon(icon2);
        microVideo.removeAll();
        microVideo.add(imagePanel1, BorderLayout.WEST);
        microVideo.add(imagePanel2, BorderLayout.SOUTH);
        return microVideo;
    }

    private static short iter = 0;

    private void updateImage() {
        Mat mat = photographer.getNext();
        BufferedImage image = matToBufferedImage(mat);

        imagePanel = new ImagePanel(image, this);


        if (first) {
            ImagePanel.nowPoint = Main.scanner.detector.setting.getPoint();
            first = false;
        }
        video.removeAll();
        video.add(imagePanel, BorderLayout.CENTER);
        if (iter % 10 == 0) {
            updateMiniImage();
            iter = -1;
        }
        iter++;
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
        BufferedImage image2 = matToBufferedImage(micro2);
        icon1.setImage(image1);
        icon2.setImage(image2);
        microVideo.revalidate();
        microVideo.repaint();
    }

    private static BufferedImage matToBufferedImage(Mat mat) {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        return converter.convert(new OpenCVFrameConverter.ToMat().convert(mat));
    }
}