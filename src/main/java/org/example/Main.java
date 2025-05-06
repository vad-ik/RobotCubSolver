package org.example;

import org.example.solvers.AI.DeepCubeSolver;
import org.example.solvers.AI.TransformToAI;
import org.example.UI.MyUI;
import org.example.camera.ColorScanner;
import org.example.camera.CreateDataset;
import org.example.serialPort.Radio;
import org.example.solvers.controller.Solver;
import org.example.solvers.solverLayer.Cub;
import org.example.solvers.solverLayer.Side;
import org.example.solvers.solverLayer.SolverLayers;

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



    }
    public static void startSolver(Solver solver){
        if (radio==null){
            solver.solve(cub,null,false);
        }else {
            solver.solve(cub, radio, radio.isActive());
        }
    }




}