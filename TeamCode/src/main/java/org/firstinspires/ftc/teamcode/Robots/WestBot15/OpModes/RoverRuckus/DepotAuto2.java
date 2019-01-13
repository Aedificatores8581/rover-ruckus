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
/*
cleaner version of DepotAuto
 */
@Autonomous (name = "Depot auto 2", group = "competition autonomous   ")
public class DepotAuto2 extends WestBot15 {
    private final static boolean USING_VECTOR_FIELDS = false;
    private static final double MARKER_CLOSED_POSITION = 1, MARKER_OPEN_POSITION = 0.5;
    private final static int ON_CRATER_RIM_THRESHOLD = 15;

    private Servo maerkr;
    private GoldDetector detector;

    private Vector2 sampleVect = new Vector2();
    private AutoState autoState = AutoState.LAND;
    private Crater crater = Crater.LEFT;

    private double startTime = 0;
    double initialPosition = 0;
    private double sampleDelay = 0, claimDelay = 0, parkingDelay = 0, doubleSampleDelay = 0;

    private boolean is_aextending = false;
    private boolean onCrater = false;
    private double prevTime = 0;
    private boolean canSwitchTimer = true;
    private double d = 72; // oof
    boolean obtainedSampleLocation = false;
    double time = 0;

    public void init() {
        prevTime = UniversalFunctions.getTimeInSeconds();
        drivetrain.position = new Pose();
        msStuckDetectInit = UniversalConstants.MS_STUCK_DETECT_INIT_DEFAULT;

        super.init();

        maerkr = hardwareMap.servo.get("mrkr");
        maerkr.setPosition(MARKER_CLOSED_POSITION);

        activateGamepad1();

        detector = new GoldDetector();
        detector.opState = Detector.OperatingState.TUNING;
        FtcRobotControllerActivity.frameGrabber.detector = detector;

        drivetrain.controlState = TankDT.ControlState.FIELD_CENTRIC;

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

        if (gamepad1.dpad_left) {
            crater = Crater.LEFT;
        }

        if (gamepad1.dpad_right) {
            crater = Crater.RIGHT;
        }

        if (gamepad1.dpad_up) {
            is_aextending = true;
        }

        if (gamepad1.dpad_down) {
            is_aextending = false;
        }

        telemetry.addData("Active Timer: ", autoState);
        telemetry.addData("sample dealy: ", sampleDelay);
        telemetry.addData("claim delay: ", claimDelay);
        telemetry.addData("parking delay: ", parkingDelay);
        telemetry.addData("double-sample delay ", doubleSampleDelay);
        telemetry.addData("is extending?", is_aextending);
        telemetry.addData("Crater", crater);
    }

    @Override
    public void start() {
        super.start();

        drivetrain.position = new Pose(0, 0, Math.PI / 2);
        startTime = UniversalFunctions.getTimeInSeconds();
        autoState = AutoState.LAND;
    }

    public void loop() {
        if (!(gamepad1.left_trigger > 0.2)) {
            switch (autoState) {
                case LAND:
                    lift.setPower(-1);
                    if (lift.topPressed()) {
                        lift.liftMotor.setPower(0);
                        if (UniversalFunctions.getTimeInSeconds() - startTime > sampleDelay) {
                            drivetrain.resetEncoders();
                            autoState = AutoState.VISION;
                            time = UniversalFunctions.getTimeInSeconds();
                            startAngleY = getGyroAngleY();
                        }
                    }
                    break;

                case VISION:
                    Vector2 temp = new Vector2(-detector.element.x, detector.element.y);
                    temp.x += 640 / 2;
                    temp.y -= 480 / 2;

                    double vertAng = temp.y / 480 * motoG4.rearCamera.horizontalAngleOfView();
                    double horiAng = temp.x / 640 * motoG4.rearCamera.verticalAngleOfView();

                    double newY = (motoG4.getLocation().z - 1) / Math.tan(-vertAng - Math.toRadians(37));
                    double newX = newY * Math.tan(horiAng);
                    newY *= -1;

                    sampleVect = new Vector2(newX + motoG4.getLocation().x, newY + motoG4.getLocation().y);
                    if (Math.abs(sampleVect.x) < 5) { d = 72; }

                    drivetrain.position.y += 37.97 - newY;
                    obtainedSampleLocation = true;
                    sampleVect.y = 37.97;
                    initialPosition = drivetrain.position.y;
                    autoState = autoState.FORWARD;
                    break;

                case FORWARD:
                    //startAngleY = getGyroAngleY();
                    setRobotAngle();

                    drivetrain.updateLocation();
                    drivetrain.maxSpeed = 0.6;

                    intaek.articulateDown();

                    if (drivetrain.position.y < 2.0) {
                        drivetrain.setLeftPow(1);
                        drivetrain.setRightPow(1);
                    } else{
                        autoState = AutoState.SAMPLE;
                    }
                    break;

                case SAMPLE:
                    intaek.setPower(1);
                    intaek.dispensor.setPosition(Intake.CLOSED_DISPENSOR_POSITION);

                    lift.setPower(1);

                    drivetrain.updateLocation();
                    drivetrain.maxSpeed = 0.45;

                    setRobotAngle();

                    Vector2 newVect = new Vector2(sampleVect.x, sampleVect.y);
                    newVect.x -= drivetrain.position.x;
                    newVect.y -= drivetrain.position.y;
                    newVect.scalarMultiply(1.2);

                    drivetrain.newFieldCentric(newVect, robotAngle, 12);

                    if (newVect.magnitude() < 0.5) {
                        if (UniversalFunctions.getTimeInSeconds() - startTime > claimDelay) {
                            autoState = AutoState.TO_THE_DEPOT;
                        }
                    }
                    break;

                case TO_THE_DEPOT:
                    intaek.setPower(0);
                    intaek.articulateUp();

                    drivetrain.updateLocation();
                    drivetrain.maxSpeed = 0.5;

                    setRobotAngle();

                    newVect = new Vector2(0, d);
                    newVect.x -= drivetrain.position.x;
                    newVect.y -= drivetrain.position.y;

                    drivetrain.newFieldCentric(newVect, robotAngle, 12);

                    if (newVect.magnitude() < 0.2) {
                        drivetrain.setRightPow(0);
                        drivetrain.setLeftPow(0);
                        //claim
                        if (UniversalFunctions.getTimeInSeconds() - startTime > parkingDelay) {
                            autoState = AutoState.FACE_THE_CRATER;
                        }
                    }

                    if (UniversalFunctions.getTimeInSeconds() - startTime > 15) {
                        if (UniversalFunctions.getTimeInSeconds() - startTime > sampleDelay) {
                            autoState = AutoState.FACE_THE_CRATER;
                        }
                    }
                    break;

                case FACE_THE_CRATER:
                    drivetrain.updateLocation();
                    drivetrain.turnToFace(robotAngle, 5 * Math.PI / 4);

                    setRobotAngle();

                    if (Math.abs(UniversalFunctions.normalizeAngleRadians(robotAngle.angle(), 5 * Math.PI / 4)) < Math.toDegrees(5)) {
                        autoState = AutoState.CLAIM;
                    }
                    break;
                case CLAIM:
                    maerkr.setPosition(MARKER_OPEN_POSITION);
                    autoState = AutoState.PARK;
                    Vector2 craterVect = new Vector2();
                    craterVect.setFromPolar(1, 5*Math.PI / 4);
                    drivetrain.newFieldCentric(craterVect, robotAngle, 0.00000000000000001);
                    break;

                case PARK:
                    setRobotAngle();
                    drivetrain.updateLocation();

                    if (!onCrater) {
                        if (drivetrain.position.y < 37.5) {
                            if (is_aextending) {
                                drivetrain.maxSpeed = 0.5;
                                aextendo.aextendTM(1);
                                drivetrain.stop();
                            } else {
                                drivetrain.maxSpeed=0.8;
                                onCrater = Math.abs(normalizeGyroAngleY()) > ON_CRATER_RIM_THRESHOLD;
                            }
                        } else {
                            drivetrain.maxSpeed = 0.5;
                        }

                        if (is_aextending) {
                            if (drivetrain.position.y - (aextendo.getExtensionLength() + 6) * Math.sqrt(2) / 2 < 0 || aextendo.getExtensionLength() > 25) {
                                onCrater = true;
                            }
                        }
                    } else {
                        drivetrain.stop();
                        aextendo.extendo.setPower(0);
                        if(is_aextending) {
                            intaek.articulateDown();
                            intaek.setPower(1);
                        }
                    }
                    break;

                case DOUBLE_SAMPLE:
                    // Coming soon to a FTC team near you...
                    break;
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
        TO_THE_CRATER,
        FACE_THE_CRATER
    }
}
