package org.example.camera;

import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.Mat;
import org.example.UI.MyException;

public class Photographer {
    FrameGrabber grabber;
    Frame frame;

    public Photographer(int cam)  {
        this.grabber = new OpenCVFrameGrabber(cam);

        try {

            grabber.start();
        } catch (FrameGrabber.Exception e) {
            new MyException("не подключена камера");
        }

    }
    public static int getNumberOfConnectedCameras() {
        int cameraIndex = 0;
        while (true) {
            try (OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(cameraIndex)) {
                grabber.start();
                grabber.stop();
                cameraIndex++;
            } catch (FrameGrabber.Exception e) {
                return cameraIndex;
            }
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
