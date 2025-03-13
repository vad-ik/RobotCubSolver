package org.example.camera;

import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.global.opencv_highgui;

import org.bytedeco.opencv.opencv_core.Point;
import org.example.UI.MyException;
import org.example.solver.Cub;
import org.example.solver.Side;

import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.List;

import static org.bytedeco.opencv.global.opencv_core.mean;

public class RubiksCubeDetection {
   public static Photographer photo;

    public static void main(String[] args) {
        while (true) {
            Cub cub = new Cub(100);
            RubiksCubeDetection detector = new RubiksCubeDetection(true);
            detector.nextPhoto(cub, true);
            opencv_highgui.waitKey(0);
        }
    }

    Mat srcMat = new Mat(4, 1, opencv_core.CV_32FC2); // 4 точки, 1 канал, тип CV_32FC2 (2 канала: x и y)
    Mat srcMat2 = new Mat(4, 1, opencv_core.CV_32FC2); // 4 точки, 1 канал, тип CV_32FC2 (2 канала: x и y)
    public static Mat dstMat = new Mat(4, 1, opencv_core.CV_32FC2);

    Mat image;
    public SettingCamera setting=new SettingCamera();
    RubiksCubeDetection(boolean debug)  {

        if (debug) {

            setting.camPort=0;
            if (photo !=null) {
                photo.end();
            }
            photo = new Photographer(setting.camPort);

            image = photo.getNext();

            // Отображение результата
            opencv_highgui.imshow("Original Image", image);
            opencv_highgui.waitKey(0);
            Scanner scanner = new Scanner(System.in);
            setting.point = new float[]{scanner.nextFloat(), scanner.nextFloat(),//левый верхний0
                    scanner.nextFloat(), scanner.nextFloat(),//центр верхний2
                    scanner.nextFloat(), scanner.nextFloat(),//правый верхний4
                    scanner.nextFloat(), scanner.nextFloat(),//правый нижний6
                    scanner.nextFloat(), scanner.nextFloat(),//центр нижний8
                    scanner.nextFloat(), scanner.nextFloat()//левый нижний10
            };
            try {
                SettingCamera.saveToFile(setting,"setting");
            } catch (IOException e) {
                new MyException("не удалось сохранить настройки");
            }
        } else {

            setting= SettingCamera.loadFromFile("setting");

            if (photo !=null) {
                photo.end();
            }
            photo = new Photographer(setting.camPort);


        }

        updateSrcMat(setting.point,srcMat,srcMat2);

        FloatIndexer dstIndexer = dstMat.createIndexer();
        dstIndexer.put(0, 0, 0.0f, 0.0f);     // Точка 1 (x, y)
        dstIndexer.put(1, 0, 200.0f, 0.0f);   // Точка 2 (x, y)
        dstIndexer.put(2, 0, 200.0f, 200.0f); // Точка 3 (x, y)
        dstIndexer.put(3, 0, 0.0f, 200.0f);   // Точка 4 (x, y)
    }
    public static void updateSrcMat(float[] point,Mat srcMat,Mat srcMat2){
        FloatIndexer srcIndexer = srcMat.createIndexer();
        srcIndexer.put(0, 0, point[0], point[1]); // Точка 1 (x, y)
        srcIndexer.put(1, 0, point[2], point[3]); // Точка 2 (x, y)
        srcIndexer.put(2, 0, point[8], point[9]); // Точка 3 (x, y)
        srcIndexer.put(3, 0, point[6], point[7]);  // Точка 4 (x, y)


        FloatIndexer srcIndexer2 = srcMat2.createIndexer();
        srcIndexer2.put(0, 0, point[2], point[3]); // Точка 1 (x, y)
        srcIndexer2.put(1, 0, point[4], point[5]); // Точка 2 (x, y)
        srcIndexer2.put(2, 0, point[10], point[11]); // Точка 3 (x, y)
        srcIndexer2.put(3, 0, point[8], point[9]);  // Точка 4 (x, y)
    }
    public void getNextPhoto(){
        image = photo.getNext();
    }

    public void nextPhoto(Cub cub, boolean debug) {
        if (!debug) {
            image = photo.getNext();
        }
        // Заполняем dstMat с помощью FloatIndexer
        getEdge(srcMat,  image, 1, 3, cub, debug,1);
        // Заполняем dstMat с помощью FloatIndexer
        getEdge(srcMat2,  image, 0, 2, cub, debug,2);
        if (debug) {
            drawPoint();
            opencv_highgui.waitKey(0);
            opencv_highgui.destroyAllWindows();
        }
    }
    public static Mat getTransform(Mat image, Mat srcMat, Mat dstMat){
        Mat transformMatrix = opencv_imgproc.getPerspectiveTransform(srcMat, dstMat);

        // Применяем перспективное преобразование
        Mat warped = new Mat();
        opencv_imgproc.warpPerspective(image, warped, transformMatrix, new Size(200, 200));
        return warped;
    }
    void getEdge(Mat srcMat, Mat image, int iMin, int iMax, Cub cub, boolean debug,int num) {


        // Вычисляем матрицу перспективного преобразования
        Mat warped =getTransform(image,srcMat,dstMat);
        // Разделяем грань на 9 квадратов и распознаем цвета
        recognizeColors(warped, iMin, iMax, cub, debug,num);
        if (debug) {
            opencv_highgui.imshow("Warped Face" + iMin, warped);
        }

    }
    void drawPoint(){

        drawPoints(image, srcMat);
        drawPoints(image, srcMat2);
            // Отображение результата
            opencv_highgui.imshow("Original Image" , image);

    }
    public void drawPoints(Mat image, Mat points) {
        // Создаем индексер для доступа к данным points
        FloatIndexer indexer = points.createIndexer();
        // Цвет точек (BGR формат)
        Scalar color = new Scalar(0, 0, 255, 0); // Красный цвет
        // Радиус точек
        int radius = 5;
        // Толщина линии (заполненная точка)
        int thickness = -1;
        // Рисуем каждую точку
        for (int i = 0; i < points.rows(); i++) {
            // Получаем координаты точки
            float x = indexer.get(i, 0, 0);
            float y = indexer.get(i, 0, 1);
            // Рисуем точку
            opencv_imgproc.circle(image, new Point((int) x, (int) y), radius, color, thickness, opencv_imgproc.LINE_AA, 0);
        }
        drawGrid( image,  points);
    }
    public void drawGrid(Mat image, Mat points) {
        // Создаем индексер для доступа к данным points
        FloatIndexer indexer = points.createIndexer();

        // Получаем координаты углов четырехугольника
        float x1 = indexer.get(0, 0, 0); // Левый верхний
        float y1 = indexer.get(0, 0, 1);
        float x2 = indexer.get(1, 0, 0); // Правый верхний
        float y2 = indexer.get(1, 0, 1);
        float x3 = indexer.get(3, 0, 0); // Левый нижний
        float y3 = indexer.get(3, 0, 1);
        float x4 = indexer.get(2, 0, 0); // Правый нижний
        float y4 = indexer.get(2, 0, 1);

        // Цвет линий сетки (BGR формат)
        Scalar gridColor = new Scalar(0, 255, 0, 0); // Зеленый цвет

        // Толщина линии
        int thickness = 2;

        // Рисуем вертикальные линии
        for (int i = 1; i < 3; i++) {
            float t = i / 3.0f; // Параметр интерполяции (0.33, 0.66)

            // Верхняя точка вертикальной линии
            float topX = x1 + t * (x2 - x1);
            float topY = y1 + t * (y2 - y1);

            // Нижняя точка вертикальной линии
            float bottomX = x3 + t * (x4 - x3);
            float bottomY = y3 + t * (y4 - y3);

            // Рисуем линию
            opencv_imgproc.line(image, new Point((int) topX, (int) topY), new Point((int) bottomX, (int) bottomY), gridColor, thickness, opencv_imgproc.LINE_AA, 0);
        }

        // Рисуем горизонтальные линии
        for (int i = 1; i < 3; i++) {
            float t = i / 3.0f; // Параметр интерполяции (0.33, 0.66)

            // Левая точка горизонтальной линии
            float leftX = x1 + t * (x3 - x1);
            float leftY = y1 + t * (y3 - y1);

            // Правая точка горизонтальной линии
            float rightX = x2 + t * (x4 - x2);
            float rightY = y2 + t * (y4 - y2);

            // Рисуем линию
            opencv_imgproc.line(image, new Point((int) leftX, (int) leftY), new Point((int) rightX, (int) rightY), gridColor, thickness, opencv_imgproc.LINE_AA, 0);
        }
    }
    // Функция для распознавания цветов на грани
    private void recognizeColors(Mat face, int iMin, int iMax, Cub cub, boolean debug,int num) {
        int rows = face.rows();
        int cols = face.cols();
        // Разделяем грань на 9 квадратов (3x3)
        for (int i = 0; i < 3; i++) {
            for (int j = iMin; j < iMax; j++) {
                // Вычисляем координаты квадрата
                int x1 = j * (cols / 3);
                int y1 = i * (rows / 3);
                int x2 = (j + 1) * (cols / 3);
                int y2 = (i + 1) * (rows / 3);
                // Вырезаем квадрат
                int border = 10;
                Mat square = new Mat(face, new Rect(x1 + border, y1 + border, x2 - x1 - border, y2 - y1 - border));
                String myColor=detectColor(square, debug);
                if (iMin == 0) {
                    cub.sides[Cub.SideNumber.right.ordinal()].cell[i * 3 + j + 1] = Side.Color.valueOf(myColor).ordinal();
                    if (debug) {System.out.println(i * 3 + j + 1);}
                } else {
                    cub.sides[Cub.SideNumber.front.ordinal()].cell[i * 3 + j + 1] = Side.Color.valueOf(myColor).ordinal();
                    if (debug) {System.out.println(i * 3 + j + 1);}
                }
                if (debug) {
                    System.out.println("color:"+num+" " +i+" "+j);
                    opencv_highgui.imshow("block Image"+num+" " +i+" "+j, square);
                    System.out.println(myColor);
                }
            }
        }
    }
    public String detectColor(Mat mat, boolean debug) {
        // Вычисляем среднее значение по каждому каналу (BGR)
        Scalar meanColor = mean(mat);
        int blue = (int) meanColor.get(0);
        int green = (int) meanColor.get(1);
        int red = (int) meanColor.get(2);

        Color targetColor=new Color( red,  green,  blue);
        if (debug) {
            System.out.println("rgb("+red + ", " + green + ", " + blue+")");
        }

        String closestColorName = findClosestColorName(targetColor);
        System.out.println("Ближайший цвет: " + closestColorName);
return closestColorName;
    }


    private static final Map<Color, String> COLOR_NAMES = Map.of(
            Color.RED, "red",
            Color.ORANGE, "orange",
            Color.YELLOW, "yellow",
            Color.WHITE, "white",
            Color.BLUE, "blue",
            Color.GREEN, "green"
    );
   

    // Функция для вычисления расстояния между двумя цветами
    public static double distance(Color from, Color to) {
        return Math.sqrt(
                Math.pow(from.getRed() - to.getRed(), 2)
                        + Math.pow(from.getGreen() - to.getGreen(), 2)
                        + Math.pow(from.getBlue() - to.getBlue(), 2)
        );
    }

    // Функция для поиска названия ближайшего цвета
    public static String findClosestColorName(Color targetColor) {
        return COLOR_NAMES.entrySet().stream()
                .min((entry1, entry2) -> Double.compare(
                        distance(targetColor, entry1.getKey()),
                        distance(targetColor, entry2.getKey())
                ))
                .map(Map.Entry::getValue) // Извлекаем название цвета
                .orElse("Неизвестный цвет"); // Если список пуст
    }


}
