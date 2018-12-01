package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

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
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;
import org.firstinspires.ftc.teamcode.Vision.Detectors.BlockDetector;
import org.opencv.core.Point;

import ftc.vision.Detector;

@Autonomous(name = "crater sans comments", group = "competition autonomous")
public class CraterAutoSansComments extends WestBot15 {
    private final double PARK_MOTOR_SPEED = 0.4;

    BlockDetector detector;

    Point newNewPoint = new Point();
    private Vector2 sampleVect = new Vector2();
    private Pose robotPose = new Pose();

    private double prevLeft, prevRight = 0;
    private double hardNewY;
    private double rightEncPosition, leftEncPosition;
    private double startTime = 0;

    private boolean onCrater = false;

    private GyroAngles gyroAngles;
    private Orientation angle;

    private final static int ON_CRATER_RIM_THRESHOLD = 60;
    AutoState autoState = AutoState.LOWER;

    public void init() {
        drivetrain.position = new Pose();
        detector = new BlockDetector();
        angle = new Orientation();
        gyroAngles = new GyroAngles(angle);

        super.init();

        msStuckDetectInit = UniversalConstants.MS_STUCK_DETECT_INIT_DEFAULT;

        activateGamepad1();

        detector.opState = Detector.OperatingState.TUNING;
        FtcRobotControllerActivity.frameGrabber.detector = detector;

        normalizeGyroAngle();

        drivetrain.controlState = TankDT.ControlState.FIELD_CENTRIC;
        drivetrain.direction = TankDT.Direction.BACK;

        drivetrain.leftFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivetrain.leftFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        drivetrain.leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivetrain.leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        drivetrain.rightFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivetrain.rightFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        drivetrain.rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivetrain.rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void start(){
        super.start();

        drivetrain.position = new Pose(0, 0, 0);
        startTime = UniversalFunctions.getTimeInSeconds();
    }

    public void loop(){
        switch (autoState) {
            case LOWER: break;
            case SAMPLE:
                updateLocation(drivetrain.averageLeftEncoders() - prevLeft, drivetrain.averageRightEncoders() - prevRight);

                prevLeft = drivetrain.averageLeftEncoders();
                prevRight = drivetrain.averageRightEncoders();

                setRobotAngle();

                drivetrain.maxSpeed = 0.2;


                Vector2 temp = new Vector2(-detector.element.x, detector.element.y);
                temp.x += 640 / 2;
                temp.y -= 480 / 2;

                double vertAng = temp.y / 480 * motoG4.rearCamera.horizontalAngleOfView();
                double horiAng = temp.x / 640 * motoG4.rearCamera.verticalAngleOfView();

                double newY = (motoG4.getLocation().z - 1) / Math.tan(-vertAng - 0.364773814);
                double newX = newY * Math.tan(horiAng);

                newY *= -1;

            case CLAIM:


                drivetrain.teleOpLoop(new Vector2(84.85, -16.1), new Vector2(), robotAngle);
                break;

            case PARK:
                if (Math.abs(normalizeGyroAngleY()) > ON_CRATER_RIM_THRESHOLD) {
                    onCrater = true;
                } else {
                    onCrater = false;
                }

                if (!onCrater) {
                    drivetrain.setRightPow(PARK_MOTOR_SPEED);
                    drivetrain.setLeftPow(PARK_MOTOR_SPEED);
                } else {
                    drivetrain.setRightPow(0.0);
                    drivetrain.setLeftPow(0.0);
                }

                drivetrain.updateEncoders();

                telemetry.addData("onCrater?", onCrater);

                drivetrain.teleOpLoop(new Vector2(Math.sqrt(2)/2, Math.sqrt(2) / 2), new Vector2(), 0);
                break;
        }

        telemetry.addData("robot ang: ", Math.toDegrees(robotAngle.angle()));
        telemetry.addData("left pow", drivetrain.leftFore.getPower());
        telemetry.addData("sampleVect, ", sampleVect);
        telemetry.addData("desired distance, ", drivetrain.ENC_PER_INCH * hardNewY);
        telemetry.addData("distance traveled, ", drivetrain.averageLeftEncoders() - leftEncPosition );
        telemetry.addData("element position", detector.element);
    }

    public void stop() {
        super.stop();
        detector.isInitialized = false;
    }

    public void updateLocation(double leftChange, double rightChange) {
        leftChange = leftChange / drivetrain.ENC_PER_INCH;
        rightChange = rightChange / drivetrain.ENC_PER_INCH;

        double angle = 0;
        Vector2 turnVector = new Vector2();

        if (rightChange == leftChange) {
            turnVector.setFromPolar(rightChange, robotPose.angle);
        } else {
            double radius = drivetrain.DISTANCE_BETWEEN_WHEELS / 2 * (leftChange + rightChange) / (rightChange - leftChange);

            angle = (rightChange - leftChange) / (drivetrain.DISTANCE_BETWEEN_WHEELS);
            radius = Math.abs(radius);
            turnVector.setFromPolar(radius, angle);
            turnVector.setFromPolar(radius - turnVector.x, angle);

            if (Math.min(leftChange, rightChange) == -UniversalFunctions.maxAbs(leftChange, rightChange)) {
                turnVector.x *= -1;
            }
        }

        turnVector.rotate(robotPose.angle);

        robotPose.add(turnVector);
        robotPose.angle += angle;
    }
}
