package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.AExtendotm;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Intake;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Lift;
import org.firstinspires.ftc.teamcode.Components.Sensors.Cameras.MotoG4;
import org.firstinspires.ftc.teamcode.Components.Sensors.TouchSensor;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;
import org.firstinspires.ftc.teamcode.Vision.Detectors.GoldDetector;
import org.opencv.core.Point3;

import ftc.vision.Detector;

import static org.firstinspires.ftc.teamcode.Universal.UniversalConstants.MS_STUCK_DETECT_INIT_DEFAULT;

public class Autonomous extends WestBot15 {
	public enum AutoSide { CRATER, DEPOT }
	public enum Alliance { RED, BLUE }

	public final static boolean AEXTENDO_IN_AUTO = false;
	public final static int ON_CRATER_RIM_THRESHOLD = 15;

	public static double startTime;
	public static boolean usingVectorFields = false;
	public static double sampleDelay, claimDelay, craterDelay = 0;

	private static final Point3 PHONE_POSITION = new Point3(0, 9.20759410753813, 14.92590572034875);
	private GoldDetector detector;
	private TouchSensor liftTopTouch, liftBottomTouch;

	@Override
	public void init() {
		msStuckDetectInit = MS_STUCK_DETECT_INIT_DEFAULT;
		super.init();

		detector = new GoldDetector();

		motoG4 = new MotoG4();
		motoG4.setLocationAndOrientation(PHONE_POSITION, new Point3(-Math.toRadians(37), -Math.PI / 2, 0));

		drivetrain.initMotors(hardwareMap);
		drivetrain.position = new Pose();

		startAngleY = getGyroAngleY();

		drivetrain.position = new Pose();
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

		liftTopTouch = new TouchSensor();
		liftBottomTouch = new TouchSensor();
		liftTopTouch.init(hardwareMap, "top");
		liftBottomTouch.init(hardwareMap, "bot");
	}

	@Override
	public void start() {
		super.start();
		drivetrain.position = new Pose(0, 0, Math.PI / 2);
		// startTime is literally the time from start.
		startTime = UniversalFunctions.getTimeInSeconds();
	}

	public void displayToDrivers() {
		detector.opState = Detector.OperatingState.TUNING;
		FtcRobotControllerActivity.frameGrabber.detector = detector;
	}

	public void lower() {
		lift.setPower(-1);
		if (lift.topPressed()) { lift.liftMotor.setPower(0); }
	}
}
