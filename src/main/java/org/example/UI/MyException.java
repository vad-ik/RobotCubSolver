package org.example.UI;

import javax.swing.*;
import java.awt.*;

public class MyException {
    public MyException(String string) {
        JFrame frame = new JFrame("Ошибка"); // Для окна нужна "рама" - Frame
        // стандартное поведение при закрытии окна - завершение приложения
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 100); // размеры окна

        JPanel panel = new JPanel();
        JTextField text = new JTextField(string);

        // Делаем текстовое поле нередактируемым
        text.setEditable(false);
        panel.add(text);

        frame.add(panel, BorderLayout.CENTER);


        frame.setVisible(true); // Делаем окно видимым
    }
}
