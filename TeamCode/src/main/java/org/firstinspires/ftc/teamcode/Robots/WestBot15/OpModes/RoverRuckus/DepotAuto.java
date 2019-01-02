package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.Drivetrain;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Lift;
import org.firstinspires.ftc.teamcode.Components.Sensors.TouchSensor;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.Map.AttractionField;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;
import org.firstinspires.ftc.teamcode.Vision.Detectors.BlockDetector;
import org.opencv.core.Point;

import ftc.vision.Detector;

@Autonomous (name = "Depot auto", group = "competition autonomous   ")
public class DepotAuto extends WestBot15 {
    BlockDetector detector;
    boolean hasDrove;
    double prevLeft, prevRight = 0;
    Vector2 sampleVect = new Vector2();
    boolean onCrater = false;
    double d = 63;
    double startTime = 0;
    double speedMult = 1;
    final boolean USING_VECTOR_FIELDS= false;
    private final static int ON_CRATER_RIM_THRESHOLD = 15;
    AutoState autoState = AutoState.LOWER;
    Crater crater = Crater.LEFT;
    boolean doubleSample = false;
    double prevTime = 0;
    boolean canSwitchTimer = true;
    double sampleDelay = 0, claimDelay = 0, parkingDelay = 0, doubleSampleDelay = 0;
    boolean obtainedSampleLocation = false;
    public void init(){
        activateGamepad1();
        updateGamepad1();
        double increment = rightStick1.y * UniversalFunctions.getTimeInSeconds() - prevTime;
        switch (autoState){
            case SAMPLE:
                sampleDelay += increment;
                if(gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.CLAIM;
                    canSwitchTimer = false;
                }
                else if(gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.DOUBLE_SAMPLE;
                    canSwitchTimer = false;
                }
                canSwitchTimer = gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER && gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER;
                break;
            case CLAIM:
                claimDelay += increment;
                if(gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.PARK;
                    canSwitchTimer = false;
                }
                else if(gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.SAMPLE;
                    canSwitchTimer = false;
                }
                canSwitchTimer = gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER && gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER;
                break;
            case PARK:
                parkingDelay += increment;
                if(gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.DOUBLE_SAMPLE;
                    canSwitchTimer = false;
                }
                else if(gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.CLAIM;
                    canSwitchTimer = false;
                }
                canSwitchTimer = gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER && gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER;
                break;
            case DOUBLE_SAMPLE:
                doubleSampleDelay += increment;
                if(gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.SAMPLE;
                    canSwitchTimer = false;
                }
                else if(gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.PARK;
                    canSwitchTimer = false;
                }
                canSwitchTimer = gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER && gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER;
                break;
        }
        telemetry.addData("Active Timer: ", autoState);
        telemetry.addData("sample dealy: ", sampleDelay);
        telemetry.addData("claim delay: ", claimDelay);
        telemetry.addData("parking delay: ", parkingDelay);
        telemetry.addData("double-sample delay ", doubleSampleDelay);
        prevTime = UniversalFunctions.getTimeInSeconds();
        drivetrain.position = new Pose();
        msStuckDetectInit = 500000;
        super.init();

        activateGamepad1();
        //TODO: remove from init
        startAngleY = getGyroAngleY();
        detector = new BlockDetector();
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
        autoState = AutoState.LOWER;
    }
    public void loop(){
        switch (autoState) {
            case LOWER:
                lift.setPower(1);
                if(!lift.topPressed()) {
                    lift.liftMotor.setPower(0);
                    if(UniversalFunctions.getTimeInSeconds() - startTime > sampleDelay)
                        autoState = AutoState.SAMPLE;
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
                drivetrain.maxSpeed = 0.6;
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
                if(!obtainedSampleLocation){
                    drivetrain.position.y += 37.97 - newY;
                }
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
                    if(UniversalFunctions.getTimeInSeconds() - startTime > claimDelay)
                        autoState = AutoState.CLAIM;
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
                drivetrain.maxSpeed = 0.5;

                newVect = new Vector2(0, d);
                newVect.x -= drivetrain.position.x;
                newVect.y -= drivetrain.position.y;
                if (newVect.magnitude() > 12)
                    newVect.setFromPolar(1, newVect.angle());
                else
                    newVect.scalarMultiply(1.0 / 12);


                    drivetrain.teleOpLoop(newVect, new Vector2(), robotAngle);
                    drivetrain.setLeftPow();
                    drivetrain.setRightPow();
                if(newVect.magnitude() < 0.1){
                    drivetrain.setRightPow(0);
                    drivetrain.setLeftPow(0);
                    //claim
                    if(UniversalFunctions.getTimeInSeconds() - startTime > parkingDelay)
                        autoState = AutoState.PARK;
                }
                if(UniversalFunctions.getTimeInSeconds() - startTime > 15){
                    if(UniversalFunctions.getTimeInSeconds() - startTime > sampleDelay)
                        autoState = AutoState.PARK;
                }
                break;
            case PARK:
                drivetrain.direction = Drivetrain.Direction.FOR;
                drivetrain.updateEncoders();
                double leftChange2 = drivetrain.averageLeftEncoders() - prevLeft;
                double rightChange2 = drivetrain.averageRightEncoders() - prevRight;
                drivetrain.updateLocation(leftChange2, rightChange2);
                prevLeft = drivetrain.averageLeftEncoders();
                prevRight = drivetrain.averageRightEncoders();
                setRobotAngle();
                drivetrain.maxSpeed = 0.5;

                if(onCrater == false) {
                    int i = crater == Crater.RIGHT ? 1 : -1;
                    Vector2 tempV = new Vector2(i * Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
                    tempV.setFromPolar(tempV.magnitude(), tempV.angle() - i * Math.toRadians(5));
                    drivetrain.teleOpLoop(tempV, new Vector2(), robotAngle);
                    drivetrain.setLeftPow();
                    drivetrain.setRightPow();
                    if(drivetrain.position.y < 30)
                        /*aextendo.aextendTM(1);
                    if(drivetrain.position.y - aextendo.getExtensionLength() * Math.sqrt(2) / 2 < 0)
                        onCrater = true;
                }
                else {*/
                    drivetrain.stop();
                    aextendo.aextendTM(0);
                }

                break;
            case DOUBLE_SAMPLE:

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
    enum Crater{
        LEFT,
        RIGHT
    }
}
