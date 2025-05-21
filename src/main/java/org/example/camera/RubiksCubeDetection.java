package org.example.camera;

import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.*;
import org.example.UI.MyException;
import org.example.solvers.solverLayer.Cub;
import org.example.solvers.solverLayer.Side;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

import static org.bytedeco.opencv.global.opencv_core.mean;

public class RubiksCubeDetection {
    public static Photographer photo;
    public static Mat srcMat = new Mat(4, 1, opencv_core.CV_32FC2); // 4 точки, 1 канал, тип CV_32FC2 (2 канала: x и y)
    public static Mat srcMat2 = new Mat(4, 1, opencv_core.CV_32FC2); // 4 точки, 1 канал, тип CV_32FC2 (2 канала: x и y)
    public static Mat dstMat = new Mat(4, 1, opencv_core.CV_32FC2);

    public Mat image;
    public SaveSettings setting = new SaveSettings();

    public void startCam() {
        if (photo != null) {
            photo.end();
        }
        photo = new Photographer(setting.getCamPort());
    }

    private String debugString = "";

    public void save() {
        String path = "C:\\rubiks\\debug\\";
        String name = UUID.randomUUID().toString().substring(0, 8);
        opencv_imgcodecs.imwrite(path + name + ".png", image);
        try {
            FileWriter writer = new FileWriter(path + name + ".txt", true);

            writer.write(debugString);
            debugString = "";
            writer.close();
        } catch (IOException e) {
            System.out.println("Возникла ошибка во время записи, проверьте данные.");
        }
    }

    RubiksCubeDetection(boolean debug) {

        if (debug) {

            setting.setCamPort(0);
            if (photo != null) {
                photo.end();
            }
            photo = new Photographer(setting.getCamPort());

            image = photo.getNext();

            // Отображение результата
            opencv_highgui.imshow("Original Image", image);
            opencv_highgui.waitKey(0);
            Scanner scanner = new Scanner(System.in);
            setting.setPoint(new float[]{scanner.nextFloat(), scanner.nextFloat(),//левый верхний0
                    scanner.nextFloat(), scanner.nextFloat(),//центр верхний2
                    scanner.nextFloat(), scanner.nextFloat(),//правый верхний4
                    scanner.nextFloat(), scanner.nextFloat(),//правый нижний6
                    scanner.nextFloat(), scanner.nextFloat(),//центр нижний8
                    scanner.nextFloat(), scanner.nextFloat()//левый нижний10
            });
            try {
                SaveSettings.saveToFile(setting, "setting");
            } catch (IOException e) {
                new MyException("не удалось сохранить настройки");
            }
        } else {

            updateSetting();

            if (photo != null) {
                photo.end();
            }
            photo = new Photographer(setting.getCamPort());
        }

        updateSrcMat(setting.getPoint(), srcMat, srcMat2);

        FloatIndexer dstIndexer = dstMat.createIndexer();
        dstIndexer.put(0, 0, 0.0f, 0.0f);     // Точка 1 (x, y)
        dstIndexer.put(1, 0, 200.0f, 0.0f);   // Точка 2 (x, y)
        dstIndexer.put(2, 0, 200.0f, 200.0f); // Точка 3 (x, y)
        dstIndexer.put(3, 0, 0.0f, 200.0f);   // Точка 4 (x, y)
    }

    public void updateSetting() {
        setting = SaveSettings.loadFromFile("setting");
        updateSrcMat(setting.getPoint(), srcMat, srcMat2);
    }

    public static void updateSrcMat(float[] point, Mat srcMat, Mat srcMat2) {
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

    public void getNextPhoto() {
        image = photo.getNext();
    }

    public void nextPhoto(Cub cub, boolean debug) {
        if (!debug) {
            image = photo.getNext();
        }
        // Заполняем dstMat с помощью FloatIndexer
        getEdge(srcMat, image, 1, 3, cub, debug, 1);
        // Заполняем dstMat с помощью FloatIndexer
        getEdge(srcMat2, image, 0, 2, cub, debug, 2);
        if (debug) {
            drawPoint();
            opencv_highgui.waitKeyEx();
//            opencv_highgui.waitKey(0);
            opencv_highgui.destroyAllWindows();
        }
    }

    public static Mat getTransform(Mat image, Mat srcMat, Mat dstMat) {
        Mat transformMatrix = opencv_imgproc.getPerspectiveTransform(srcMat, dstMat);
        // Применяем перспективное преобразование
        Mat warped = new Mat();
        opencv_imgproc.warpPerspective(image, warped, transformMatrix, new Size(200, 200));
        return warped;
    }

    void getEdge(Mat srcMat, Mat image, int iMin, int iMax, Cub cub, boolean debug, int num) {
        // Вычисляем матрицу перспективного преобразования
        Mat warped = getTransform(image, srcMat, dstMat);
        // Разделяем грань на 9 квадратов и распознаем цвета
        recognizeColors(warped, iMin, iMax, cub, debug, num);
        if (debug) {
            opencv_highgui.imshow("Warped Face" + iMin, warped);
        }
    }

    void drawPoint() {

        drawPoints(image, srcMat);
        drawPoints(image, srcMat2);
        // Отображение результата
        opencv_highgui.imshow("Original Image", image);
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
    }


    // Функция для распознавания цветов на грани
    private void recognizeColors(Mat face, int iMin, int iMax, Cub cub, boolean debug, int num) {
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
                String myColor = detectColor(square, debug);
//                String myColor=detectColorHSV(square, debug);
                debugString="color:" + num + " " + i + " " + j+" "+myColor;
                if (iMin == 0) {
                    cub.sides[Cub.SideNumber.right.ordinal()].cell[i * 3 + j + 1] = Side.Color.valueOf(myColor).ordinal();
                } else {
                    cub.sides[Cub.SideNumber.front.ordinal()].cell[i * 3 + j + 1] = Side.Color.valueOf(myColor).ordinal();
                }
                if (debug) {
                    System.out.println(i * 3 + j + 1);
                }
                if (debug) {
                    System.out.println("color:" + num + " " + i + " " + j);
                    opencv_highgui.imshow("block Image" + num + " " + i + " " + j + myColor, square);
                    opencv_highgui.resizeWindow("block Image" + num + " " + i + " " + j + myColor, 300, 100);
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

        Color targetColor = new Color(red, green, blue);
        if (debug) {
            System.out.println("rgb(" + red + ", " + green + ", " + blue + ")");
        }
        String closestColorName = getColorForRatio(targetColor.getRed(), targetColor.getGreen(), targetColor.getBlue(), debug);

        System.out.println("Ближайший цвет: " + closestColorName);
        return closestColorName;
    }


    public static String getColorForRatio(double r, double g, double b, boolean debug) {
        r++;
        g++;
        b++;
        if (debug) {
            float[] hsv = new float[3];
            Color.RGBtoHSB((int) r, (int) g, (int) b, hsv);
            System.out.println((Arrays.toString(hsv)));
            System.out.println("r/b=" + (r / b));
            System.out.println("r/g=" + (r / g));
            System.out.println("b/g=" + (b / g));
            System.out.println("g/r=" + (g / r));
            System.out.println("g/b=" + (g / b));
        }
        if (r / g > 3 && r / b > 3) {
            return "red";
        }
        if (g / r > 1.2 && g / b > 1.2) {
            return "green";
        }
        if (b / r > 1.2 && b / g > 1.2) {
            return "blue";
        }
        if (0.8 < r / b && r / b < 1.2 && 0.8 < b / g && b / g < 1.2 && 0.8 < g / r && g / r < 1.2) {
            return "white";
        }
        if (r / g > 1.2 && r / b > 1.2) {
            return "orange";
        }
        if (r / b > 1.2 && g / b > 1.2) {
            return "yellow";
        }
        return "Non";
    }
}
