package org.firstinspires.ftc.teamcode.Vision.Detectors;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.util.List;

import ftc.vision.Detector;

public class MineralDetector extends Detector {
    private GoldDetector goldDetector;
    private SilverDetector silverDetector;
    public Mat workingImage = new Mat();
    public List<MatOfPoint> elements;
    public Point element;
    public MineralDetector(){
        super();
        goldDetector = new GoldDetector();
        silverDetector = new SilverDetector();
    }

    public Mat result(){
        Core.add(goldDetector.result(), silverDetector.result(), workingImage);
        return workingImage;
    }
    public void detect(Mat image){
        goldDetector.detect(image);
        silverDetector.detect(image);
    }
    public List<MatOfPoint> detectGold(Mat image){
        return goldDetector.detectContours(image);
    }
    public List<MatOfPoint> detectSilver(Mat image){
        return silverDetector.detectContours(image);
    }

}
