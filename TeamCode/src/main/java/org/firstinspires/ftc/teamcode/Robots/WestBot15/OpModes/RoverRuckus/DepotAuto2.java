package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.Drivetrain;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Intake;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;
import org.firstinspires.ftc.teamcode.Vision.Detectors.GoldDetector;

import java.util.concurrent.CancellationException;

import ftc.vision.Detector;
/*
cleaner version of DepotAuto
 */
@Autonomous (name = "Depot auto 2", group = "competition autonomous   ")
public class DepotAuto2 extends WestBot15 {
    private final static boolean USING_VECTOR_FIELDS = false;
    private final static int ON_CRATER_RIM_THRESHOLD = 15;
    boolean isTIMED = false;
    private boolean CanSwitchTimer2 = false;
    private boolean isDoubleSampling = false;
    Pose poseAfterSample = new Pose();
    private GoldDetector detector;

    private Vector2 sampleVect = new Vector2();
    private AutoState autoState = AutoState.LAND;
    private Crater crater = Crater.LEFT;

    private double startTime = 0;
    double initialPosition = 0;
    private double sampleDelay = 0, claimDelay = 0, parkingDelay = 5, doubleSampleDelay = 0;

    private boolean is_aextending = false;
    private boolean Auto_Early_Stop = true;
    private boolean onCrater = false;
    private double prevTime = 0;
    private boolean canSwitchTimer = true;
    private double depotYLocation = 60;
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

        double increment = 1;
        if(gamepad1.a)
            isDoubleSampling = true;
        if(gamepad1.b)
            isDoubleSampling = false;
        if (gamepad1.left_bumper && canSwitchTimer) {
            if (autoState == AutoState.SAMPLE)
                sampleDelay += increment;
            if (autoState == AutoState.CLAIM)
                claimDelay += increment;
            if (autoState == AutoState.PARK)
                parkingDelay += increment;
            canSwitchTimer = false;
        }
        else if (!gamepad1.left_bumper)
            canSwitchTimer = true;

        if (gamepad1.right_bumper && CanSwitchTimer2) {
            if (autoState == AutoState.LAND)
            autoState = AutoState.SAMPLE;
            else if (autoState == AutoState.SAMPLE)
                autoState = AutoState.CLAIM;
            else if (autoState == AutoState.CLAIM)
                autoState = AutoState.PARK;
            else if (autoState == AutoState.PARK)
                autoState = AutoState.SAMPLE;
            CanSwitchTimer2 = false;
        }
        else if (!gamepad1.right_bumper)
            CanSwitchTimer2 = true;


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
        if (gamepad1.y){
            Auto_Early_Stop = !Auto_Early_Stop;
        }
        sampleDelay = UniversalFunctions.clamp(2, sampleDelay, 20);

        telemetry.addData("Active Timer: ", autoState);
        telemetry.addData("sample dealy: ", sampleDelay);
        telemetry.addData("claim delay: ", claimDelay);
        telemetry.addData("parking delay: ", parkingDelay);
        telemetry.addData("double-sample delay ", doubleSampleDelay);
        telemetry.addData("is extending?", is_aextending);
        telemetry.addData("Crater", crater);
        telemetry.addData ("Can Switch Timer", canSwitchTimer);
        telemetry.addData ("Can Switch Timer2", CanSwitchTimer2);
        telemetry.addData ("Stop Auto after Sample", Auto_Early_Stop);
        telemetry.addData("robotAngle", robotAngle.angle());
        telemetry.update();
    }

    @Override
    public void start() {
        super.start();

        startTime = UniversalFunctions.getTimeInSeconds();
        autoState = AutoState.LAND;
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
                if (Math.abs(sampleVect.x) < 5) {
                    depotYLocation = 57;
                }

                initialPosition = drivetrain.position.y;
                autoState = autoState.FORWARD;
                break;

            case FORWARD:

                drivetrain.updateLocation();
                drivetrain.position.angle = robotAngle.angle();
                drivetrain.maxSpeed = 0.4;

                intaek.articulateDown();

                if (drivetrain.position.y - initialPosition < 2) {
                    drivetrain.setLeftPow(.6);
                    drivetrain.setRightPow(.6);
                } else {
                    autoState = AutoState.SAMPLE;
                }
                break;

            case SAMPLE:
                intaek.setPower(-1);
                maerkrLeft.setPosition(0.4);
                lift.setPower(-1);

                drivetrain.updateLocation();
                drivetrain.maxSpeed = 0.4;
                Vector2 nearDrivingVect = new Vector2(sampleVect.x, sampleVect.y);
                nearDrivingVect.subtract(drivetrain.position.toVector());

                Vector2 drivingVect = new Vector2(sampleVect.x, sampleVect.y);
                drivingVect.x += 0.455 * Math.sin(sampleVect.angle());
                drivingVect.y -= 0.455 * Math.cos(sampleVect.angle());
                drivetrain.driveToPoint(drivingVect.x, drivingVect.y, robotAngle, drivetrain.direction.FOR, 8);

                if (UniversalFunctions.maxAbs(drivetrain.leftFore.getPower(), drivetrain.rightFore.getPower()) < 0.52) {
                    intaek.articulateUp();

                    if (!Auto_Early_Stop) autoState = AutoState.FORWARD_AFTER_SAMPLPE;
                    else autoState = AutoState.FINISHED;
                    poseAfterSample = drivetrain.position.clone();
                    if ((UniversalFunctions.getTimeInSeconds() - startTime > claimDelay) && !Auto_Early_Stop) {
                        autoState = AutoState.FORWARD_AFTER_SAMPLPE;
                        poseAfterSample = drivetrain.position.clone();
                    }
                    else if ((UniversalFunctions.getTimeInSeconds() - startTime > claimDelay) && Auto_Early_Stop) autoState = AutoState.FINISHED;
                }
                break;
            case FORWARD_AFTER_SAMPLPE:
                drivetrain.updateLocation();
                Vector2 temp2 = new Vector2(drivetrain.position.x, drivetrain.position.y);
                temp2.subtract(poseAfterSample.clone().toVector());
                drivetrain.setLeftPow(1);
                drivetrain.setRightPow(1);
                if(temp2.magnitude() > (sampleVect.x < -8 ? 3 : 5)) {
                    autoState = AutoState.TO_THE_DEPOT;
                }
                break;

            case TO_THE_DEPOT:
                intaek.setPower(0);
                intaek.articulateUp();
                drivetrain.updateLocation();
                drivetrain.position.angle = robotAngle.angle();
                drivetrain.maxSpeed = 0.55;

                Vector2 newVect = new Vector2((crater == Crater.RIGHT ? 1 : -1) * 5.5, depotYLocation);
                if (sampleVect.x > 8) {
                    newVect.y = crater == Crater.RIGHT ? 58 : 57;
                } else if (sampleVect.x < -8) {
                    newVect.y = crater == Crater.RIGHT ? 57 : 58;
                }
                drivetrain.driveToPoint(newVect.x, newVect.y, robotAngle, Drivetrain.Direction.FOR, 1.5);
                if (UniversalFunctions.maxAbs(drivetrain.rightFore.getPower(), drivetrain.leftFore.getPower()) < 0.25 || (drivetrain.position.y > 50 && Math.signum(drivetrain.leftFore.getPower()) != Math.signum(drivetrain.rightFore.getPower()))) {
                    if (UniversalFunctions.getTimeInSeconds() - startTime > parkingDelay) {
                        autoState = AutoState.CLAIM;
                    }
                }

                if (UniversalFunctions.getTimeInSeconds() - startTime > 15) {
                    if (UniversalFunctions.getTimeInSeconds() - startTime > sampleDelay) {
                        autoState = AutoState.CLAIM;
                    }
                }
                break;

            case FACE_THE_CRATER:
                drivetrain.updateLocation();
                drivetrain.position.angle = robotAngle.angle();
                drivetrain.maxSpeed = 0.65;
                drivetrain.turnToFace(robotAngle, (crater == Crater.RIGHT ? -1 : 1) * 3 * Math.PI / 4);

                if (Math.abs(drivetrain.leftFore.getPower() / drivetrain.maxSpeed) < 0.9) {
                    autoState = AutoState.CLAIM;
                }
                break;
            case CLAIM:
                // maerkr.setPosition(MARKER_OPEN_POSITION);

                autoState = AutoState.PARK;
                craterVect = new Vector2();
                craterVect.setFromPolar(1, Math.PI / 2 + (crater == Crater.RIGHT ? -1 : 1) * 3 * Math.PI / 4 - (crater == Crater.RIGHT ? -1 : 1) * Math.toRadians(7.5));
                break;

            case PARK:
                telemetry.addData("gyroAngle", robotAngle.angle());
                telemetry.addData("onCrater", onCrater);
                telemetry.addData("y angle", normalizeGyroAngleY());
                drivetrain.updateLocation();
                drivetrain.position.angle = robotAngle.angle();
                drivetrain.turnMult = 2;
                if(drivetrain.leftFore.getPower() - drivetrain.rightFore.getPower() < 0.2){
                    maerkrLeft.setPosition(UniversalConstants.MarkerServoConstants.LEFT_OPEN.getPos());
                }
                if (!onCrater) {
                    if (!onCrater)
                        onCrater = Math.abs(UniversalFunctions.normalizeAngle180(normalizeGyroAngleY())) > ON_CRATER_RIM_THRESHOLD;
                    drivetrain.maxSpeed = 0.75;
                    drivetrain.newFieldCentric(craterVect, robotAngle, 0.00000000000000001);
                } else {
                    drivetrain.stop();
                    aextendo.extendo.setPower(0);
                    autoState = AutoState.FINISHED;
                    if (is_aextending) {
                        //intaek.articulateDown();
                        //intaek.setPower(1);
                    }
                    }
                break;
            case FINISHED:
                break;
        }

        telemetry.addData("position", drivetrain.position.toString());
        telemetry.addData("robot ang: ", Math.toDegrees(robotAngle.angle()));
        telemetry.addData("sampleVect, ", sampleVect);
        telemetry.addData("element position", detector.element);
        telemetry.addData("onCrater", onCrater);
        telemetry.addData("state", autoState);
        telemetry.addData("topPressed", lift.topPressed());
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
        FORWARD_AFTER_SAMPLPE,
        TO_THE_DEPOT,
        CLAIM,
        PARK,
        INTAKE,
        DOUBLE_SAMPLE,
        TO_THE_LANDER,
        DISPENSE,
        TO_THE_CRATER,
        FINISHED,
        FACE_THE_CRATER
    }
}
