package org.example.camera;

import org.example.Main;
import org.example.solver.Cub;

public class ColorScanner {
   public RubiksCubeDetection detector;


    public ColorScanner(boolean debug){
        detector=new RubiksCubeDetection(debug);

    }
    public void scan(Cub cub)  {//тестовый набор
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

        System.out.println(cub.toString2());
    }
    void sendToArduino(Cub cub)  {
        Main.radio.writeString(cub.solver.toString());
        cub.solver=new StringBuilder();

    }

    public static void main(String[] args) {
        ColorScanner scanner=new ColorScanner(false);

        Cub cub = new Cub(6);
        System.out.println(cub.toString2());

            scanner.scan(cub);


        System.out.println(cub.toString2());

    }
}
