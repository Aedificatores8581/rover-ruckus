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

    final boolean IS_AEXTENDINGTM = false;

    BlockDetector detector;
    boolean hasDrove;
    double prevLeft, prevRight = 0;
    Vector2 sampleVect = new Vector2();
    boolean onCrater = false;
    double d = 72;
    double startTime = 0;
    double speedMult = 1;
    final boolean USING_VECTOR_FIELDS= false;
    private final static int ON_CRATER_RIM_THRESHOLD = 15;
    AutoState autoState = AutoState.LOWER;
    double initialPosition = 0;
    Crater crater = Crater.LEFT;
    boolean doubleSample = false;
    double prevTime = 0;
    boolean canSwitchTimer = true;

    double sampleDelay = 0, claimDelay = 0, parkingDelay = 0, doubleSampleDelay = 0;
    boolean obtainedSampleLocation = false;
    double time = 0;
    boolean thing = false;
    boolean thing2 = false;
    public void init(){
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
    }
    @Override
    public void start(){
        super.start();
        drivetrain.position = new Pose(0, 0, Math.PI / 2);
        startTime = UniversalFunctions.getTimeInSeconds();
        autoState = AutoState.LOWER;
    }
    public void loop(){
        if(! (gamepad1.left_trigger > 0.2)) {
            switch (autoState) {
                case LOWER:
                    lift.setPower(-1);
                    if (lift.topPressed()) {
                        lift.liftMotor.setPower(0);
                        if (UniversalFunctions.getTimeInSeconds() - startTime > sampleDelay) {
                            autoState = AutoState.FORWARD;
                            time = UniversalFunctions.getTimeInSeconds();
                            startAngleY = getGyroAngleY();
                        }
                    }
                    break;
                case FORWARD:
                    drivetrain.updateEncoders();
                    double leftChange3 = drivetrain.averageLeftEncoders() - prevLeft;
                    double rightChange3 = drivetrain.averageRightEncoders() - prevRight;
                    drivetrain.updateLocation(leftChange3, rightChange3);
                    prevLeft = drivetrain.averageLeftEncoders();
                    prevRight = drivetrain.averageRightEncoders();
                    setRobotAngle();
                    drivetrain.maxSpeed = 0.6;

                    if(drivetrain.position.y < 6.0) {
                        drivetrain.setLeftPow(1);
                        drivetrain.setRightPow(1);
                    }
                    else{
                        autoState = AutoState.SAMPLE;
                    }
                    break;
                case SAMPLE:
                    //TODO: add dropping intaek
                    lift.setPower(1);
                    drivetrain.updateEncoders();
                    double leftChange = drivetrain.averageLeftEncoders() - prevLeft;
                    double rightChange = drivetrain.averageRightEncoders() - prevRight;
                    drivetrain.updateLocation(leftChange, rightChange);
                    prevLeft = drivetrain.averageLeftEncoders();
                    prevRight = drivetrain.averageRightEncoders();
                    setRobotAngle();
                    drivetrain.maxSpeed = 0.6;
                    if (UniversalFunctions.getTimeInSeconds() - startTime > 2)
                        speedMult = 1;
                    Vector2 temp = new Vector2(-detector.element.x, detector.element.y);
                    temp.x += 640 / 2;
                    temp.y -= 480 / 2;

                    double vertAng = temp.y / 480 * motoG4.rearCamera.horizontalAngleOfView();
                    double horiAng = temp.x / 640 * motoG4.rearCamera.verticalAngleOfView();

                    double newY = (motoG4.getLocation().z - 2 / 2) / Math.tan(-vertAng - Math.toRadians(37));
                    double newX = newY * Math.tan(horiAng);
                    newY *= -1;
                    if (!hasDrove) {
                        sampleVect = new Vector2(newX + motoG4.getLocation().x, newY + motoG4.getLocation().y);
                        if (sampleVect.x > 10) {
                            sampleVect.x += 4;
                            d=70;
                        }
                        if(Math.abs(sampleVect.x) < 5)
                            d = 72;
                    }
                    if (!obtainedSampleLocation) {
                        drivetrain.position.y += 37.97 - newY;
                        obtainedSampleLocation = true;
                        sampleVect.y = 37.97;
                        sampleVect.scalarMultiply(1.2);
                        initialPosition = drivetrain.position.y;
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

                    if (newVect.magnitude() < 0.33333333333333) {
                        if (UniversalFunctions.getTimeInSeconds() - startTime > claimDelay)
                            autoState = AutoState.CLAIM;
                    }
                    break;

                case CLAIM:
                    //TODO: add lifting intaek
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
                    if(Math.abs(sampleVect.x) < -8) {
                        if(robotAngle.angle() < -(Math.PI / 2 - Math.atan2(72 - 37.97 * 1.2, sampleVect.x))){
                            drivetrain.setLeftPow(1);
                            drivetrain.setRightPow(1);
                        }
                        else
                            drivetrain.teleOpLoop(newVect, new Vector2(), robotAngle);
                        d = 71;
                    }
                    else if(sampleVect.x > 8){
                        double angleBetween = UniversalFunctions.normalizeAngleRadians(newVect.angle(), robotAngle.angle());
                        if (Math.sin(angleBetween) < 0) {
                            drivetrain.setLeftPow(1);
                            drivetrain.setRightPow(-1);
                        }
                        else {
                            drivetrain.teleOpLoop(newVect, new Vector2(), robotAngle);
                            drivetrain.setLeftPow();
                            drivetrain.setRightPow();
                        }
                    }
                    else if(sampleVect.x < -8){
                        d = 68;
                    }
                    else
                        drivetrain.teleOpLoop(newVect, new Vector2(), robotAngle);
                    drivetrain.setLeftPow();
                    drivetrain.setRightPow();

                    if (newVect.magnitude() < 0.2) {
                        drivetrain.setRightPow(0);
                        drivetrain.setLeftPow(0);
                        //claim
                        if (UniversalFunctions.getTimeInSeconds() - startTime > parkingDelay)
                            autoState = AutoState.PARK;
                    }
                    if (UniversalFunctions.getTimeInSeconds() - startTime > 15) {
                        if (UniversalFunctions.getTimeInSeconds() - startTime > sampleDelay)
                            autoState = AutoState.PARK;
                    }
                    thing = true;
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

                    if (onCrater == false) {
                        int i = crater == Crater.RIGHT ? 1 : -1;
                        Vector2 tempV = new Vector2(i * Math.sqrt(2) / 2, -Math.sqrt(2) / 2);
                        tempV.setFromPolar(tempV.magnitude(), tempV.angle() + i * Math.toRadians(5));
                        if(thing){
                            double angleBetween = UniversalFunctions.normalizeAngleRadians(tempV.angle(), robotAngle.angle());
                            if (Math.sin(angleBetween) < 0) {
                                drivetrain.setLeftPow(1);
                                drivetrain.setRightPow(-1);
                            }
                            else {
                                double cos = Math.cos(angleBetween);
                                double turnMult = Math.abs(cos) + 1;
                                drivetrain.setLeftPow(-turnMult * cos);
                                drivetrain.setRightPow(turnMult * cos);
                            }
                            if(Math.abs(drivetrain.leftPow) < 0.9)
                                thing = false;
                        }
                        else{
                            drivetrain.teleOpLoop(tempV, new Vector2(), robotAngle);
                            drivetrain.setLeftPow();
                            drivetrain.setRightPow();
                        }
                        if(IS_AEXTENDINGTM) {
                            if (drivetrain.position.y < 37.5) {
                                aextendo.aextendTM(1);
                                drivetrain.stop();
                            }
                            if (drivetrain.position.y - (aextendo.getExtensionLength() + 6) * Math.sqrt(2) / 2 < 0 || aextendo.getExtensionLength() > 25)
                                onCrater = true;
                        }
                        else{
                            if(thing = false)
                                onCrater = Math.abs(normalizeGyroAngleY()) > ON_CRATER_RIM_THRESHOLD;
                        }
                    } else {
                        drivetrain.stop();
                        aextendo.extendo.setPower(0);
                        if(IS_AEXTENDINGTM) {
                            //TODO: intaek
                        }
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
            }

            telemetry.addData("robot ang: ", Math.toDegrees(robotAngle.angle()));
            telemetry.addData("sampleVect, ", sampleVect);
            telemetry.addData("element position", detector.element);
            telemetry.addData("position", drivetrain.position.toString());
            telemetry.addData("turnVector", drivetrain.turnVector);
            telemetry.addData("onCrater", onCrater);
            telemetry.addData("state", autoState);
            telemetry.addData("topPressed", lift.topPressed());
        }
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
