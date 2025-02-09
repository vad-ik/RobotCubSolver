package org.example.UI;

import org.example.Main;
import org.example.serialPort.Radio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MyUI {
   public JPanel err;
    public MyUI() {
        JFrame frame = new JFrame("My First GUI"); // Для окна нужна "рама" - Frame
        // стандартное поведение при закрытии окна - завершение приложения
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,300); // размеры окна
        frame.setLocationRelativeTo(null); // окно - в центре экрана
        JPanel panel=new JPanel();
        err=new JPanel();
        addBut(panel,"l");
        addBut(panel,"r");
        addBut(panel,"u");
        addBut(panel,"d");
        addBut(panel,"b");
        addBut(panel,"f");
        frame.add(panel, BorderLayout.CENTER);

        frame.add(err, BorderLayout.SOUTH);

        frame.setVisible(true); // Делаем окно видимым
    }
    void addBut(JPanel frame,String name){

        JButton newBut = new JButton(name); // Экземпляр класса JButton
        newBut.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Main.radio.writeString(name);
                } catch (Radio.BadRotationExeption ex) {

                    err.add(new JTextArea("не удалось повернуть"));
                }
            }
        });

        frame.add(newBut); // Добавляем кнопку на Frame
    }


}
