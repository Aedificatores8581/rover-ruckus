package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.Math.GyroAngles;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Vision.Detectors.GoldDetector;
import org.opencv.core.Point;

import ftc.vision.Detector;

@Autonomous(name = "sampling test", group = "none")
public class SamplingTest extends WestBot15 {
    GoldDetector detector;
    boolean hasDrove;
    double prevLeft, prevRight = 0;
    double hardNewY;
    boolean hasDriven = false;
    boolean parking= false, onCrater = false;
    Vector2 newVect = new Vector2();
    Point newNewPoint = new Point();
    double rightEncPosition, leftEncPosition;
    Vector2 sampleVect = new Vector2();
    Pose robotPose = new Pose();
    double xAng = 0;

    GyroAngles gyroAngles;
    Orientation angle;

    private final static int ON_CRATER_RIM_THRESHOLD = 60;

    public void init(){
        drivetrain.position = new Pose(0, 0, Math.PI / 2);
        msStuckDetectInit = 500000;
        usingIMU = true;
        super.init();
        activateGamepad1();
        detector = new GoldDetector();
        detector.opState = Detector.OperatingState.TUNING;
        FtcRobotControllerActivity.frameGrabber.detector = detector;
        angle = new Orientation();
        gyroAngles = new GyroAngles(angle);
        normalizeGyroAngle();
        drivetrain.controlState = TankDT.ControlState.FIELD_CENTRIC;
        drivetrain.direction = TankDT.Direction.BACK;
        drivetrain.leftFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivetrain.leftFore.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drivetrain.leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivetrain.leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drivetrain.rightFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivetrain.rightFore.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drivetrain.rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivetrain.rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void init_loop(){
        super.init_loop();

        Vector2 temp = new Vector2(-detector.element.x, detector.element.y);
        temp.x += 640 / 2;
        temp.y -= 480 / 2;

        double vertAng = temp.y / 480 * motoG4.rearCamera.horizontalAngleOfView();
        double horiAng = temp.x / 640 * motoG4.rearCamera.verticalAngleOfView();

        double newY = (motoG4.getLocation().z - 1) / Math.tan(-vertAng - Math.toRadians(37));
        double newX = newY * Math.tan(horiAng);
        newY *= -1;
        sampleVect = new Vector2(newX - motoG4.getLocation().x, newY - motoG4.getLocation().y);
        telemetry.addData("location of sample", sampleVect);
    }
    @Override
    public void start(){
        super.start();
        drivetrain.position = new Pose(0, 0, 0);
    }

    public void loop(){
        drivetrain.updateLocation();
        setRobotAngle();

        Vector2 temp = new Vector2(-detector.element.x, detector.element.y);
        temp.x += 640 / 2;
        temp.y -= 480 / 2;

        double vertAng = temp.y / 480 * motoG4.rearCamera.horizontalAngleOfView();
        double horiAng = temp.x / 640 * motoG4.rearCamera.verticalAngleOfView();

        double newY = (motoG4.getLocation().z - 1) / Math.tan(-vertAng - Math.toRadians(37));
        double newX = newY * Math.tan(horiAng);
        newY *= -1;
        sampleVect = new Vector2(newX, newY);

        if(!gamepad1.left_bumper){
            drivetrain.driveToPoint(sampleVect.x, sampleVect.y, robotAngle, drivetrain.direction.FOR, 12);
        }
        else
            drivetrain.stop();
        telemetry.addData("location of sample", sampleVect);
    }

    public void stop(){
        super.stop();
        detector.isInitialized = false;
    }
}
