package org.example.camera;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;

import java.util.UUID;

public class CreateDataset {
    String outputPath = "";
    public CreateDataset() {
        this.outputPath = "C:\\rubiks\\dataset";
    }

public  void addToDataset(Mat image){
    getEdge(RubiksCubeDetection.srcMat, image, 1, 3,  1);
    // Заполняем dstMat с помощью FloatIndexer
    getEdge(RubiksCubeDetection.srcMat2, image, 0, 2,   2);


}

    private void getEdge(Mat srcMat, Mat image, int iMin, int iMax, int num) {
        Mat warped = RubiksCubeDetection.getTransform(image, srcMat, RubiksCubeDetection.dstMat);
        // Разделяем грань на 9 квадратов и распознаем цвета
        recognizeColors(warped, iMin, iMax,   num);
    }
    private void recognizeColors(Mat face, int iMin, int iMax , int num) {
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


                // Сохраните изображение в PNG

                opencv_imgcodecs.imwrite(outputPath+"/"+num+" "+i+" "+j+" "+ UUID.randomUUID().toString().substring(0, 8)+".png", square);
            }
        }
    }
}
