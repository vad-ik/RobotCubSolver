package org.example.UI;

import com.fazecast.jSerialComm.SerialPort;
import org.example.Main;
import org.example.camera.SaveSettings;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ComPortSelectionUI extends JFrame {
    JComboBox<String> portComboBox;
    public ComPortSelectionUI()  {
        setTitle("выбор com порта ");
        setSize(300, 100);
//         Таймер для обновления изображения с камеры

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - this.getWidth())/2-300;
        int y = (screenSize.height - this.getHeight()) /2;
        setLocation(x, y);


        // Добавляем выпадающий список на форму

        portComboBox=comboBox();
        JPanel panel=new JPanel();
        panel.add(portComboBox);
        panel.add(butReload());
        panel.add(butOk());

        add(panel);

        setVisible(true);
    }
    JComboBox<String> comboBox(){
        SerialPort[] ports =SerialPort.getCommPorts();
        String[] portNames = new String[ports.length];

        // Заполняем массив именами портов
        for (int i = 0; i < ports.length; i++) {
            portNames[i] = ports[i].getSystemPortName();
        }

        // Создаем выпадающий список с именами портов
        return new JComboBox<>(portNames);
    }
    JButton butOk(){
        JButton ok=new JButton("Сохранить");
        ok.addActionListener(_ -> {
            Main.scanner.detector.setting.setComPort((String) portComboBox.getSelectedItem());
            try {
                SaveSettings.saveToFile(Main.scanner.detector.setting,"setting");
            } catch (IOException ex) {
                new MyException("не удалось сохранить настройки");
            }
        });
        return ok;
    }
    JButton butReload(){
        JButton reload=new JButton("Обновить");
        reload.addActionListener(_ -> {
            portComboBox=comboBox();
        });
        return reload;
    }

}
