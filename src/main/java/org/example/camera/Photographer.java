package org.example.camera;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.example.UI.MyException;

import java.util.ArrayList;
import java.util.List;


public class Photographer {

    private final Java2DFrameConverter java2DConverter = new Java2DFrameConverter();
    private final OpenCVFrameConverter.ToMat openCVConverter = new OpenCVFrameConverter.ToMat();
    private Webcam webcam;

    public Photographer(int cam) {

        if (Webcam.getWebcams().isEmpty()) {
            new MyException("Камера не подключена");
        } else {
            webcam = Webcam.getWebcams().get(cam);
            if (webcam != null) {
                System.out.println("Камера найдена: " + webcam.getName());

                // Устанавливаем разрешение (например, VGA)
                webcam.setViewSize(WebcamResolution.VGA.getSize());
                // Открываем камеру
                webcam.open();

                // Захватываем изображение
                if (webcam.isImageNew()) {
                    webcam.getImage();
                }
            } else {

                new MyException("Камера не подключена");
            }
        }
    }

    public static List<String> getConnectedCameras() {
        List<String> names = new ArrayList<>();
        List<Webcam> web = Webcam.getWebcams();
        for (Webcam value : web) {
            names.add(value.getName());
        }
        return names;
    }

    public Mat getNext() {
        // Захват изображения
        Mat mat = openCVConverter.convert(java2DConverter.convert(webcam.getImage()));

        // Меняем порядок каналов (RGB → BGR)
        opencv_imgproc.cvtColor(mat, mat, opencv_imgproc.COLOR_RGB2BGR);

        return mat;
    }
    public void end() {
        webcam.close();
    }
}
