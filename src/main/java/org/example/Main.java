package org.example;

import org.example.AI.DeepCubeSolver;
import org.example.AI.TransformToAI;
import org.example.UI.MyUI;
import org.example.camera.ColorScanner;
import org.example.camera.CreateDataset;
import org.example.serialPort.Radio;
import org.example.solver.Cub;
import org.example.solver.Side;
import org.example.solver.Solver;

import java.util.Random;

public class Main {
    //  System.out.println(cub.solver.toString());

    public static ColorScanner scanner;
    public static Radio radio;
    public static String wayBack;
    public static CreateDataset dataset;
    public static Cub cub;

    public static void main(String[] args) {
        dataset = new CreateDataset();

        scanner = new ColorScanner(false);
        radio = new Radio();
        cub = new Cub();
        new MyUI();


//        for (int i = 0; i <1000 ; i++) {
//            cubConfuse(cub);
//            solveCub();
//            chesk(cub);
//        }
    }

    public static void solveAI() {

        TransformToAI transformer = new TransformToAI();
        String state = transformer.transform(cub);
        String ans = DeepCubeSolver.send(state);

        ans = (ans.split("\\["))[1].split("]")[0];

        String path = rotateAI(ans);
        printPath(path);

        radio.writeString(cub.solver.toString());
    }

    static void printPath(String path) {
        wayBack = (new StringBuilder(path).reverse().toString())
                .replaceAll("r", "rrr").replaceAll("l", "lll")
                .replaceAll("u", "uuu").replaceAll("d", "ddd")
                .replaceAll("f", "fff").replaceAll("b", "bbb")

                .replaceAll("rrrr", "").replaceAll("llll", "")
                .replaceAll("uuuu", "").replaceAll("dddd", "")
                .replaceAll("ffff", "").replaceAll("bbbb", "");

        path = path.replaceAll("rrr", "r`").replaceAll("lll", "l`")
                .replaceAll("uuu", "u`").replaceAll("ddd", "d`")
                .replaceAll("fff", "f`").replaceAll("bbb", "b`");
        System.out.println("путь для решения " + path);
        System.out.println("обратный путь " +
                wayBack.replaceAll("rrr", "r`").replaceAll("lll", "l`")
                        .replaceAll("uuu", "u`").replaceAll("ddd", "d`")
                        .replaceAll("fff", "f`").replaceAll("bbb", "b`"));
    }

    static String rotateAI(String str) {
        cub.solver = new StringBuilder();
        String[] step = str.split(", ");

        for (String string : step) {
            int num;

            String[] nowStep = string.split("_");
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

    public static void solveCub() {
        Solver solver = new Solver();
//        Cub cub = new Cub();
//
//        cubСonfuse(cub);
        solver.solve(cub);
        chesk(cub);

        System.out.println(cub.solver.toString());
        radio.writeString(cub.solver.toString());
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

    static void cubConfuse(Cub cub) {
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