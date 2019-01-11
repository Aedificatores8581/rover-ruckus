package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Vision.Detectors.GoldDetector;

import ftc.vision.Detector;

public class AutonomousDepotSide extends WestBot15 {
    final double BACK_MARKER_CLOSED_POSITION = 1, BACK_MARKER_OPEN_POSITION = 0.5;
    GoldDetector detector;
    AutoState autoState = AutoState.LAND;
    double sampleDelay = 0;
    @Override
    public void init(){
        drivetrain.position = new Pose();
        msStuckDetectInit = 500000;
        super.init();
        activateGamepad1();
        detector = new GoldDetector();
        detector.opState = Detector.OperatingState.TUNING;
        FtcRobotControllerActivity.frameGrabber.detector = detector;
        lift.setPower(1);
    }
    @Override
    public void start(){
        super.start();
        autoState = AutoState.LAND;

    }
    public void loop(){
        switch(autoState){
            case LAND:
                lift.setPower(-1);
                if (lift.topPressed()) {
                    if (getCurrentTime() > sampleDelay) {
                        autoState = AutoState.VISION;
                    }
                }
                break;
            case VISION:

        }

    }
    public enum AutoState{
        LAND,
        VISION,
        FORWARD,
        SAMPLE,
        TO_THE_DEPOT,
        CLAIM,
        PARK,
        INTAKE,
        DOUBLE_SAMPLE,
        TO_THE_LANDER,
        DISPENSE,
        TO_THE_CRATER
    }
}
