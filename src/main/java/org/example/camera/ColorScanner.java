package org.example.camera;

import org.bytedeco.opencv.global.opencv_highgui;
import org.example.Main;
import org.example.solver.Cub;

public class ColorScanner {
    RubiksCubeDetection detector;
    public ColorScanner(){
        detector=new RubiksCubeDetection(false);

    }
    void scan(Cub cub){
        detector.nextPhoto(cub, false);
        cub.r();
        cub.r();
        cub.u();
        cub.u();
        cub.d();
        cub.d();
        sendToArduino(cub);
        detector.nextPhoto(cub, false);
        cub.b();
        cub.l();
        cub.r();
        cub.r();
        cub.uI();
        sendToArduino(cub);
        detector.nextPhoto(cub, false);
        cub.b();
        cub.b();
        cub.r();
        cub.f();
        sendToArduino(cub);
        detector.nextPhoto(cub, false);
        cub.u();
        cub.u();
        cub.d();
        cub.d();
        cub.f();
        sendToArduino(cub);
        detector.nextPhoto(cub, false);
        cub.f();
        cub.r();
        cub.r();
        cub.f();
        sendToArduino(cub);
        detector.nextPhoto(cub, false);

        cub.rI();
        cub.u();
        sendToArduino(cub);
        detector.nextPhoto(cub, false);

        cub.d();
        cub.fI();
        cub.rI();
        sendToArduino(cub);
        detector.nextPhoto(cub, false);


    }
    void sendToArduino(Cub cub){
        Main.radio.writeString(cub.solver.toString());
        cub.solver=new StringBuilder();
        //////////////////////////////////////////////////////////////////////подождать либо слушать ответ
    }

    public static void main(String[] args) {
        ColorScanner scanner=new ColorScanner();

        Cub cub = new Cub(6);
        System.out.println(cub.toString2());

        scanner.scan(cub);

        System.out.println(cub.toString2());

    }
}
