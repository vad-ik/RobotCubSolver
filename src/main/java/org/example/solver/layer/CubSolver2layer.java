package org.example.solver.layer;

import org.example.solver.Cub;
import org.example.solver.Side;

import java.util.ArrayList;

public class CubSolver2layer {
    public void collectSecondLayer(Cub cub) {
        while (!check2leer(cub)) {

            boolean upLost = true;
            for (int ret = 0; ret < 4; ret++) {
                if (cub.sides[Cub.SideNumber.up.ordinal()].cell[handCell(ret, 8)] != CubSolver3layer.upColor &&
                        cub.sides[handPosition(ret, 2)].cell[2] != CubSolver3layer.upColor) {
                    upLost = false;

                    ArrayList<Integer> color = new ArrayList<>();
                    color.add(cub.sides[handPosition(ret, 2)].cell[2]);
                    color.add(cub.sides[Cub.SideNumber.up.ordinal()].cell[handCell(ret, 8)]);
                    int j = 0;
                    while (!(color.contains(cub.sides[handPosition(ret + j, 2)].cell[5]) &&
                            color.contains(cub.sides[handPosition(ret + j, 3)].cell[5]))) {
                        cub.u();
                        j += 3;
                    }
                    if (cub.sides[handPosition(ret + j, 2)].cell[2] == cub.sides[handPosition(ret + j, 2)].cell[5]) {
                        setRight((ret + j) % 4, cub);
                    } else {
                        setLeft((ret + j) % 4, cub);
                    }


                    break;
                }
            }
            if (upLost) {
                // break;
                for (int i = 0; i < 4; i++) {
                    if (cub.sides[handPosition(i, 2)].cell[6] != CubSolver3layer.upColor &&
                            cub.sides[handPosition(i, 3)].cell[4] != CubSolver3layer.upColor) {
                        setRight(i, cub);
                    }
                }
            }
        }
    }

    int handCell(int rotation, int cell) {
        for (int i = 0; i < rotation; i++) {
            switch (cell) {
                case 2:
                    cell = 4;
                    break;
                case 6:
                    cell = 2;
                    break;
                case 8:
                    cell = 6;
                    break;
                case 4:
                    cell = 8;
                    break;
                case 1:
                    cell = 7;
                    break;
                case 7:
                    cell = 9;
                    break;
                case 9:
                    cell = 3;
                    break;
                case 3:
                    cell = 1;
                    break;
            }
        }
        return cell;
    }

    int handPosition(int rotation, int side) {
        int i = side + rotation;
        while (i >= 5) {
            i -= 4;
        }
        return i;
    }

    boolean check2leer(Cub cub) {
        boolean ans = true;
        for (int i = 1; i < 5; i++) {
            ans = ans && check(cub.sides[i]);
        }
        return ans;
    }

    boolean check(Side side) {
        return side.cell[4] == side.cell[5] && side.cell[5] == side.cell[6];
    }

    void setLeft(int rotation, Cub cub) {
        cub.u();
        cub.u();
        leftPifPaf((rotation + 1) % 4, cub);
        pifPaf(rotation, cub);
    }

    void setRight(int rotation, Cub cub) {
        cub.u();
        pifPaf(rotation, cub);
        leftPifPaf((rotation + 1) % 4, cub);
    }

    void pifPaf(int rotation, Cub cub) {
        switch (rotation) {
            case 0:
                cub.r();
                cub.u();
                cub.rI();
                cub.uI();
                break;
            case 1:
                cub.b();
                cub.u();
                cub.bI();
                cub.uI();
                break;
            case 2:
                cub.l();
                cub.u();
                cub.lI();
                cub.uI();
                break;
            case 3:
                cub.f();
                cub.u();
                cub.fI();
                cub.uI();
                break;
        }
    }

    void leftPifPaf(int rotation, Cub cub) {
        switch (rotation) {
            case 0:
                cub.lI();
                cub.uI();
                cub.l();
                cub.u();
                break;
            case 1:
                cub.fI();
                cub.uI();
                cub.f();
                cub.u();
                break;
            case 2:
                cub.rI();
                cub.uI();
                cub.r();
                cub.u();
                break;
            case 3:
                cub.bI();
                cub.uI();
                cub.b();
                cub.u();
                break;
        }
    }
}
