package org.example.UI;

import org.example.Main;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class SpeedSettingsUI extends JFrame {
    public SpeedSettingsUI() throws HeadlessException {
        setTitle("Настройка скорости");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 150); // размеры окна

        setLocationRelativeTo(null); // окно - в центре экрана
        configure();
        setVisible(true); // Делаем окно видимым
    }

    private void configure() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));


        panel.add(addLayer(1000, "задержка вращения", 's'));
        panel.add(addLayer(100, "задержка между поворотом", 'r'));
        add(panel);
        //todo сохранять в настройки
    }

    private JPanel addLayer(int defaultValue, String text, char signal) {
        JPanel panel = new JPanel();

        JLabel description = new JLabel(text);
        panel.add(description);

        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);  // Убирает пробелы между разрядами (1 000 → 1000)
        JFormattedTextField turnDelay = new JFormattedTextField(format);

        turnDelay.setValue(defaultValue);
        turnDelay.addPropertyChangeListener("value", e -> {
            Number value = (Number) turnDelay.getValue();
            if (value != null && value.intValue() < 0) {
                turnDelay.setValue(0);  // Автоматически заменяем отрицательные на 0
            }
        });
        panel.add(turnDelay);

        JButton send = new JButton("отправить");
        send.addActionListener(e -> Main.radio.writeString("" + signal + turnDelay.getText()));
        panel.add(send);
        return panel;
    }
}
