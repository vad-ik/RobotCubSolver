package org.example.solvers.controller;

import org.example.serialPort.Radio;
import org.example.solvers.solverLayer.Cub;
import org.example.solvers.solverLayer.SolverLayers;

public class LayerController implements Solver{
    public void solve(Cub cub, Radio radio, Boolean radioConnected) {
        SolverLayers solver = new SolverLayers();

        solver.solve(cub);
        fixString(cub);
        if (radioConnected) {
            radio.writeString(cub.solver.toString());
        }
    }
    private void fixString(Cub cub){
        String str=cub.solver.toString();
        str=str.replaceAll("uuuu","").replaceAll("dddd","")
                .replaceAll("bbbb","").replaceAll("ffff","")
                .replaceAll("llll","").replaceAll("rrrr","");

        str=str.replaceAll("rrr", "r`").replaceAll("lll", "l`")
                .replaceAll("uuu", "u`").replaceAll("ddd", "d`")
                .replaceAll("fff", "f`").replaceAll("bbb", "b`");
        cub.solver=new StringBuilder(str);
    }
    public String getName(){
        return "послойная сборка";
    }
}
