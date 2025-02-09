package org.example.camera;

import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.Mat;

public class Photographer {
    FrameGrabber grabber;
    Frame frame;

    public Photographer(int cam) {
        this.grabber = new OpenCVFrameGrabber(cam);
        try {
            grabber.start();
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

    Mat getNext(){
        getFrame();
        Mat img = converter.convert(frame);
        return img;
    }



}
