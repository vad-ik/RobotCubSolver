package org.example.solvers.controller;

import org.example.Main;
import org.example.serialPort.Radio;
import org.example.solvers.kocemba.Search;
import org.example.solvers.solverLayer.Cub;
import org.example.solvers.solverLayer.Side;


import java.util.Arrays;

public class KocembaController implements Solver {


    private int maxDepth = 80, maxTime = 5;
    boolean useSeparator = false;
    boolean showString = false;


    private int getColor(Cub cub, int side, int cell) {
        if (showString) {
            System.out.print(side + " " + cell + " ");
        }
        switch (side) {//перестановка граней по порядку алгоритма коцембы
            case 1 -> side = 3;
            case 3 -> side = 5;
            case 4 -> side = 1;
            case 5 -> side = 4;
        }
        if (showString) {
            System.out.print(side + " " + cell + " ");
            System.out.println(Side.Color.values()[cub.sides[side].cell[cell + 1]]);
        }
        return cub.sides[side].cell[cell + 1];//нумерация с 0
    }

    @Override
    public void solve(Cub cub, Radio radio, Boolean radioConnected) {
        StringBuffer s = new StringBuffer(54);

        for (int i = 0; i < 54; i++)
            s.insert(i, 'B');// default initialization

        for (int i = 0; i < 6; i++)
            // read the 54 facelets
            for (int j = 0; j < 9; j++) {
                int col = getColor(cub, i, j);

                if (col == getColor(cub, 0, 4)) {
                    s.setCharAt(9 * i + j, 'U');
                } else if (col == getColor(cub, 1, 4)) {
                    s.setCharAt(9 * i + j, 'R');
                } else if (col == getColor(cub, 2, 4)) {
                    s.setCharAt(9 * i + j, 'F');
                } else if (col == getColor(cub, 3, 4)) {
                    s.setCharAt(9 * i + j, 'D');
                } else if (col == getColor(cub, 4, 4)) {
                    s.setCharAt(9 * i + j, 'L');
                } else if (col == getColor(cub, 5, 4)) {
                    s.setCharAt(9 * i + j, 'B');
                }
                if (showString) {
                    System.out.println();
                }
            }


        String cubeString = s.toString();
        if (showString) {
            System.out.println("Cube Definiton String: " + cubeString);
            System.out.println(stats(cubeString));
        }

        // ++++++++++++++++++++++++ Call Search.solution method from package org.kociemba.twophase ++++++++++++++++++++++++
        String result = Search.solution(cubeString, maxDepth, maxTime, useSeparator);
        if (showString) {
            System.out.println(result);
        }
        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) == '2') {
                var arr=result.toCharArray();
                arr[i] = result.charAt(i - 1);
                result=String.valueOf(arr);
            }
        }
        if (showString) {
            System.out.println(cub.solver.toString());
        }
        // +++++++++++++++++++ Replace the error messages with more meaningful ones in your language ++++++++++++++++++++++
        if (result.contains("Error")) {
            switch (result.charAt(result.length() - 1)) {
                case '1':
                    result = "There are not exactly nine facelets of each color!";
                    break;
                case '2':
                    result = "Not all 12 edges exist exactly once!";
                    break;
                case '3':
                    result = "Flip error: One edge has to be flipped!";
                    break;
                case '4':
                    result = "Not all 8 corners exist exactly once!";
                    break;
                case '5':
                    result = "Twist error: One corner has to be twisted!";
                    break;
                case '6':
                    result = "Parity error: Two corners or two edges have to be exchanged!";
                    break;
                case '7':
                    result = "No solution exists for the given maximum move number!";
                    break;
                case '8':
                    result = "Timeout, no solution found within given maximum time!";
                    break;
            }
            System.out.println(result);
        } else {
            execute(cub, result.replaceAll(" ", ""));
            if (radioConnected) {
                radio.writeString(cub.solver.toString());
            }}

        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    }

    private String stats(String string) {
        int[] num = new int[6];
        for (int i = 0; i < string.length(); i++) {


            switch (string.charAt(i)) {
                case 'U' -> num[0]++;
                case 'B' -> num[1]++;
                case 'L' -> num[2]++;
                case 'R' -> num[3]++;
                case 'F' -> num[4]++;
                case 'D' -> num[5]++;
            }
        }
        return Arrays.toString(num);
    }

    private void execute(Cub cub, String string) {


        string = string.replaceAll("U'", "UUU").replaceAll("B'", "BBB")
                .replaceAll("L'", "LLL").replaceAll("R'", "RRR")
                .replaceAll("F'", "FFF").replaceAll("D'", "DDD");
        for (int i = 0; i < string.length(); i++) {
            switch (string.charAt(i)) {
                case 'U' -> cub.u();
                case 'B' -> cub.b();
                case 'L' -> cub.l();
                case 'R' -> cub.r();
                case 'F' -> cub.f();
                case 'D' -> cub.d();
            }
        }
    }

    @Override
    public String getName() {
        return "Коцемба";
    }

}
