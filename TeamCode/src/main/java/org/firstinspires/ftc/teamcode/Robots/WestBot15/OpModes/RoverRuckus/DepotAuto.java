package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.Drivetrain;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Intake;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Lift;
import org.firstinspires.ftc.teamcode.Components.Sensors.TouchSensor;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.Map.AttractionField;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;
import org.firstinspires.ftc.teamcode.Vision.Detectors.GoldDetector;
import org.opencv.core.Point;

import ftc.vision.Detector;

@Autonomous (name = "Depot auto", group = "competition autonomous   ")
public class DepotAuto extends WestBot15 {
    Servo maerkr;
    boolean IS_AEXTENDINGTM = false;
    final double MARKER_CLOSED_POSITION = 1, MARKER_OPEN_POSITION = 0.5;
    GoldDetector detector;
    boolean hasDrove = false;
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
    boolean singleVect = false;
    double prevTime = 0;
    boolean canSwitchTimer = true;

    double sampleDelay = 0, claimDelay = 0, parkingDelay = 0, doubleSampleDelay = 0;
    boolean obtainedSampleLocation = false;
    double time = 0;
    boolean thing = false;
    boolean thing2 = false;
    public void init(){
        prevTime = UniversalFunctions.getTimeInSeconds();
        drivetrain.position = new Pose();
        msStuckDetectInit = 500000;
        super.init();
        maerkr = hardwareMap.servo.get("mrkr");
        maerkr.setPosition(MARKER_CLOSED_POSITION);
        activateGamepad1();
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
        lift.setPower(1);
    }
    public void init_loop() {
        activateGamepad1();
        updateGamepad1();
        double increment = rightStick1.y * UniversalFunctions.getTimeInSeconds() - prevTime;
        switch (autoState) {
            case SAMPLE:
                sampleDelay += increment;
                if (gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.CLAIM;
                    canSwitchTimer = false;
                } else if (gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.DOUBLE_SAMPLE;
                    canSwitchTimer = false;
                }
                canSwitchTimer = gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER && gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER;
                break;
            case CLAIM:
                claimDelay += increment;
                if (gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.PARK;
                    canSwitchTimer = false;
                } else if (gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.SAMPLE;
                    canSwitchTimer = false;
                }
                canSwitchTimer = gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER && gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER;
                break;
            case PARK:
                parkingDelay += increment;
                if (gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.DOUBLE_SAMPLE;
                    canSwitchTimer = false;
                } else if (gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.CLAIM;
                    canSwitchTimer = false;
                }
                canSwitchTimer = gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER && gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER;
                break;
            case DOUBLE_SAMPLE:
                doubleSampleDelay += increment;
                if (gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.SAMPLE;
                    canSwitchTimer = false;
                } else if (gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = AutoState.PARK;
                    canSwitchTimer = false;
                }
                canSwitchTimer = gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER && gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER;
                break;
        }
        if (gamepad1.dpad_left)
            crater = Crater.LEFT;
        if(gamepad1.dpad_right)
            crater = Crater.RIGHT;
        if(gamepad1.dpad_up)
            IS_AEXTENDINGTM = true;
        if(gamepad1.dpad_down)
            IS_AEXTENDINGTM = false;
        telemetry.addData("Active Timer: ", autoState);
        telemetry.addData("sample dealy: ", sampleDelay);
        telemetry.addData("claim delay: ", claimDelay);
        telemetry.addData("parking delay: ", parkingDelay);
        telemetry.addData("double-sample delay ", doubleSampleDelay);
        telemetry.addData("IS_AEXTENDINGTM", IS_AEXTENDINGTM);
        telemetry.addData("Crater", crater);
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
                            //startAngleY = getGyroAngleY();
                        }
                    }
                    break;
                case FORWARD:
                    //startAngleY = getGyroAngleY();
                    drivetrain.updateEncoders();
                    double leftChange3 = drivetrain.averageLeftEncoders() - prevLeft;
                    double rightChange3 = drivetrain.averageRightEncoders() - prevRight;
                    drivetrain.updateLocation(leftChange3, rightChange3);
                    prevLeft = drivetrain.averageLeftEncoders();
                    prevRight = drivetrain.averageRightEncoders();
                    setRobotAngle();
                    drivetrain.maxSpeed = 0.6;
                    intaek.articulateDown();
                    if(drivetrain.position.y < 2.0) {
                        drivetrain.setLeftPow(1);
                        drivetrain.setRightPow(1);
                    }
                    else{
                        autoState = AutoState.SAMPLE;
                    }
                    break;
                case SAMPLE:
                    drivetrain.leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                    drivetrain.leftFore.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                    drivetrain.rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                    drivetrain.rightFore.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

                    intaek.setPower(1);
                    intaek.dispensor.setPosition(Intake.CLOSED_DISPENSOR_POSITION);
                    lift.setPower(1);
                    drivetrain.updateEncoders();
                    double leftChange = drivetrain.averageLeftEncoders() - prevLeft;
                    double rightChange = drivetrain.averageRightEncoders() - prevRight;
                    drivetrain.updateLocation(leftChange, rightChange);
                    prevLeft = drivetrain.averageLeftEncoders();
                    prevRight = drivetrain.averageRightEncoders();
                    setRobotAngle();
                    drivetrain.maxSpeed = 0.45;
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
                        hasDrove = true;
                        if(Math.abs(sampleVect.x) < 5)
                            d = 72;
                    }
                    if (!obtainedSampleLocation) {
                        drivetrain.position.y += 37.97 - newY;
                        obtainedSampleLocation = true;
                        sampleVect.y = 37.97;
                        initialPosition = drivetrain.position.y;
                    }
                    Vector2 newVect = new Vector2(sampleVect.x, sampleVect.y);
                    newVect.x -= drivetrain.position.x;
                    newVect.y -= drivetrain.position.y;
                    newVect.scalarMultiply(1.2);
                    Vector2 temp2 = new Vector2(newVect.x, newVect.y);
                    if (newVect.magnitude() > 12)
                        newVect.setFromPolar(speedMult, newVect.angle());
                    else
                        newVect.scalarMultiply(6 / 12);

                    if (Math.abs(sampleVect.x) < 5) {
                        drivetrain.leftPow = 1;
                        drivetrain.rightPow = 1;
                        drivetrain.setLeftPow();
                        drivetrain.setRightPow();
                    }
                    else{
                        //                   if(singleVect == false){
                        drivetrain.teleOpLoop(newVect, new Vector2(), robotAngle);
                        //                     singleVect = true;
                        //                       }
                    }
                    if (newVect.magnitude() < 0.5 || (-drivetrain.position.y + sampleVect.y < 0)) {
                        if (UniversalFunctions.getTimeInSeconds() - startTime > claimDelay)
                            autoState = AutoState.CLAIM;
                    }
                    telemetry.addData("newVect", newVect);
                    break;

                case CLAIM:
                    intaek.setPower(0);
                    intaek.articulateUp();
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
                    if (sampleVect.x > -8){
                        if(drivetrain.rightPow > drivetrain.leftPow){
                            drivetrain.setRightPow(1);
                            drivetrain.setLeftPow(1);
                        }
                    }
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
                            if(Math.abs(drivetrain.leftPow) < 1)
                                thing = false;
                            if(Math.abs(drivetrain.leftFore.getPower()) < 0.5 || Math.abs(drivetrain.rightFore.getPower()) < 0.5)
                                maerkr.setPosition(MARKER_OPEN_POSITION);
                        }
                        else{
                            if(UniversalFunctions.getTimeInSeconds() - startTime > parkingDelay) {
                                drivetrain.teleOpLoop(tempV, new Vector2(), robotAngle);
                                drivetrain.setLeftPow();
                                drivetrain.setRightPow();
                            }
                        }

                        if (drivetrain.position.y < 37.5) {
                            if(IS_AEXTENDINGTM) {

                                drivetrain.maxSpeed = 0.5;
                                aextendo.aextendTM(1);
                                drivetrain.stop();
                            }
                            else{
                                drivetrain.maxSpeed=0.8;
                                onCrater = Math.abs(normalizeGyroAngleY()) > ON_CRATER_RIM_THRESHOLD;
                            }
                        }
                        else
                            drivetrain.maxSpeed = 0.5;
                        if(IS_AEXTENDINGTM) {
                            if (drivetrain.position.y - (aextendo.getExtensionLength() + 6) * Math.sqrt(2) / 2 < 0 || aextendo.getExtensionLength() > 25)
                                onCrater = true;
                        }
                    } else {
                        drivetrain.stop();
                        aextendo.extendo.setPower(0);
                        if(IS_AEXTENDINGTM) {
                            intaek.articulateDown();
                            intaek.setPower(1);
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
            telemetry.addData("onCrater", onCrater);
            telemetry.addData("state", autoState);
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
