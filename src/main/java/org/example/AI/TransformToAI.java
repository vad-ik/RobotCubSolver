package org.example.AI;

import org.example.solver.Cub;
import org.example.solver.Side;

import java.util.*;
//    yellow,0
//    orange,1
//    blue,2
//    red,3
//    green,4
//    white,5

public class TransformToAI {

    // Структура для хранения данных
    private Map<List<Integer>, Map<Integer, Integer>> dataStorage = new HashMap<>();

    public TransformToAI() {
        this.addObject(Arrays.asList(Side.Color.yellow.ordinal()), Arrays.asList(13));
        this.addObject(Arrays.asList(Side.Color.orange.ordinal()), Arrays.asList(22));
        this.addObject(Arrays.asList(Side.Color.blue.ordinal()), Arrays.asList(40));
        this.addObject(Arrays.asList(Side.Color.red.ordinal()), Arrays.asList(31));
        this.addObject(Arrays.asList(Side.Color.green.ordinal()), Arrays.asList(49));
        this.addObject(Arrays.asList(Side.Color.white.ordinal()), Arrays.asList(4));


        this.addObject(Arrays.asList(Side.Color.white.ordinal(), Side.Color.orange.ordinal(), Side.Color.blue.ordinal()), Arrays.asList(2, 20, 44));
        this.addObject(Arrays.asList(Side.Color.white.ordinal(), Side.Color.orange.ordinal(), Side.Color.green.ordinal()), Arrays.asList(0, 26, 47));
        this.addObject(Arrays.asList(Side.Color.white.ordinal(), Side.Color.red.ordinal(), Side.Color.green.ordinal()), Arrays.asList(6, 29, 53));
        this.addObject(Arrays.asList(Side.Color.white.ordinal(), Side.Color.red.ordinal(), Side.Color.blue.ordinal()), Arrays.asList(8, 35, 38));


        this.addObject(Arrays.asList(Side.Color.white.ordinal(), Side.Color.green.ordinal()), Arrays.asList(3, 50));
        this.addObject(Arrays.asList(Side.Color.white.ordinal(), Side.Color.orange.ordinal()), Arrays.asList(1, 23));
        this.addObject(Arrays.asList(Side.Color.white.ordinal(), Side.Color.red.ordinal()), Arrays.asList(7, 32));
        this.addObject(Arrays.asList(Side.Color.white.ordinal(), Side.Color.blue.ordinal()), Arrays.asList(5, 41));


        this.addObject(Arrays.asList(Side.Color.yellow.ordinal(), Side.Color.orange.ordinal(), Side.Color.blue.ordinal()), Arrays.asList(9, 18, 42));
        this.addObject(Arrays.asList(Side.Color.yellow.ordinal(), Side.Color.orange.ordinal(), Side.Color.green.ordinal()), Arrays.asList(11, 24, 45));
        this.addObject(Arrays.asList(Side.Color.yellow.ordinal(), Side.Color.red.ordinal(), Side.Color.green.ordinal()), Arrays.asList(17, 27, 51));
        this.addObject(Arrays.asList(Side.Color.yellow.ordinal(), Side.Color.red.ordinal(), Side.Color.blue.ordinal()), Arrays.asList(15, 33, 36));


        this.addObject(Arrays.asList(Side.Color.yellow.ordinal(), Side.Color.green.ordinal()), Arrays.asList(14, 48));
        this.addObject(Arrays.asList(Side.Color.yellow.ordinal(), Side.Color.orange.ordinal()), Arrays.asList(10, 21));
        this.addObject(Arrays.asList(Side.Color.yellow.ordinal(), Side.Color.red.ordinal()), Arrays.asList(16, 30));
        this.addObject(Arrays.asList(Side.Color.yellow.ordinal(), Side.Color.blue.ordinal()), Arrays.asList(12, 39));

        this.addObject(Arrays.asList(Side.Color.red.ordinal(), Side.Color.green.ordinal()), Arrays.asList(28, 52));
        this.addObject(Arrays.asList(Side.Color.green.ordinal(), Side.Color.orange.ordinal()), Arrays.asList(46, 25));
        this.addObject(Arrays.asList(Side.Color.blue.ordinal(), Side.Color.red.ordinal()), Arrays.asList(37, 34));
        this.addObject(Arrays.asList(Side.Color.orange.ordinal(), Side.Color.blue.ordinal()), Arrays.asList(19, 43));

    }

    // Метод для добавления объекта
    public void addObject(List<Integer> keyNumbers, List<Integer> correspondingNumbers) {
        Map<Integer, Integer> correspondingNumbersMap = new HashMap<>();
        for (int i = 0; i < keyNumbers.size(); i++) {
            correspondingNumbersMap.put(keyNumbers.get(i), correspondingNumbers.get(i));
        }
        // Создаем ключ, сортируя числа
        List<Integer> key = new ArrayList<>(keyNumbers);
        Collections.sort(key);

        // Добавляем в хэш-таблицу
        dataStorage.put(key, correspondingNumbersMap);
    }

    // Метод для поиска объекта
    public int findObject(List<Integer> keyNumbers, int cod) {
        // Создаем ключ, сортируя числа
        List<Integer> key = new ArrayList<>(keyNumbers);
        Collections.sort(key);

        // Ищем в хэш-таблице
        return dataStorage.getOrDefault(key, null).getOrDefault(cod, null);
    }


   public String transform(Cub cub) {
        StringBuilder state = new StringBuilder("[");
        Side u = cub.sides[Cub.SideNumber.up.ordinal()];
        Side d = cub.sides[Cub.SideNumber.down.ordinal()];
        Side r = cub.sides[Cub.SideNumber.right.ordinal()];
        Side l = cub.sides[Cub.SideNumber.left.ordinal()];
        Side f = cub.sides[Cub.SideNumber.front.ordinal()];
        Side b = cub.sides[Cub.SideNumber.back.ordinal()];
        oneSide(cub, state, u, new int[]{
                l.cell[1], b.cell[3],
                b.cell[2],
                r.cell[3], b.cell[1],
                l.cell[2],
                r.cell[2],
                l.cell[3], f.cell[1],
                f.cell[2],
                f.cell[3], r.cell[1]
        });
        oneSide(cub, state, d, new int[]{
                l.cell[9], f.cell[7],
                f.cell[8],
                f.cell[9], r.cell[7],
                l.cell[8],
                r.cell[8],
                l.cell[7], b.cell[9],
                b.cell[8],
                b.cell[7], r.cell[9]
        });
        oneSide(cub, state, l, new int[]{
                u.cell[1], b.cell[3],
                u.cell[4],
                u.cell[7], f.cell[1],
                b.cell[6],
                f.cell[4],
                b.cell[9], d.cell[7],
                d.cell[4],
                d.cell[1], f.cell[7]
        });
        oneSide(cub, state, r, new int[]{
                u.cell[9], f.cell[3],
                u.cell[6],
                u.cell[3], b.cell[1],
                f.cell[6],
                b.cell[4],
                d.cell[3], f.cell[9],
                d.cell[6],
                d.cell[9], b.cell[7]
        });
        StringBuilder back=new StringBuilder();
        oneSide(cub, back, b, new int[]{
                u.cell[3], r.cell[3],
                u.cell[2],
                u.cell[1], l.cell[1],
                r.cell[6],
                l.cell[4],
                d.cell[9], r.cell[9],
                d.cell[8],
                d.cell[7], l.cell[7]
        });
        String[] backArr=back.toString().split(",");
        for (int i = backArr.length-1; i >=0; i--) {
            state.append(backArr[i]);
            state.append(",");
        }
        oneSide(cub, state, f, new int[]{
                u.cell[7], l.cell[3],
                u.cell[8],
                u.cell[9], r.cell[1],
                l.cell[6],
                r.cell[4],
                d.cell[1], l.cell[9],
                d.cell[2],
                d.cell[3], r.cell[7]
        });


        state.deleteCharAt(state.length() - 1);
        return state.append("]").toString();
    }

    void oneSide(Cub cub, StringBuilder state, Side now, int[] point) {


        int i = 1;
        int j = 0;
        state.append(findObject(Arrays.asList(now.cell[i++], point[j++], point[j++]), now.cell[i - 1]));
        state.append(",");
        state.append(findObject(Arrays.asList(now.cell[i++], point[j++]), now.cell[i - 1]));
        state.append(",");
        state.append(findObject(Arrays.asList(now.cell[i++], point[j++], point[j++]), now.cell[i - 1]));
        state.append(",");
        state.append(findObject(Arrays.asList(now.cell[i++], point[j++]), now.cell[i - 1]));
        state.append(",");
        state.append(findObject(List.of(now.cell[i++]), now.cell[i - 1]));
        state.append(",");
        state.append(findObject(Arrays.asList(now.cell[i++], point[j++]), now.cell[i - 1]));
        state.append(",");
        state.append(findObject(Arrays.asList(now.cell[i++], point[j++], point[j++]), now.cell[i - 1]));
        state.append(",");
        state.append(findObject(Arrays.asList(now.cell[i++], point[j++]), now.cell[i - 1]));
        state.append(",");
        state.append(findObject(Arrays.asList(now.cell[i++], point[j++], point[j++]), now.cell[i - 1]));
        state.append(",");
    }




}