package org.example.camera;

import org.example.Main;
import org.example.solver.Cub;
import org.example.solver.Side;

public class ColorScanner {
    public RubiksCubeDetection detector;


    public ColorScanner(boolean debug) {
        detector = new RubiksCubeDetection(debug);
    }
    public void save(){
        detector.save();
    }
    public void scan(Cub cub) {//тестовый набор
        nextPhoto(cub, false);
        save();
        cub.r();
        cub.r();
        cub.u();
        cub.u();
        cub.d();
        cub.d();
        sendToArduino(cub);
        nextPhoto(cub, false);
       save();
        cub.b();
        cub.l();
        cub.r();
        cub.r();
        cub.uI();
        sendToArduino(cub);
        nextPhoto(cub, false);
       save();
        cub.b();
        cub.b();
        cub.r();
        cub.f();
        sendToArduino(cub);
        nextPhoto(cub, false);
        save();
        cub.u();
        cub.u();
        cub.d();
        cub.d();
        cub.f();
        sendToArduino(cub);
        nextPhoto(cub, false);
       save();
        cub.f();
        cub.r();
        cub.r();
        cub.f();
        sendToArduino(cub);
        nextPhoto(cub, false);
       save();
        cub.rI();
        cub.u();
        sendToArduino(cub);
        nextPhoto(cub, false);
        save();
        cub.d();
        cub.fI();
        cub.rI();
        sendToArduino(cub);
        nextPhoto(cub, false);
        save();

        System.out.println(cub.toString2());
    }

    void sendToArduino(Cub cub) {
        Main.radio.writeString(cub.solver.toString());
        cub.solver = new StringBuilder();
        try {
            Thread.sleep(100*5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //todo ждать ответа
    }

    public static void main(String[] args) {
        ColorScanner scanner = new ColorScanner(false);

        Cub cub = new Cub(6);
        System.out.println(cub.toString2());
        scanner.scan(cub);
        System.out.println(cub.toString2());
    }
    public void nextPhoto(Cub cub, boolean b){
        detector.nextPhoto(cub,b);
//        cub.sides[Cub.SideNumber.front.ordinal()].cell[2] = Side.Color.orange.ordinal();
//        cub.sides[Cub.SideNumber.front.ordinal()].cell[3] = Side.Color.orange.ordinal();
//        cub.sides[Cub.SideNumber.front.ordinal()].cell[5] = Side.Color.orange.ordinal();
//        cub.sides[Cub.SideNumber.front.ordinal()].cell[6] = Side.Color.orange.ordinal();
//        cub.sides[Cub.SideNumber.front.ordinal()].cell[8] = Side.Color.orange.ordinal();
//        cub.sides[Cub.SideNumber.front.ordinal()].cell[9] = Side.Color.orange.ordinal();
//
//
//        cub.sides[Cub.SideNumber.right.ordinal()].cell[2] = Side.Color.orange.ordinal();
//        cub.sides[Cub.SideNumber.right.ordinal()].cell[1] = Side.Color.orange.ordinal();
//        cub.sides[Cub.SideNumber.right.ordinal()].cell[5] = Side.Color.orange.ordinal();
//        cub.sides[Cub.SideNumber.right.ordinal()].cell[4] = Side.Color.orange.ordinal();
//        cub.sides[Cub.SideNumber.right.ordinal()].cell[8] = Side.Color.orange.ordinal();
//        cub.sides[Cub.SideNumber.right.ordinal()].cell[7] = Side.Color.orange.ordinal();
    }
}
