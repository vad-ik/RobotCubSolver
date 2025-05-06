import org.example.solvers.solverLayer.Cub;
import org.example.solvers.solverLayer.Side;

import java.util.Random;

public class Utils {
    static void chesk(Cub cub) {
        for (int i = 0; i < 6; i++) {
            chesk(cub.sides[i]);
        }
    }

    static void chesk(Side side) {
        int col = side.cell[1];
        for (int i = 1; i < 10; i++) {
            if (side.cell[i] != col) {
                System.out.println("Allarm");
                throw new RuntimeException("кубик собран неправильно");
            }
        }
    }

    static void cubConfuse(Cub cub) {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {

            // System.out.println(cub.toString()+"\n\n");
            int a = random.nextInt(6);
            switch (a) {
                case 0:
                    cub.u();
                    break;
                case 1:
                    cub.d();
                    break;
                case 2:
                    cub.l();
                    break;
                case 3:
                    cub.r();
                    break;
                case 4:
                    cub.f();
                    break;
                case 5:
                    cub.b();
                    break;
            }
        }
        cub.solver = new StringBuilder();
    }
}
