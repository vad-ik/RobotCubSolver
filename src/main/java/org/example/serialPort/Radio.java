package org.example.serialPort;

import arduino.Arduino;

public class Radio {
    int freq;
    String port;
    Arduino arduino;
    public Radio(String port, int freq) {
        this.port = port;
        this.freq = freq;
        arduino = new Arduino(port, freq);
    }
    public void writeString(String string) throws BadRotationExeption {
        arduino.serialWrite(string);

        throw new BadRotationExeption("мотор не смог повернуть куб");
//        String ans=arduino.serialRead();
//        if (ans=="1"){
//            throw new BadRotationExeption("мотор не смог повернуть куб");
//        }
    }
    public void close(){
        arduino.closeConnection();
    }
    public class BadRotationExeption extends Exception {
        public BadRotationExeption(String message) {
            super(message);
        }
    }
}
