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
        //solvers.add(new LayerController());
        //solvers.add(new AIController());
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
          // System.out.println(i);
        }
        for (int j = 0; j <solvers.size() ; j++) {

            System.out.println();
            System.out.println(solvers.get(j).getName());
            System.out.println("среднее время: "+time[j]*1.0/numTest);
            System.out.println("среднее количество шагов: "+step[j]*1.0/numTest);
        }
    }




}
/*
послойная сборка
среднее время: 840820.0
среднее количество шагов: 231.6

deepcube
среднее время: 2.820797154E10
среднее количество шагов: 66.4
////////////////////////////////////
послойная сборка
среднее время: 0,84082 мс
среднее количество шагов: 210.6

deepcube
среднее время: 20 секунд
среднее количество шагов: 66.4




послойная сборка
среднее время: 0,58751 мс
среднее количество шагов: 206.1

deepcube
среднее время: 18 секунд
среднее количество шагов: 30.3

Коцемба
среднее время: 2.05579931E7
среднее количество шагов: 77.081

 */