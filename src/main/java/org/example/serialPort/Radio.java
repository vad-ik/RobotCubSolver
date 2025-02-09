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
    public void writeString(String string){
        arduino.serialWrite(string);
    }
    public void close(){
        arduino.closeConnection();
    }
}
