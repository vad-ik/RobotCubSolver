package org.example.UI;

import org.example.Main;
import org.example.solvers.controller.*;
import org.example.solvers.controller.Solver;
import org.example.solvers.solverLayer.Cub;
import org.example.solvers.solverLayer.Side;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Scanner;

public class MyUI extends JFrame {
    public JPanel err;
    Scanner scanner = new Scanner(System.in);
    ImageIcon backgroundImage;


    public MyUI() {
        setTitle("кубикорубикоинатор");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600); // размеры окна


        setLocationRelativeTo(null); // окно - в центре экрана
        backgroundImage = new ImageIcon("src/main/resources/background.jpg");


        // Добавление панелей на JFrame

        construct();

        setVisible(true); // Делаем окно видимым
    }


    void construct() {
        JPanel generalPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        generalPanel.setOpaque(false);
        generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.PAGE_AXIS));

        addControlButtons(generalPanel);

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        addMainPanel(mainPanel);
        generalPanel.add(mainPanel);

        generalPanel.add(setting());

        err = new JPanel();
        err.setOpaque(false);
        generalPanel.add(err);


        add(generalPanel, BorderLayout.CENTER);
    }

    JPanel setting() {
        JPanel settingPanel = new JPanel();
        settingPanel.setOpaque(false);
        JButton setting = new JButton("настроить камеру");
        setting.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SettingCamUI();

                ImagePanel.nowPoint = Main.scanner.detector.setting.getPoint();
//                Main.scanner = new ColorScanner(false);

            }
        });
        settingPanel.add(setting);

        JButton colorTest = new JButton("проверить цвета(debug)");
        colorTest.addActionListener(_ -> {

            Main.scanner.detector.updateSetting();
            Main.scanner.detector.startCam();
            Main.scanner.detector.getNextPhoto();
            Main.scanner.detector.nextPhoto(Main.cub, true);
        });
        settingPanel.add(colorTest);

        JButton dataSetTest = new JButton("создать датасет(debug)");
        dataSetTest.addActionListener(_ -> {

            Main.scanner.detector.updateSetting();
            Main.scanner.detector.startCam();
            Main.scanner.detector.getNextPhoto();
            Main.dataset.addToDataset(Main.scanner.detector.image);
        });
        settingPanel.add(dataSetTest);

        JButton setCub = new JButton("задать кубик с консоли");
        setCub.addActionListener(_ -> setCubConsole());
        settingPanel.add(setCub);

        JButton getCub = new JButton("вывести кубик в консоль");
        getCub.addActionListener(_ -> System.out.println(Main.cub.toString2()));
        settingPanel.add(getCub);

        JButton confuse = new JButton("разобрать до предыдущего состояния");
        confuse.addActionListener(_ -> {
            //todo это сейчас только ии записывает, добавить послойную
            Main.radio.writeString(Main.wayBack);
        });
        settingPanel.add(confuse);

        JButton sendToArdu = new JButton("отправить строку с консоли на ардуино");
        sendToArdu.addActionListener(_ -> {

            System.out.println("ВВедите строку для отправки роботу");
            Main.radio.writeString(scanner.nextLine());
        });
        settingPanel.add(sendToArdu);

        JButton speedSettings = new JButton("Настроить задержки");
        speedSettings.addActionListener(_ -> {
            new SpeedSettingsUI();
        });
        settingPanel.add(speedSettings);


        return settingPanel;
    }

    void setCubConsole() {
        System.out.println("введите цвета с помошью команд \"w\",\"y\",\"r\",\"g\",\"o\",\"b\"");
        String input;

        for (int j = 0; j < Cub.SideNumber.values().length; j++) {


            System.out.println("введите грань: " + Cub.SideNumber.values()[j]);
            input = scanner.nextLine();
            while (input.length() < 9) {
                input = scanner.nextLine();
            }
            for (int i = 1; i < 10; i++) {
                int colorCel = -1;
                switch (input.charAt(i - 1)) {
                    case 'w' -> colorCel = Side.Color.white.ordinal();
                    case 'y' -> colorCel = Side.Color.yellow.ordinal();
                    case 'b' -> colorCel = Side.Color.blue.ordinal();
                    case 'r' -> colorCel = Side.Color.red.ordinal();
                    case 'g' -> colorCel = Side.Color.green.ordinal();
                    case 'o' -> colorCel = Side.Color.orange.ordinal();
                }
                Main.cub.sides[j].cell[i] = colorCel;
            }
        }
        System.out.println("кубик успешно записан");
    }

    void addControlButtons(JPanel mainPAnel) {
        JPanel panel = new JPanel();
        addBut(panel, "l");
        addBut(panel, "r");
        addBut(panel, "u");
        addBut(panel, "d");
        addBut(panel, "b");
        addBut(panel, "f");
        panel.setOpaque(false);
        mainPAnel.add(panel);
    }

    void addMainPanel(JPanel frame) {
        JButton scanBut = new JButton("сканировать кубик"); // Экземпляр класса JButton
        scanBut.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.scanner.detector.updateSetting();
                Main.scanner.detector.startCam();
                Main.scanner.scan(Main.cub);
            }
        });
        frame.add(scanBut);



        frame.add(addSolver( "собрать кубик с помощью AI",new AIController()));
        frame.add(addSolver( "собрать кубик по слоям",new LayerController()));
        frame.add(addSolver( "собрать кубик алгоритмом Коцембы",new KocembaController()));
    }
    private JButton addSolver(String name, Solver solver){
        JButton startBut = new JButton(name); // Экземпляр класса JButton
        startBut.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.startSolver(solver);
            }
        });
        return startBut;
    }

    void addBut(JPanel frame, String name) {

        JButton newBut = new JButton(name); // Экземпляр класса JButton
        newBut.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (name) {
                    case "u" -> Main.cub.u();
                    case "l" -> Main.cub.l();
                    case "f" -> Main.cub.f();
                    case "r" -> Main.cub.r();
                    case "b" -> Main.cub.b();
                    case "d" -> Main.cub.d();
                    default -> throw new IllegalArgumentException("Invalid input button rotate: " + name);
                }
                Main.cub.solver = new StringBuilder();
                Main.radio.writeString(name);
            }
        });
        frame.add(newBut); // Добавляем кнопку на Frame
    }
}
