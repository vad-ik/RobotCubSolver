package org.example.solvers.controller;

import org.example.serialPort.Radio;
import org.example.solvers.solverLayer.Cub;
import org.example.utils.Utils;

public class TestController implements Solver {
    @Override
    public void solve(Cub cub, Radio radio, Boolean radioConnected) {
        System.out.println("start confuse");
        int step=5;
        Utils.cubConfuse(cub, step);
        if (radioConnected) {
            radio.writeString(cub.solver.toString());
        } else {
            System.out.println(cub.solver.toString());
        }
        cub.solver = new StringBuilder();
        try {
            Thread.sleep(step*1800);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("start solver");
        new KocembaController().solve(cub, radio, radio.isActive());
        cub.solver = new StringBuilder();

    }

    @Override
    public String getName() {
        return "Test";
    }
}
