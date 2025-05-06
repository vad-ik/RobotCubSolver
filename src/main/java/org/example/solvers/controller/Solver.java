package org.example.solvers.controller;

import org.example.serialPort.Radio;
import org.example.solvers.solverLayer.Cub;

public interface Solver {
    public void solve(Cub cub, Radio radio, Boolean radioConnected);
    public String getName();
}
