package org.example;

import org.example.UI.MyUI;
import org.example.serialPort.Radio;
import org.example.solver.Cub;
import org.example.solver.Side;
import org.example.solver.Solver;


import java.util.Random;

public class Main {
    //  System.out.println(cub.solver.toString());

//    public static Radio radio;
    public static Radio radio = new Radio("COM52", 9600);

    public static void main(String[] args) {

        MyUI ui=new MyUI();
//        solveCub();
    }
    static void solveCub(){
        Solver solver = new Solver();
        Cub cub = new Cub();

        cubСonfuse(cub);
        solver.solve(cub);
        chesk(cub);


        try {
            radio.writeString(cub.solver.toString());
        } catch (Radio.BadRotationExeption e) {
            System.out.println(e);
        }


        radio.close();
    }

    static void chesk(Cub cub) {
        for (int i = 0; i < 6; i++) {
            chesk(cub.sides[i]);
        }
    }

    static void chesk(Side side) {
        int col = side.cell[1];
        for (int i = 1; i < 10; i++) {
            if (side.cell[i] != col) {
                System.out.println("Allarm");

            }
        }
    }

    static void cubСonfuse(Cub cub) {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {

            // System.out.println(cub.toString()+"\n\n");
            int a = random.nextInt(6);
            switch (a) {
                case 0:
                    cub.u();
                    break;
                case 1:
                    cub.d();
                    break;
                case 2:
                    cub.l();
                    break;
                case 3:
                    cub.r();
                    break;
                case 4:
                    cub.f();
                    break;
                case 5:
                    cub.b();
                    break;
            }

        }
        cub.solver = new StringBuilder();
    }

}