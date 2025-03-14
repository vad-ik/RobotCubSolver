package org.example;

import org.example.AI.DeepCubeSolver;
import org.example.AI.TransformToAI;
import org.example.UI.MyException;
import org.example.UI.MyUI;
import org.example.camera.ColorScanner;
import org.example.serialPort.Radio;
import org.example.solver.Cub;
import org.example.solver.Side;
import org.example.solver.Solver;


import java.util.Random;

public class Main {
    //  System.out.println(cub.solver.toString());

    public static ColorScanner scanner;
    public static Radio radio;
    private static String comPort = "COM52";
    private static int freq = 9600;

    public static Cub cub;

    public static void main(String[] args) {


        cub = new Cub();
        MyUI ui = new MyUI();


//        solveCub();
//        solveAI();
    }

    public static void solveAI() throws Radio.BadRotationExeption {

        TransformToAI transformer = new TransformToAI();
        String state = transformer.transform(cub);
        String ans = DeepCubeSolver.send(state);
        ans = (ans.split("\\["))[1].split("]")[0];

        String path = rotateAI(ans);
        radio = new Radio(comPort, freq);
        radio.writeString(cub.solver.toString());


        radio.close();
    }

    static String rotateAI(String str) {
        cub.solver = new StringBuilder();
        String[] step = str.split(", ");

        for (int i = 0; i < step.length; i++) {
            int num;

            String[] nowStep = step[i].split("_");
            if (nowStep[1].equals("1\"")) {
                num = 1;
            } else {
                num = 3;
            }
            switch (nowStep[0]) {
                case "\"U":

                    for (int j = 0; j < num; j++) {
                        cub.u();

                    }
                    break;
                case "\"F":
                    for (int j = 0; j < num; j++) {
                        cub.f();
                    }
                    break;
                case "\"L":
                    for (int j = 0; j < num; j++) {
                        cub.l();
                    }
                    break;
                case "\"R":
                    for (int j = 0; j < num; j++) {
                        cub.r();
                    }
                    break;
                case "\"D":
                    for (int j = 0; j < num; j++) {
                        cub.d();
                    }
                    break;
                case "\"B":
                    for (int j = 0; j < num; j++) {
                        cub.b();
                    }
                    break;

            }
        }
        return cub.solver.toString();
    }

    public static void solveCub() throws Radio.BadRotationExeption {
        Solver solver = new Solver();
//        Cub cub = new Cub();
//
//        cubСonfuse(cub);
        solver.solve(cub);
        chesk(cub);


        radio.writeString(cub.solver.toString());


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