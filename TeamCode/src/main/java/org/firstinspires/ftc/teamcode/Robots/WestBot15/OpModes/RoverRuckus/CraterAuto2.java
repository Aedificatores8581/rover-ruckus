package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.Drivetrain;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;
import org.firstinspires.ftc.teamcode.Vision.Detectors.GoldDetector;

import ftc.vision.Detector;

@Autonomous(name = "CraterAuto2", group = "autonomous")
public class CraterAuto2 extends WestBot15 {

    private final static boolean USING_VECTOR_FIELDS = false;
    private final static int ON_CRATER_RIM_THRESHOLD = 15;
    private boolean isDoubleSampling = false;
    boolean canDriveForwardIntermediate = false;
    private GoldDetector detector;
    //TODO: find samplePosition
    final Pose intermediatePoint = new Pose(-22,22), samplePosition = new Pose(3, 0);
    Pose temporaryPose = new Pose();

    Pose depotLocation = new Pose();

    private Vector2 sampleVect = new Vector2();
    private CraterAuto2.AutoState autoState = CraterAuto2.AutoState.LAND;

    private double startTime = 0;
    double initialPosition = 0;
    private double sampleDelay = 0, claimDelay = 0, parkingDelay = 0, doubleSampleDelay = 0;

    private boolean is_aextending = false;
    private boolean onCrater = false;
    private double prevTime = 0;
    private boolean canSwitchTimer = true;
    boolean obtainedSampleLocation = false;
    double time = 0;
    Vector2 craterVect = new Vector2();
    public double prevTimeInit = 0;
    public void init() {
        prevTime = UniversalFunctions.getTimeInSeconds();
        drivetrain.position = new Pose();
        msStuckDetectInit = UniversalConstants.MS_STUCK_DETECT_INIT_DEFAULT;

        super.init();

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
        prevTimeInit = UniversalFunctions.getTimeInSeconds();
        zeroDegreeAngle = getGyroAngle();
        intaek.articulateUp();
    }
    public void init_loop() {
        lift.setPower(-1);
        activateGamepad1();
        updateGamepad1();

        double increment = rightStick1.y * UniversalFunctions.getTimeInSeconds() - prevTime;
        if(gamepad1.a)
            isDoubleSampling = true;
        if(gamepad1.b)
            isDoubleSampling = false;
        switch (autoState) {
            case SAMPLE:
                sampleDelay += increment;

                if (gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = CraterAuto2.AutoState.CLAIM;
                    canSwitchTimer = false;
                } else if (gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = CraterAuto2.AutoState.DOUBLE_SAMPLE;
                    canSwitchTimer = false;
                }

                canSwitchTimer = gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER && gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER;
                break;

            case CLAIM:
                claimDelay += increment;

                if (gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = CraterAuto2.AutoState.PARK;
                    canSwitchTimer = false;
                } else if (gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = CraterAuto2.AutoState.SAMPLE;
                    canSwitchTimer = false;
                }

                canSwitchTimer = gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER && gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER;
                break;

            case PARK:
                parkingDelay += increment;

                if (gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = CraterAuto2.AutoState.DOUBLE_SAMPLE;
                    canSwitchTimer = false;
                } else if (gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = CraterAuto2.AutoState.CLAIM;
                    canSwitchTimer = false;
                }

                canSwitchTimer = gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER && gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER;
                break;

            case DOUBLE_SAMPLE:
                doubleSampleDelay += increment;

                if (gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = CraterAuto2.AutoState.SAMPLE;
                    canSwitchTimer = false;
                } else if (gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER && canSwitchTimer) {
                    autoState = CraterAuto2.AutoState.PARK;
                    canSwitchTimer = false;
                }

                canSwitchTimer = gamepad1.left_trigger < UniversalConstants.Triggered.TRIGGER && gamepad1.right_trigger < UniversalConstants.Triggered.TRIGGER;
                break;
        }

        if (gamepad1.dpad_left) {
        }

        if (gamepad1.dpad_right) {
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
        telemetry.addData("robotAngle", robotAngle.angle());
    }

    @Override
    public void start() {
        super.start();

        //drivetrain.position = new Pose(0, 0, Math.PI / 2);
        startTime = UniversalFunctions.getTimeInSeconds();
        autoState = CraterAuto2.AutoState.LAND;
        startAngle = zeroDegreeAngle;
    }
    public void loop() {
        setRobotAngle();
        drivetrain.turnMult = 1.85;
        switch (autoState) {
            case LAND:
                lift.setPower(1);
                if (lift.bottomPressed()) {
                    lift.liftMotor.setPower(0);
                    if (UniversalFunctions.getTimeInSeconds() - startTime > sampleDelay) {
                        drivetrain.resetEncoders();
                        autoState = CraterAuto2.AutoState.VISION;
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

                initialPosition = drivetrain.position.y;
                autoState = autoState.FORWARD;
                break;

            case FORWARD:
                lift.setPower(-1);
                drivetrain.updateLocation();
                drivetrain.position.angle = robotAngle.angle();
                drivetrain.maxSpeed = 0.7;

                if (drivetrain.position.y - initialPosition < 2) {
                    drivetrain.setLeftPow(1);
                    drivetrain.setRightPow(1);
                } else {
                    autoState = AutoState.TO_THE_DEPOT;
                }
                break;
            case TO_THE_DEPOT:

                lift.setPower(-1);
                drivetrain.updateLocation();
                drivetrain.position.angle = robotAngle.angle();
                drivetrain.driveToPoint(intermediatePoint.x, intermediatePoint.y, robotAngle, Drivetrain.Direction.FOR, 6);
                Vector2 temp2 = new Vector2(intermediatePoint.x, intermediatePoint.y);
                temp2.x -= drivetrain.position.x;
                temp2.y -= drivetrain.position.y;
                if (temp2.magnitude() > 6 && !canDriveForwardIntermediate) {
                    drivetrain.driveToPoint(intermediatePoint.x, intermediatePoint.y, robotAngle, Drivetrain.Direction.FOR, 6);
                } else {
                    canDriveForwardIntermediate = true;
                    drivetrain.newFieldCentric(new Vector2(-8, 0), robotAngle, 0.5);
                    Pose relativeDTPosition = new Pose(drivetrain.position.clone());
                    relativeDTPosition.x -= temporaryPose.x;
                    relativeDTPosition.y -= temporaryPose.y;
                    if (relativeDTPosition.toVector().magnitude() > 20)
                        autoState = AutoState.FACE_THE_DEPOT;
                }
                if (!canDriveForwardIntermediate) {
                    temporaryPose = new Pose(drivetrain.position);
                }
                break;
            case TO_THE_DEPOT2:
                drivetrain.updateLocation();
                drivetrain.position.angle = robotAngle.angle();
                drivetrain.driveToPoint(depotLocation.x, depotLocation.y, robotAngle, Drivetrain.Direction.FOR, 3);
                if (UniversalFunctions.maxAbs(drivetrain.leftFore.getPower(), drivetrain.rightFore.getPower()) > 0.5)
                    autoState = AutoState.FACE_THE_DEPOT;
                break;
            case FACE_THE_DEPOT:
                drivetrain.updateLocation();
                drivetrain.maxSpeed = 0.5;
                Vector2 temp3 = new Vector2();
                temp3.setFromPolar(1, Math.PI * 3 / 4);
                drivetrain.turnToFace(robotAngle, temp3);
                if (UniversalFunctions.maxAbs(drivetrain.leftFore.getPower(), drivetrain.rightFore.getPower()) < 0.075) {
                    drivetrain.stop();
                    autoState = AutoState.TO_THE_CRATER;
                }
                break;
            case CLAIM:
                if (aextendo.getExtensionLength() < 25)
                    aextendo.aextendTM(1);
                else {
                    aextendo.aextendTM(0);
                    intaek.articulateDown();
                    intaek.setPower(-1);
                    //TODO: Add claiming condition
                }
                break;
            case TO_THE_CRATER:
                drivetrain.updateLocation();
                drivetrain.position.angle = robotAngle.angle();
                intaek.setPower(0);
                intaek.articulateUp();
                drivetrain.updateLocation();
                drivetrain.position.angle = robotAngle.angle();
                if (!aextendo.isRetracted()) {
                    intaek.articulateUp();
                    aextendo.aextendTM(-1);
                } else {
                    drivetrain.position.angle = robotAngle.angle();
                    drivetrain.driveToPoint(samplePosition.x, samplePosition.y, robotAngle, Drivetrain.Direction.BACK, 5);
                    if (Math.abs(drivetrain.leftFore.getPower()) < 0.3) {
                        autoState = AutoState.SAMPLE;
                        //intaek.articulateDown();
                    }
                }
                break;
            case SAMPLE:
                intaek.setPower(1);
                drivetrain.updateLocation();
                drivetrain.position.angle = robotAngle.angle();
                drivetrain.maxSpeed = 0.5;
                if (Math.abs(drivetrain.leftFore.getPower()) < 0.1) {
                    if (aextendo.getExtensionLength() + 9 + 5 < sampleVect.magnitude()) {
                        aextendo.aextendTM(1 - 0.4 * aextendo.getExtensionLength() / (sampleVect.magnitude() + 9 + 5));
                    } else {
                        aextendo.aextendTM(-1);
                        intaek.articulateUp();
                        if (aextendo.getExtensionLength() + 9 + 5 < sampleVect.magnitude() - 5) {
                            aextendo.aextendTM(0);
                            autoState = AutoState.FACE_THE_CRATER;
                        }
                    }
                } else {
                    Vector2 intakeSampleVect = new Vector2(sampleVect.x, sampleVect.y);
                    intakeSampleVect.setFromPolar(sampleVect.magnitude(), Math.PI / 2);
                    intakeSampleVect.x -= 0.455;
                    intakeSampleVect.rotate(sampleVect.angle() - Math.PI / 2);
                    drivetrain.turnToFace(robotAngle, intakeSampleVect);
                }
                break;
            case FACE_THE_CRATER:
                drivetrain.updateLocation();
                drivetrain.position.angle = robotAngle.angle();
                intaek.setPower(0);
                drivetrain.updateLocation();
                drivetrain.position.angle = robotAngle.angle();
                drivetrain.turnToFace(robotAngle, 0);
                if (Math.abs(drivetrain.leftFore.getPower()) < 0.1)
                    autoState = AutoState.PARK;
                break;
            case PARK:
                if (aextendo.getExtensionLength() < 17) {
                    aextendo.aextendTM(0.7);
                    intaek.articulateDown();
                    intaek.setPower(1);
                } else {
                    aextendo.aextendTM(1);
                }
                if (aextendo.getExtensionLength() < 24) {
                    if (isDoubleSampling) {
                        ///TODO:add transfer
                    } else {
                        aextendo.aextendTM(0);
                    }
                }
                break;
        }
        telemetry.addData("AutoState", autoState);

        telemetry.addData("position", drivetrain.position);
    }
    public void stop(){
        super.stop();
        detector.isInitialized = false;
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
        FACE_THE_CRATER,
        TO_THE_DEPOT2,
        FACE_THE_DEPOT,
        FACE_SAMPLE
    }
}
