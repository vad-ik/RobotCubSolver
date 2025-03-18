package org.example.UI;

import org.example.Main;
import org.example.camera.ColorScanner;
import org.example.serialPort.Radio;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyUI {
    public JPanel err;

    public MyUI() {
        JFrame frame = new JFrame("My First GUI"); // Для окна нужна "рама" - Frame
        // стандартное поведение при закрытии окна - завершение приложения
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300); // размеры окна
        frame.setLocationRelativeTo(null); // окно - в центре экрана
        construct(frame);
        frame.setVisible(true); // Делаем окно видимым
    }

    void construct(JFrame frame) {
        JPanel generalPanel = new JPanel();
        generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.PAGE_AXIS));

        addControlButtons(generalPanel);

        JPanel mainPanel = new JPanel();
        addMainPanel(mainPanel);
        generalPanel.add(mainPanel);

        generalPanel.add(setting());

        err = new JPanel();
        generalPanel.add(err);
        frame.add(generalPanel);
    }

    JPanel setting() {
        JPanel settingPanel = new JPanel();
        JButton setting = new JButton("настроить камеру");
        setting.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingCamUI settingCamUI=new SettingCamUI();

                ImagePanel.nowPoint = Main.scanner.detector.setting.getPoint();
//                Main.scanner = new ColorScanner(false);

            }
        });
        settingPanel.add(setting);

        JButton colorTest = new JButton("проверить цвета");
        colorTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Main.scanner = new ColorScanner(false);
                Main.scanner.detector.getNextPhoto();
                Main.scanner.detector.nextPhoto(Main.cub, true);
            }
        });
        settingPanel.add(colorTest);


        return settingPanel;
    }

    void addControlButtons(JPanel mainPAnel) {
        JPanel panel = new JPanel();
        addBut(panel, "l");
        addBut(panel, "r");
        addBut(panel, "u");
        addBut(panel, "d");
        addBut(panel, "b");
        addBut(panel, "f");
        mainPAnel.add(panel);
    }

    void addMainPanel(JPanel frame) {
        JButton scanBut = new JButton("сканировать кубик"); // Экземпляр класса JButton
        scanBut.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.scanner = new ColorScanner(false);
                try {
                    Main.scanner.scan(Main.cub);
                } catch (Radio.BadRotationExeption ex) {
                    err.add(new JTextArea("не удалось повернуть"));
                }
            }
        });
        frame.add(scanBut);
        JButton startBut = new JButton("собрать кубик"); // Экземпляр класса JButton
        startBut.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    Main.solveAI();

            }
        });
        frame.add(startBut);

    }

    void addBut(JPanel frame, String name) {

        JButton newBut = new JButton(name); // Экземпляр класса JButton
        newBut.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    Main.radio.writeString(name);

            }
        });

        frame.add(newBut); // Добавляем кнопку на Frame
    }


}
