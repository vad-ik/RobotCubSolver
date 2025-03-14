package org.example.camera;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.example.UI.MyException;

import java.util.ArrayList;
import java.util.List;


public class Photographer {

    private Java2DFrameConverter java2DConverter = new Java2DFrameConverter();
    private OpenCVFrameConverter.ToMat openCVConverter = new OpenCVFrameConverter.ToMat();
    private Webcam webcam;

    public Photographer(int cam) {
        webcam = Webcam.getWebcams().get(cam);
        webcam = Webcam.getDefault();
        if (webcam != null) {
            System.out.println("Камера найдена: " + webcam.getName());

            // Устанавливаем разрешение (например, VGA)
            webcam.setViewSize(WebcamResolution.VGA.getSize());


//            System.out.println(WebcamResolution.VGA.getSize());

            // Открываем камеру
            webcam.open();

            // Захватываем изображение
            if (webcam.isImageNew()) {
                webcam.getImage();


            }


        } else {

            new MyException("не подключена камера");

        }

    }

    public static List<String> getConnectedCameras() {
        List<String> names=new ArrayList<>();
        List<Webcam> web= Webcam.getWebcams();
        for (int i = 0; i < web.size(); i++) {
            names.add(web.get(i).getName());
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
