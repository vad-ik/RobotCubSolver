package org.example.serialPort;


import com.fazecast.jSerialComm.SerialPort;
import org.example.Main;
import org.example.UI.ComPortSelectionUI;
import org.example.UI.MyException;

import java.io.IOException;
import java.io.InputStream;


public class Radio {

    SerialPort sp = null;


    public Radio() {

        String myPort = Main.scanner.detector.setting.getComPort();
        SerialPort[] ports = SerialPort.getCommPorts();


        for (SerialPort port : ports) {
            System.out.println(port.getSystemPortName());
            if (myPort.equals(port.getSystemPortName())) {

                sp = port;
            }
        }
        if (sp != null) {
            sp.setComPortParameters(9600, 8, 1, 0);


            if (sp.openPort()) {
                System.out.println("Port is open :)");
            } else {
                System.out.println("Failed to open port :(");
                new MyException("Failed to open port");
            }
        } else {
            new ComPortSelectionUI();
        }

    }
    public boolean isActive(){
        if (sp==null){
            return false;
        }
        return sp.isOpen();
    }

    public void writeString(String string) {
        try {
            sp.getOutputStream().write((string).getBytes());
            sp.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void read() {
        InputStream inputStream = sp.getInputStream();
        try {
            StringBuilder response = new StringBuilder();
            long startTime = System.currentTimeMillis();
            int timeout = 10000;
            while (System.currentTimeMillis() - startTime < timeout) {
                if (sp.bytesAvailable() > 0) {
                    byte[] buffer = new byte[sp.bytesAvailable()];
                    int numRead = inputStream.read(buffer);
                    response.append(new String(buffer, 0, numRead));

                    // Если в ответе есть символ новой строки, считаем, что строка завершена
                    if (response.toString().contains("\n")) {
                        break;
                    }
                }
                Thread.sleep(10); // Небольшая задержка, чтобы не нагружать процессор
            }
            if (System.currentTimeMillis() - startTime < timeout) {
                System.out.println("Response from Arduino:\"" + response.toString().trim() + "\"");
            } else {
                System.out.println("превышен интервал ожидания ответа");
                new MyException("превышен интервал ожидания ответа");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
