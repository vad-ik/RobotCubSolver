package org.example.solvers.solverLayer;

import java.util.Random;

public class Side {
    Side(boolean rand){
        Random r = new Random();
        for(int i = 0; i < 10; i++){
            if(rand){
                cell[i] = r.nextInt(6);
            }else{
                cell[i] = i;
            }
        }
    }
    Side(int col){
        for (int i = 0; i < 10; i++) {
            cell[i] = col;
        }

    }
    Side(Side side){
        System.arraycopy(side.cell, 0, this.cell, 0, side.cell.length);
    }
    public enum Color {
        white,
        orange,
        green,
        red,
        blue,
        yellow,
        Non
    }

    public int[] cell = new int[10];//нумирация с еденицы, а то запутаюсь ниже раскройка кубика
    //      up
    // left front right back
    //      down

    //      123
    //      456
    //      789
    //  123 123 123 123
    //  456 456 456 456
    //  789 789 789 789
    //      123
    //      456
    //      789

   void rotation(){
       int temp = cell[1];
       cell[1] = cell[7];
       cell[7] = cell[9];
       cell[9] = cell[3];
       cell[3] = temp;

       temp = cell[2];
       cell[2] = cell[4];
       cell[4] = cell[8];
       cell[8] = cell[6];
       cell[6] = temp;
   }

}
