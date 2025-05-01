package org.example.solver.layer;

import org.example.solver.Cub;
import org.example.solver.Side;

public class CubSolver3layer {

    public static final int upColor = Side.Color.white.ordinal();

    public void collectThirdLayer(Cub cub) {
        solveCross(cub);
        solveUp(cub);
        solveRibs(cub);
        solveAngles(cub);
        //  compose(cub);
    }

    void compose(Cub cub) {
        while (cub.sides[Cub.SideNumber.front.ordinal()].cell[2] != cub.sides[Cub.SideNumber.front.ordinal()].cell[5]) {
            cub.u();
        }
        while (cub.sides[Cub.SideNumber.down.ordinal()].cell[2] != cub.sides[Cub.SideNumber.down.ordinal()].cell[5]) {
            cub.d();
        }
    }

    void solveAngles(Cub cub) {
        int i = 0;
        while (!check3leer(cub)) {
            i++;
            if (i > 100) {
                System.out.println("ERROR");
                System.out.println(cub);
            }
            boolean finish = false;
            for (int rot = 0; rot < 4 && !finish; rot++) {
                if (cub.sides[handPosition(rot, 2)].cell[3] == cub.sides[handPosition(rot, 2)].cell[2] &&
                        cub.sides[handPosition(rot, 3)].cell[1] == cub.sides[handPosition(rot, 3)].cell[2]
                ) {

                    permutationOfAngles(cub, rot + 2);

                    if (!check3leer(cub)) {
                        permutationOfAngles(cub, rot + 2);
                    }
                    finish = true;
                }
            }
            if (!finish) {
                permutationOfAngles(cub, 0);
            }
        }
    }

    boolean check3leer(Cub cub) {
        boolean ans = true;
        for (int i = 1; i < 5; i++) {
            ans = ans && check(cub.sides[i]);
        }
        return ans;
    }

    boolean check(Side side) {
        return side.cell[1] == side.cell[2] && side.cell[2] == side.cell[3];
    }

    void permutationOfAngles(Cub cub, int rot) {
        hendRotate(cub, rot);
        hendRotate(cub, rot);
        cub.d();
        hendRotate(cub, rot);
        hendRotate(cub, rot);
        cub.dI();
        hendRotate(cub, 3 + rot);
        hendRotate(cub, 3 + rot);
        hendRotate(cub, rot);
        hendRotate(cub, rot);
        cub.u();
        cub.u();
        cub.u();
        hendRotate(cub, rot);
        hendRotate(cub, rot);
        cub.u();
        hendRotate(cub, 3 + rot);
        hendRotate(cub, 3 + rot);
    }

    void solveRibs(Cub cub) {

        boolean finidh = false;
        for (int rot = 0; rot < 4 && !finidh; rot++) {
            for (int i = 0; i < 4 && !finidh; i++) {
                if (cub.sides[handPosition(rot, 1)].cell[2] == cub.sides[handPosition(rot, 1)].cell[5] &&
                        cub.sides[handPosition(rot, 4)].cell[2] == cub.sides[handPosition(rot, 4)].cell[5] &&
                        cub.sides[handPosition(rot, 2)].cell[2] == cub.sides[handPosition(rot, 2)].cell[5] &&
                        cub.sides[handPosition(rot, 3)].cell[2] == cub.sides[handPosition(rot, 3)].cell[5]) {

                    finidh = true;
                    break;
                }
                if (cub.sides[handPosition(rot, 1)].cell[2] == cub.sides[handPosition(rot, 1)].cell[5] &&
                        cub.sides[handPosition(rot, 4)].cell[2] == cub.sides[handPosition(rot, 4)].cell[5]) {

                    sixthStage(cub, rot);
                    finidh = true;
                    break;
                }
                if (cub.sides[handPosition(rot, 2)].cell[2] == cub.sides[handPosition(rot, 2)].cell[5] &&
                        cub.sides[handPosition(rot, 4)].cell[2] == cub.sides[handPosition(rot, 4)].cell[5]) {

                    sixthStage(cub, rot);
                    cub.u();
                    cub.u();
                    sixthStage(cub, rot);
                    cub.uI();
                    finidh = true;
                    break;
                }
                cub.u();
            }
        }
    }

    void sixthStage(Cub cub, int rotation) {
        hendRotate(cub, rotation);

        cub.u();
        hendRotate(cub, rotation);
        hendRotate(cub, rotation);
        hendRotate(cub, rotation);
        hendRotate(cub, rotation + 3);
        hendRotate(cub, rotation + 3);
        hendRotate(cub, rotation + 3);
        hendRotate(cub, rotation);
        cub.u();
        hendRotate(cub, rotation);
        hendRotate(cub, rotation);
        hendRotate(cub, rotation);
        cub.uI();
        hendRotate(cub, rotation);
        hendRotate(cub, rotation);
        hendRotate(cub, rotation);
        hendRotate(cub, rotation + 3);
        hendRotate(cub, rotation);
        hendRotate(cub, rotation);
        cub.uI();
        hendRotate(cub, rotation);
        hendRotate(cub, rotation);
        hendRotate(cub, rotation);
        cub.uI();
    }


    void solveUp(Cub cub) {
        for (int i = 0; i < 4; i++) {

            //добавить проверку на сломанный кубик
            while (cub.sides[Cub.SideNumber.up.ordinal()].cell[3] != upColor) {
                downPifPaf(cub);
            }
            cub.u();
        }
    }

    void downPifPaf(Cub cub) {
        cub.d();
        cub.r();
        cub.dI();
        cub.rI();
    }

    void solveCross(Cub cub) {

        if (!(cub.sides[Cub.SideNumber.up.ordinal()].cell[2] == upColor &&
                cub.sides[Cub.SideNumber.up.ordinal()].cell[4] == upColor &&
                cub.sides[Cub.SideNumber.up.ordinal()].cell[6] == upColor &&
                cub.sides[Cub.SideNumber.up.ordinal()].cell[8] == upColor
        )) {
            if (cub.sides[Cub.SideNumber.up.ordinal()].cell[2] == upColor &&
                    cub.sides[Cub.SideNumber.up.ordinal()].cell[8] == upColor) {
                cub.u();
            }
            if (cub.sides[Cub.SideNumber.up.ordinal()].cell[4] == upColor &&
                    cub.sides[Cub.SideNumber.up.ordinal()].cell[6] == upColor) {
                stick(cub);
            } else if (cub.sides[Cub.SideNumber.up.ordinal()].cell[2] != upColor &&
                    cub.sides[Cub.SideNumber.up.ordinal()].cell[4] != upColor &&
                    cub.sides[Cub.SideNumber.up.ordinal()].cell[6] != upColor &&
                    cub.sides[Cub.SideNumber.up.ordinal()].cell[8] != upColor) {
                stick(cub);
                cub.u();
                cub.u();
                angle(cub);
            } else {
                while (cub.sides[Cub.SideNumber.up.ordinal()].cell[2] != upColor ||
                        cub.sides[Cub.SideNumber.up.ordinal()].cell[4] != upColor) {
                    cub.u();
                }
                angle(cub);
            }
        }
    }

    void stick(Cub cub) {
        cub.f();
        pifPaf(cub);
        cub.fI();
    }

    void angle(Cub cub) {

        cub.f();
        pifPaf(cub);
        pifPaf(cub);
        cub.fI();
    }

    void pifPaf(Cub cub) {
        cub.r();
        cub.u();
        cub.rI();
        cub.uI();
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

    void hendRotate(Cub cub, int comand) {
        switch (comand % 4) {
            case 0:
                cub.r();
                break;
            case 1:
                cub.b();
                break;
            case 2:
                cub.l();
                break;
            case 3:
                cub.f();
                break;
        }
    }
}
