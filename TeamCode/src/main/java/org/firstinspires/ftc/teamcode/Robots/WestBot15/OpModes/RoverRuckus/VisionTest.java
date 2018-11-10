package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.Components.Sensors.Cameras.MotoG4;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;
import org.firstinspires.ftc.teamcode.Vision.Detectors.BlockDetector;
import org.firstinspires.ftc.teamcode.Vision.Detectors.GenericDetector;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Size;

import ftc.vision.Detector;

@Autonomous(name = "Block Detector Test", group = "none")
public class VisionTest extends OpMode {
    BlockDetector detector;
    Point sampleLocation;
    MotoG4 motoG4;

    public void init(){
        msStuckDetectInit = 500000;

        motoG4 = new MotoG4();
        motoG4.setLocationAndOrientation(new Point3(0, 0, 12), new Point3(0, 0, 0));

        detector = new BlockDetector();
        detector.opState = Detector.OperatingState.TUNING;

        FtcRobotControllerActivity.frameGrabber.detector = this.detector;
    }
    public void initLoop() {
        telemetry.addData("location 1", motoG4.getObjectLocationRear(detector.elements.get(0), detector.result().size(), 2));
        telemetry.addData("location 2", motoG4.getObjectLocationRear2(detector.elements.get(0), detector.result().size(), 2));
    }

    @Override
    public void start() { super.start(); }

    public void loop() {
        Vector2 _elementVector = new Vector2(detector.element.y + 320, -detector.element.x - 240);

        double adjustedVerticalAngle = _elementVector.y / 640 * motoG4.verticalAngleOfViewRear();
        double adjustedHorizontalAngle = _elementVector.x / 480 * motoG4.horizontalAngleOfViewRear();

        double locationPointY = 11 / Math.tan(adjustedVerticalAngle);
        double locationPointX = locationPointY * Math.tan(adjustedHorizontalAngle);
        Point locationPoint = new Point(locationPointX, locationPointY);

        telemetry.addData("location 1", (int) (100 * locationPoint.x) / 100.0 + ", " + (int) (100 * locationPoint.y) / 100.0);
        telemetry.addData("location 2", motoG4.getObjectLocationRear2(detector.element.clone(), detector.workingImage.size(), 2));
        telemetry.addData("center", (float) detector.element.x + ", " + (float) detector.element.y);
        telemetry.addData("size", detector.workingImage.size());
        telemetry.addData("Element Vector", _elementVector);
    }

    public void stop(){
        super.stop();
        detector.isInitialized = false;
    }
}
