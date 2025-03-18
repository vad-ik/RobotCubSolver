package org.example.serialPort;


import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.util.Arrays;

public class Radio {
    int freq;
    String port;

    public Radio(String myPort) {
        SerialPort[] ports = SerialPort.getCommPorts();

        SerialPort sp = null;
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
                return;
            }
            try {
                for (Integer i = 0; i < 5; ++i) {

                    sp.getOutputStream().write(i.byteValue());

                    sp.getOutputStream().flush();
                    System.out.println("Sent number: " + i);

                    byte[] newData = new byte[sp.bytesAvailable()];
                    int numRead = sp.readBytes(newData, newData.length);
                    System.out.println(numRead);
                    System.out.println(Arrays.toString(newData));
                    Thread.sleep(1000);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void writeString(String string) {
//        arduino.serialWrite(string);
//
////        throw new BadRotationExeption("мотор не смог повернуть куб");
//        String ans=arduino.serialRead();
//        System.out.println(ans);
//        if (ans=="1"){
//            throw new BadRotationExeption("мотор не смог повернуть куб");
//        }
    }

    public class BadRotationExeption extends Exception {
        public BadRotationExeption(String message) {
            super(message);
        }
    }
}
