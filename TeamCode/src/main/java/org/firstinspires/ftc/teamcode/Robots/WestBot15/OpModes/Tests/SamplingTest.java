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
    public void initLoop(){
        //telemetry.addData("location 1", motoG4.rearCamera.getObjectLocation(detector.elements.get(0), detector.result().size(), 2));
    }
    @Override
    public void start(){
        super.start();
        drivetrain.position = new Pose(0, 0, Math.PI / 2);
    }

    public void loop(){
        drivetrain.updateEncoders();
        double leftChange = drivetrain.averageLeftEncoders() - prevLeft;
        double rightChange = drivetrain.averageRightEncoders() - prevRight;
        drivetrain.updateLocation(leftChange, rightChange);
        prevLeft = drivetrain.averageLeftEncoders();
        prevRight = drivetrain.averageRightEncoders();
        setRobotAngle();

        drivetrain.maxSpeed = 0.2;
        if(gamepad1.left_trigger > 0.2){
            drivetrain.maxSpeed = 0.4;
        }

        Vector2 temp = new Vector2(-detector.element.x, detector.element.y);
        temp.x += 640/ 2;
        temp.y -= 480 / 2;

        double vertAng = temp.y / 480 * motoG4.rearCamera.horizontalAngleOfView();
        double horiAng = temp.x / 640 * motoG4.rearCamera.verticalAngleOfView();

        double newY = (motoG4.getLocation().z - 2 / 2) / Math.tan(-vertAng - 0.364773814);
        double newX = newY * Math.tan(horiAng);
        newY *= -1;
        if(gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && !hasDrove) {
            hasDrove = true;
            sampleVect = new Vector2(newX + motoG4.getLocation().x, newY + motoG4.getLocation().y);
        }
        if(gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER && hasDrove) {
            hasDrove = false;
            drivetrain.setLeftPow(0);
            drivetrain.setRightPow(0);
        }
        if(hasDrove) {
            if (gamepad1.right_stick_button && !hasDriven) {
                hasDriven = true;
            } else
                hasDriven = false;
            if (hasDriven) {
                newVect = new Vector2(sampleVect.x, sampleVect.y);
                    newVect.x -= drivetrain.position.x;
                    newVect.y -= drivetrain.position.y;
                    Vector2 temp2 = new Vector2(newVect.x, newVect.y);
                      if (newVect.magnitude() > 12)
                        newVect.setFromPolar(1, newVect.angle());
                    else
                        newVect.scalarMultiply(1.0 / 12);
                    drivetrain.teleOpLoop(newVect, new Vector2(), robotAngle);
                    drivetrain.setLeftPow();
                    drivetrain.setRightPow();
                telemetry.addData("temp2 radius", temp2.magnitude());
                    telemetry.addData("temp2", (int) temp2.x + ", " + (int)temp2.y);
                /*

                if(!parking && newVect.magnitude() < 4) {
                    parking = true;
                    newVect.setFromPolar(sampleVect.magnitude(), Math.PI - sampleVect.angle());
                }
                if(parking){
                    if(!onCrater) {
                        drivetrain.teleOpLoop(newVect, new Vector2(), robotAngle);
                        drivetrain.setLeftPow();
                        drivetrain.setRightPow();
                    }
                    else{
                        drivetrain.setRightPow(0);
                        drivetrain.setLeftPow(0);
                    }
                    if (Math.abs(gyroAngles.getY()) > ON_CRATER_RIM_THRESHOLD) {
                        onCrater = true;
                    } else {
                        onCrater = false;
                    }*/

            }
        }
        /*if(hasDrove) {
            drivetrain.updateLocation(drivetrain.averageLeftEncoders() - prevLeft0, drivetrain.averageRightEncoders() - prevRight);
            prevLeft0 = drivetrain.averageLeftEncoders();
            prevRight = drivetrain.averageRightEncoders();
            drivetrain.driveToPoint(sampleVect.x, sampleVect.y, TankDT.Direction.FOR);
            if(gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER) {
                hasDrove = false;
                drivetrain.setLeftPow(0);
                drivetrain.setRightPow(0);
            }
        }*/
        telemetry.addData("sampleVect, ", sampleVect);
        telemetry.addData("element position", detector.element);
        telemetry.addData("position", drivetrain.position.toString());
        telemetry.addData("leftposition", drivetrain.averageLeftEncoders());
        telemetry.addData("rightposition", drivetrain.averageRightEncoders());
        telemetry.addData("leftChange", prevLeft);
        telemetry.addData("rightChange", prevRight);
        telemetry.addData("turnVector", drivetrain.turnVector);
        telemetry.addData("newVect", newVect);
        telemetry.addData("newVect magnitude", newVect.magnitude());
    }

    public void stop(){
        super.stop();
        detector.isInitialized = false;
    }
}
