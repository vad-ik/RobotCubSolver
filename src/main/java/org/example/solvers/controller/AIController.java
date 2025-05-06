package org.example.solvers.controller;

import org.example.Main;
import org.example.serialPort.Radio;
import org.example.solvers.AI.DeepCubeSolver;
import org.example.solvers.AI.TransformToAI;
import org.example.solvers.solverLayer.Cub;

public class AIController implements Solver {


    public void solve(Cub cub, Radio radio, Boolean radioConnected) {

        TransformToAI transformer = new TransformToAI();
        String state = transformer.transform(cub);
        String ans = DeepCubeSolver.send(state);

        assert ans != null;
        ans = (ans.split("\\["))[1].split("]")[0];

        String path = rotateAI(ans,cub);
        printPath(path,cub);
        if (radioConnected) {
            radio.writeString(cub.solver.toString());
        }
    }
    static void printPath(String path,Cub cub) {
        Main.wayBack = (new StringBuilder(path).reverse().toString())
                .replaceAll("r", "rrr").replaceAll("l", "lll")
                .replaceAll("u", "uuu").replaceAll("d", "ddd")
                .replaceAll("f", "fff").replaceAll("b", "bbb")

                .replaceAll("rrrr", "").replaceAll("llll", "")
                .replaceAll("uuuu", "").replaceAll("dddd", "")
                .replaceAll("ffff", "").replaceAll("bbbb", "");

        path = path.replaceAll("rrr", "r`").replaceAll("lll", "l`")
                .replaceAll("uuu", "u`").replaceAll("ddd", "d`")
                .replaceAll("fff", "f`").replaceAll("bbb", "b`");
        cub.solver=new StringBuilder(path);
        System.out.println("путь для решения " + path);
        System.out.println("обратный путь " +
                Main.wayBack.replaceAll("rrr", "r`").replaceAll("lll", "l`")
                        .replaceAll("uuu", "u`").replaceAll("ddd", "d`")
                        .replaceAll("fff", "f`").replaceAll("bbb", "b`"));
    }

    String rotateAI(String str,Cub cub) {
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
    public String getName(){
        return "deepcube";
    }
}
