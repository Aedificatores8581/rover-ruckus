package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.Drivetrain;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Components.Sensors.TouchSensor;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.Map.AttractionField;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;
import org.firstinspires.ftc.teamcode.Vision.Detectors.GoldDetector;
import org.opencv.core.Point;

import ftc.vision.Detector;

@Autonomous (name = "Crater auto", group = "competition autonomous   ")
public class CraterAuto1 extends WestBot15 {
    GoldDetector detector;

    boolean hasDrove;

    double prevLeft, prevRight = 0;
    double hardNewY;

    boolean hasDriven = false;
    boolean parking, onCrater = false;

    Point newNewPoint = new Point();
    double rightEncPosition, leftEncPosition;
    Vector2 sampleVect = new Vector2();
    double d = 63;
    double startTime = 0;
    double speedMult = 1;
    TouchSensor top, bottom;

    final boolean USING_VECTOR_FIELDS= false;
    private final static int ON_CRATER_RIM_THRESHOLD = 15;
    AutoState autoState = AutoState.LOWER;
    public void init(){
        drivetrain.position = new Pose();
        msStuckDetectInit = 500000;
        super.init();

        top = new TouchSensor();
        bottom = new TouchSensor();
        top.init(hardwareMap, "top");
        bottom.init(hardwareMap, "bot");
        activateGamepad1();
        //TODO: remove from init
        startAngleY = getGyroAngleY();
        detector = new GoldDetector();
        detector.opState = Detector.OperatingState.TUNING;
        FtcRobotControllerActivity.frameGrabber.detector = detector;
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
        startTime = UniversalFunctions.getTimeInSeconds();
        if(gamepad1.left_trigger > 0.2)
            d = 62.5;
    }

    public void loop(){
        switch (autoState) {
            case LOWER:
                lift.setPower(-1);
                if(lift.topPressed()) {
                    lift.liftMotor.setPower(0);
                    autoState = AutoState.SAMPLE;
                    startTime = UniversalFunctions.getTimeInSeconds();
                }
                break;
            case SAMPLE:
                drivetrain.updateEncoders();
                double leftChange = drivetrain.averageLeftEncoders() - prevLeft;
                double rightChange = drivetrain.averageRightEncoders() - prevRight;
                drivetrain.updateLocation(leftChange, rightChange);
                prevLeft = drivetrain.averageLeftEncoders();
                prevRight = drivetrain.averageRightEncoders();
                setRobotAngle();
                drivetrain.maxSpeed = 0.7;
                if(UniversalFunctions.getTimeInSeconds() - startTime > 2)
                    speedMult = 1;


                Vector2 temp = new Vector2(-detector.element.x, detector.element.y);
                temp.x += 640/ 2;
                temp.y -= 480 / 2;

                double vertAng = temp.y / 480 * motoG4.rearCamera.horizontalAngleOfView();
                double horiAng = temp.x / 640 * motoG4.rearCamera.verticalAngleOfView();

                double newY = (motoG4.getLocation().z - 2 / 2) / Math.tan(-vertAng - 0.364773814);
                double newX = newY * Math.tan(horiAng);
                newY *= -1;
                if(!hasDrove) {
                    sampleVect = new Vector2(newX + motoG4.getLocation().x, newY + motoG4.getLocation().y);
                    if(sampleVect.x < -10){
                        sampleVect.x -= 4;
                    }
                }
                if (UniversalFunctions.getTimeInSeconds() - startTime > 1) {
                    hasDrove = true;
                    Vector2 newVect = new Vector2(sampleVect.x, sampleVect.y);
                    newVect.x -= drivetrain.position.x;
                    newVect.y -= drivetrain.position.y;
                    Vector2 temp2 = new Vector2(newVect.x, newVect.y);

                    if (newVect.magnitude() > 12)
                        newVect.setFromPolar(speedMult, newVect.angle());
                    else
                        newVect.scalarMultiply(6 / 12);

                    drivetrain.teleOpLoop(newVect, new Vector2(), robotAngle);
                    drivetrain.setLeftPow();
                    drivetrain.setRightPow();

                    if (newVect.magnitude() < 0.6666666) {
                        autoState = AutoState.CLAIM;
                    }
                }

                if(drivetrain.position.y > sampleVect.y - 2) {
                    //autoState = AutoState.CLAIM;
                }
                break;

            case CLAIM:

                drivetrain.updateEncoders();
                double leftChange1 = drivetrain.averageLeftEncoders() - prevLeft;
                double rightChange1 = drivetrain.averageRightEncoders() - prevRight;
                drivetrain.updateLocation(leftChange1, rightChange1);
                prevLeft = drivetrain.averageLeftEncoders();
                prevRight = drivetrain.averageRightEncoders();
                setRobotAngle();
                drivetrain.maxSpeed = 0.9;

                Vector2 newVect = new Vector2(0, d);
                newVect.x -= drivetrain.position.x;
                newVect.y -= drivetrain.position.y;
                if (newVect.magnitude() > 12)
                    newVect.setFromPolar(1, newVect.angle());
                else
                    newVect.scalarMultiply(1.0 / 12);

                if (Math.abs(normalizeGyroAngleY()) > ON_CRATER_RIM_THRESHOLD) {
                    onCrater = true;
                } else {
                    onCrater = false;
                }
                if(onCrater){
                    drivetrain.setLeftPow(0);
                    drivetrain.setRightPow(0);
                }
                else {
                    drivetrain.teleOpLoop(newVect, new Vector2(), robotAngle);
                    drivetrain.setLeftPow();
                    drivetrain.setRightPow();
                }
                if(newVect.magnitude() < 0.5){
                    drivetrain.setRightPow(0);
                    drivetrain.setLeftPow(0);
                    //claim
                    //autoState = AutoState.PARK;
                }
                if(UniversalFunctions.getTimeInSeconds() - startTime > 15){
                    //autoState = AutoState.PARK;
                }
                break;
            case PARK:
                drivetrain.direction = Drivetrain.Direction.FOR;
                setRobotAngle();
                drivetrain.maxSpeed = 0.5;
                if(drivetrain.position.y < 38)
                    drivetrain.maxSpeed = 0.8;
                if(USING_VECTOR_FIELDS){
                    AttractionField leftSample = new AttractionField();
                }
                if (Math.abs(normalizeGyroAngleY()) > ON_CRATER_RIM_THRESHOLD) {
                    onCrater = true;
                } else {
                    onCrater = false;
                }

                if (!onCrater && UniversalFunctions.getTimeInSeconds() - startTime < 17) {
                    Vector2 tempV = new Vector2(-1 * Math.sqrt(2)/2, -Math.sqrt(2) / 2);
                    tempV.setFromPolar(tempV.magnitude(), tempV.angle() + Math.toRadians(10));
                    drivetrain.teleOpLoop(tempV, new Vector2(), robotAngle);
                    drivetrain.setLeftPow();
                    drivetrain.setRightPow();
                } else {
                    // Stopped
                    drivetrain.setRightPow(0.0);
                    drivetrain.setLeftPow(0.0);
                }
                break;
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
        }telemetry.addData("robot ang: ", Math.toDegrees(robotAngle.angle()));
        telemetry.addData("sampleVect, ", sampleVect);
        telemetry.addData("element position", detector.element);
        telemetry.addData("position", drivetrain.position.toString());
        telemetry.addData("turnVector", drivetrain.turnVector);
        telemetry.addData("onCrater", onCrater);
        telemetry.addData("state", autoState);
    }

    public void stop(){
        super.stop();
        detector.isInitialized = false;
    }
}
