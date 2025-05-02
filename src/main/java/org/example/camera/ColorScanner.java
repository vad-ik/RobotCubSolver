package org.example.camera;

import org.example.Main;
import org.example.solver.Cub;

public class ColorScanner {
    public RubiksCubeDetection detector;
    private final boolean debug=true;

    public ColorScanner(boolean debug) {
        detector = new RubiksCubeDetection(debug);
    }

    public void save() {
        if (debug){
        detector.save();
    }}

    public void scan(Cub cub) {//тестовый набор
        debugSout(cub);
        nextPhoto(cub, false);
        save();
        cub.r();debugSout(cub);
        cub.r();debugSout(cub);
        cub.u();debugSout(cub);
        cub.u();debugSout(cub);
        cub.d();debugSout(cub);
        cub.d();debugSout(cub);
        sendToArduino(cub);
        nextPhoto(cub, false);
        save();
        cub.b();debugSout(cub);
        cub.l();debugSout(cub);
        cub.r();debugSout(cub);
        cub.r();debugSout(cub);
        cub.uI();debugSout(cub);
        sendToArduino(cub);
        nextPhoto(cub, false);
        save();
        cub.b();debugSout(cub);
        cub.b();debugSout(cub);
        cub.r();debugSout(cub);
        cub.f();debugSout(cub);
        sendToArduino(cub);
        nextPhoto(cub, false);
        save();
        cub.u();debugSout(cub);
        cub.u();debugSout(cub);
        cub.d();debugSout(cub);
        cub.d();debugSout(cub);
        cub.f();debugSout(cub);
        sendToArduino(cub);
        nextPhoto(cub, false);
        save();
        cub.f();debugSout(cub);
        cub.r();debugSout(cub);
        cub.r();debugSout(cub);
        cub.f();debugSout(cub);
        sendToArduino(cub);
        nextPhoto(cub, false);
        save();
        cub.rI();debugSout(cub);
        cub.u();debugSout(cub);
        sendToArduino(cub);
        nextPhoto(cub, false);
        save();
        cub.d();debugSout(cub);
        cub.fI();debugSout(cub);
        cub.rI();debugSout(cub);
        sendToArduino(cub);
        nextPhoto(cub, false);
        save();

        System.out.println(cub.toString2());
    }

    private void debugSout(Cub cub){
        if (debug){

            System.out.println(cub.toString2());
        }
    }

    void sendToArduino(Cub cub) {
        Main.radio.writeString(cub.solver.toString());
        cub.solver = new StringBuilder();
        try {
            Thread.sleep(100 * 5);
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

    public void nextPhoto(Cub cub, boolean b) {
        detector.nextPhoto(cub, b);
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
