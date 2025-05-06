import org.example.Main;
import org.example.solvers.controller.KocembaController;
import org.example.solvers.controller.Solver;
import org.example.solvers.solverLayer.Cub;

import java.util.ArrayList;

public class AlgaritumTest {

    public static void main(String[] args) {
        Main.cub = new Cub();
        Cub cub = Main.cub;

        ArrayList<Solver> solvers = new ArrayList<>();
        //solvers.add(new LayerController());
        //solvers.add(new AIController());
        solvers.add(new KocembaController());


        long[] time = new long[solvers.size()];
        int[] step = new int[solvers.size()];


        for (int j = 0; j < solvers.size(); j++) {
            Utils.cubConfuse(cub);
            System.out.println(cub.toString2());
            long startTime = System.nanoTime();
            Main.startSolver(solvers.get(j));
            time[j] += System.nanoTime() - startTime;

            System.out.println(cub.toString2());
            System.out.println(cub.solver.toString().replaceAll("'", ""));
            step[j] += cub.solver.toString().replaceAll("'", "").length();

            Utils.chesk(cub);
        }

        for (int j = 0; j < solvers.size(); j++) {

            System.out.println();
            System.out.println(solvers.get(j).getName());
            System.out.println("среднее время: " + time[j]);
            System.out.println("среднее количество шагов: " + step[j]);
        }
    }
}


