package org.example.camera;

import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.Mat;
import org.example.UI.MyException;

import java.util.Arrays;

public class Photographer {
    FrameGrabber grabber;
    Frame frame;

    public Photographer(int cam)  {
        this.grabber = new OpenCVFrameGrabber(cam);
        grabber.setImageWidth(1920);  // Попробуй стандартное разрешение для Logitech C922
        grabber.setImageHeight(1080);
        grabber.setFrameRate(30);
        grabber.setFormat("dshow");
        grabber.setPixelFormat(org.bytedeco.opencv.global.opencv_core.CV_8UC3); // Указание цветного формата


        try {

            grabber.start();
        } catch (FrameGrabber.Exception e) {

            new MyException("не подключена камера");
            throw new RuntimeException(e);
        }

    }
    public static int getNumberOfConnectedCameras() {

        try {
            return VideoInputFrameGrabber .getDeviceDescriptions().length;
        } catch (FrameGrabber.Exception e) {
            throw new RuntimeException(e);
        }

    }

    void getFrame() {
        try {
            frame = grabber.grab();
        } catch (FrameGrabber.Exception e) {
            throw new RuntimeException(e);
        }
    }


    OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

    public Mat getNext(){
        getFrame();
        Mat img = converter.convert(frame);
        return img;
    }
    public void end(){
        try {
            grabber.close();
        } catch (FrameGrabber.Exception e) {
            throw new RuntimeException(e);
        }
    }



}
