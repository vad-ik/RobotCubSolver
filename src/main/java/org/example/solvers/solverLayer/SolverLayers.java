package org.example.solvers.solverLayer;

import org.example.solvers.solverLayer.layer.CubSolver1layer;
import org.example.solvers.solverLayer.layer.CubSolver2layer;
import org.example.solvers.solverLayer.layer.CubSolver3layer;

public class SolverLayers {
    CubSolver1layer solver1 = new CubSolver1layer();
    CubSolver2layer solver2 = new CubSolver2layer();
    CubSolver3layer solver3 = new CubSolver3layer();

    public void solve(Cub cub) {

        solver1.collectFirstLayer(cub);
        solver2.collectSecondLayer(cub);
        solver3.collectThirdLayer(cub);
    }
}