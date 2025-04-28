package org.example.solver.layer;

import org.example.solver.Cub;
import org.example.solver.Side;

import java.util.ArrayList;

public class CubSolver1layer {
    public void collectFirstLayer(Cub cub) {


        pasteWhite(cub);
        solveCross(cub);
        solveAngles(cub);
    }

    boolean check1leer(Cub cub) {
        boolean ans = true;
        for (int i = 1; i < 5; i++) {
            ans = ans && check(cub.sides[i]);
        }
        return ans;
    }

    boolean check(Side side) {
        if (side.cell[7] == side.cell[8] && side.cell[8] == side.cell[9]) {
            return true;
        } else {
            return false;
        }
    }

    void solveAngles(Cub cub) {

        while (!((cub.sides[Cub.SideNumber.down.ordinal()].cell[1] == Side.Color.white.ordinal()) &&
                (cub.sides[Cub.SideNumber.down.ordinal()].cell[3] == Side.Color.white.ordinal()) &&
                (cub.sides[Cub.SideNumber.down.ordinal()].cell[7] == Side.Color.white.ordinal()) &&
                (cub.sides[Cub.SideNumber.down.ordinal()].cell[9] == Side.Color.white.ordinal()) &&
                check1leer(cub)
        )) {

            for (int i = 0; i < 4; i++) {
                if ((cub.sides[Cub.SideNumber.up.ordinal()].cell[handCell(i, 9)] == Side.Color.white.ordinal()) ||
                        (cub.sides[handPosition(i, 2)].cell[3] == Side.Color.white.ordinal()) ||
                        (cub.sides[handPosition(i, 3)].cell[1] == Side.Color.white.ordinal())) {
                    ArrayList<Integer> color = new ArrayList<>();
                    color.add(cub.sides[handPosition(i, 2)].cell[3]);
                    color.add(cub.sides[handPosition(i, 3)].cell[1]);
                    color.add(cub.sides[Cub.SideNumber.up.ordinal()].cell[handCell(i, 9)]);
                    int j = 0;

                    while (!(color.contains(cub.sides[handPosition(i + j, 2)].cell[5]) &&
                            color.contains(cub.sides[handPosition(i + j, 3)].cell[5]))) {

                        cub.u();
                        j += 3;
                        if (j == 30) {
                            int a = 0;
                            int b = 6;
                            b = b / a;
                        }
                    }

                    int k = 0;
                    while (!((cub.sides[Cub.SideNumber.down.ordinal()].cell[rotatePifPaf(i + j, 3)] == Side.Color.white.ordinal()) &&
                            (cub.sides[handPosition(i + j, 2)].cell[5] == cub.sides[handPosition(i + j, 2)].cell[9]) &&
                            (cub.sides[handPosition(i + j, 3)].cell[5] == cub.sides[handPosition(i + j, 3)].cell[7])) &&

                            k < 90) {
                        pifPaf((i + j) % 4, cub);
                        k++;
                    }

                    break;
                }
            }

            for (int i = 0; i < 4; i++) {
                if ((cub.sides[handPosition(i, 2)].cell[9] == Side.Color.white.ordinal()) ||
                        (cub.sides[handPosition(i, 3)].cell[7] == Side.Color.white.ordinal()) ||
                        (cub.sides[Cub.SideNumber.down.ordinal()].cell[handCell(i, 3)] == Side.Color.white.ordinal() &&
                                (cub.sides[handPosition(i, 2)].cell[9] != cub.sides[handPosition(i, 2)].cell[8] ||
                                        cub.sides[handPosition(i, 3)].cell[7] != cub.sides[handPosition(i, 3)].cell[8]))) {
                    pifPaf((i), cub);
                }
            }
        }
    }

    void solveCross(Cub cub) {
        while (!((cub.sides[Cub.SideNumber.down.ordinal()].cell[2] == Side.Color.white.ordinal()) &&
                (cub.sides[Cub.SideNumber.down.ordinal()].cell[4] == Side.Color.white.ordinal()) &&
                (cub.sides[Cub.SideNumber.down.ordinal()].cell[6] == Side.Color.white.ordinal()) &&
                (cub.sides[Cub.SideNumber.down.ordinal()].cell[8] == Side.Color.white.ordinal())
        )) {
            for (int rot = 0; rot < 4; rot++) {
                int i = 0;
                while (!((cub.sides[handPosition(rot, 2)].cell[2] == cub.sides[handPosition(rot, 2)].cell[5]) &&
                        cub.sides[Cub.SideNumber.up.ordinal()].cell[handCell(rot, 8)] == Side.Color.white.ordinal()) &&
                        i < 5) {
                    cub.u();
                    i++;
                }
                if (i != 5 && cub.sides[Cub.SideNumber.up.ordinal()].cell[handCell(rot, 8)] == Side.Color.white.ordinal()) {
                    handRotation(cub, rot, 9);
                    handRotation(cub, rot, 9);

                    break;
                }
            }
        }
    }

    void pasteWhite(Cub cub) {
        while (!((cub.sides[Cub.SideNumber.up.ordinal()].cell[2] == Side.Color.white.ordinal()) &&
                (cub.sides[Cub.SideNumber.up.ordinal()].cell[4] == Side.Color.white.ordinal()) &&
                (cub.sides[Cub.SideNumber.up.ordinal()].cell[6] == Side.Color.white.ordinal()) &&
                (cub.sides[Cub.SideNumber.up.ordinal()].cell[8] == Side.Color.white.ordinal())
        )) {


            for (int rot = 0; rot < 4; rot++) {
                if (cub.sides[handPosition(rot, 2)].cell[2] == Side.Color.white.ordinal()) {
                    handRotation(cub, rot, 9);
                }
                if (cub.sides[handPosition(rot, 2)].cell[8] == Side.Color.white.ordinal()) {
                    while (cub.sides[Cub.SideNumber.up.ordinal()].cell[handCell(rot, 8)] == Side.Color.white.ordinal()) {
                        cub.u();
                    }

                    handRotation(cub, rot, 9);
                }
                if (cub.sides[handPosition(rot, 2)].cell[4] == Side.Color.white.ordinal()) {
                    while (cub.sides[Cub.SideNumber.up.ordinal()].cell[handCell(rot, 4)] == Side.Color.white.ordinal()) {
                        cub.u();
                    }
                    handRotation(cub, rot, 1);
                }

                if (cub.sides[handPosition(rot, 2)].cell[6] == Side.Color.white.ordinal()) {
                    while (cub.sides[Cub.SideNumber.up.ordinal()].cell[handCell(rot, 6)] == Side.Color.white.ordinal()) {
                        cub.u();
                    }
                    handRotation(cub, rot, 2);
                }
            }

            for (int i = 2; i < 10; i += 2) {
                if (cub.sides[Cub.SideNumber.down.ordinal()].cell[i] == Side.Color.white.ordinal()) {

                    while (cub.sides[Cub.SideNumber.up.ordinal()].cell[upDownPosition(i)] == Side.Color.white.ordinal()) {
                        cub.u();
                    }

                    handRotation(cub, i / 2, 14);
                }
            }
        }
    }

    int upDownPosition(int cell) {
        switch (cell) {

            case 2:
                return 8;
            case 6:
                return 6;
            case 8:
                return 2;
            case 4:
                return 4;
        }
        return 0;
    }

    int handPosition(int rotation, int side) {
        int i = side + rotation;
        while (i >= 5) {
            i -= 4;
        }
        return i;
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

    int rotatePifPaf(int rotation, int cell) {
        for (int i = 0; i < rotation; i++) {
            switch (cell) {
                case 1:
                    cell = 3;
                    break;
                case 7:
                    cell = 1;
                    break;
                case 9:
                    cell = 7;
                    break;
                case 3:
                    cell = 9;
                    break;
            }
        }
        return cell;
    }

    void handRotation(Cub cub, int rotation, int side) {
        int i = side + rotation * 2;
        switch (i) {

            case 1:
                cub.l();
                cub.l();
                cub.l();
                break;
            case 2:
                cub.r();
                break;
            case 3:
                cub.f();
                cub.f();
                cub.f();
                break;
            case 4:
                cub.b();
                break;
            case 5:
                cub.rI();
                break;
            case 6:
                cub.l();
                break;
            case 7:
                cub.bI();
                break;
            case 8:
                cub.f();
                break;
            case 9:
                cub.f();
                break;
            case 11:
                cub.r();
                break;
            case 13:
                cub.b();
                break;
            case 15:
                cub.l();
                break;
            case 16:
                cub.f();
                cub.f();
                break;
            case 18:
                cub.l();
                cub.l();
                break;
            case 20:
                cub.r();
                cub.r();
                break;
            case 22:
                cub.d();
                cub.d();
                break;
        }
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
}
