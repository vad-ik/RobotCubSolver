import org.example.Main;
import org.example.serialPort.Radio;
import org.example.solvers.controller.AIController;
import org.example.solvers.controller.KocembaController;
import org.example.solvers.controller.LayerController;
import org.example.solvers.controller.Solver;
import org.example.solvers.solverLayer.Cub;
import org.example.solvers.solverLayer.Side;

import java.util.ArrayList;
import java.util.Random;

public class SpeedStepTest {
    public static void main(String[] args) {
        Main.cub=new Cub();
        Cub cub=Main.cub;
        int numTest=1000;

        ArrayList<Solver> solvers=new ArrayList<>();
        solvers.add(new LayerController());
        //solvers.add(new AIController()); // ДОЛГО
        solvers.add(new KocembaController());


        long[] time=new long[solvers.size()];
        int[] step=new int[solvers.size()];


        for (int i = 0; i <numTest ; i++) {

            for (int j = 0; j <solvers.size() ; j++) {
                Utils.cubConfuse(cub);
                long startTime=System.nanoTime();
                Main.startSolver(solvers.get(j));
                time[j]+=System.nanoTime()-startTime;
                step[j]+=cub.solver.toString().replaceAll("`","").replaceAll("'", "").length();

                Utils.chesk(cub);
            }
        }
        for (int j = 0; j <solvers.size() ; j++) {

            System.out.println();
            System.out.println(solvers.get(j).getName());
            System.out.println("среднее время: "+time[j]*1.0/numTest);
            System.out.println("среднее количество шагов: "+step[j]*1.0/numTest);
        }
    }




}


