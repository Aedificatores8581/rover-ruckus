package org.firstinspires.ftc.teamcode.robotTemplates;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.robotUniversal.GyroAngles;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;

/**
 * Created by Frank Portman on 3/31/2018.
 */
//
public abstract class SwerveBotTemplate extends OpMode{

    public DcMotor lf, lr, rf, rr, cm;
    public Servo lfswervo, lrswervo, rfswervo, rrswervo, ballShift;
    public BNO055IMU imu;
    public GyroAngles gyroangles;
    public Orientation angles;
    public double startAngle;
    public double forwardAngle;
    public final double swervoRotationRatio = 0;
    public final double ENCODER_RATIO = 0; //degrees per encoder tick
    public static double SPEED = 1.0;
    private static final DcMotor.Direction MDIR = DcMotorSimple.Direction.FORWARD;
    private static final double BRAKE_POW = 0.01;
    private final DcMotor.Direction CMDIR = DcMotorSimple.Direction.FORWARD;
    public void init(){
        forwardAngle = 0;
        lf = hardwareMap.dcMotor.get("lf");
        lr = hardwareMap.dcMotor.get("lr");
        rf = hardwareMap.dcMotor.get("rf");
        rr = hardwareMap.dcMotor.get("rr");
        rfswervo = hardwareMap.servo.get("trf");
        lrswervo = hardwareMap.servo.get("tlr");
        lfswervo = hardwareMap.servo.get("tlf");
        rrswervo = hardwareMap.servo.get("trr");
        ballShift = hardwareMap.servo.get("bs");
        cm = hardwareMap.dcMotor.get("cm");
        cm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        cm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        cm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lf.setDirection(MDIR);
        lr.setDirection(MDIR);
        rf.setDirection(MDIR);
        rr.setDirection(MDIR);
        cm.setDirection(CMDIR);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json";
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, GyroAngles.ORDER, GyroAngles.UNIT);
        gyroangles = new GyroAngles(angles);
    }
    public void start(){
        startAngle = getGyroAngle();
    }
    //Returns angle read by the gyro sensor
    protected double getGyroAngle(){
        return gyroangles.refreshGyroAngles(angles);
    }
    //Normalizes the bot's angle in relation to the start angle
    protected double normalizeGyroAngle(){
        double angle = startAngle + getGyroAngle();
        double a2 = Math.abs(angle) %  360;
        if(Math.abs(angle) != angle){
            return 360 - a2;
        }
        return a2;
    }
    //Calculates the number of swervo rotations necessary to get to a specified angle from a specified angle
    protected double getSwervoRotation(double desiredAngle, double currentAngle) {
        return UniversalFunctions.normalizeAngle180(desiredAngle, currentAngle) / 360 * swervoRotationRatio;
    }
    //Calculates the number of swervo rotations necessary to get to a specified angle
    protected double getSwervoRotation(double currentAngle) {
        return currentAngle / 360 * swervoRotationRatio;
    }

    //Calculates the number change in the angle of the bot after a specified number of swervo rotations
    protected double getSwervoAngle(double swervoRotation){
        return UniversalFunctions.normalizeAngle(swervoRotation / swervoRotationRatio * 360, 0);
    }
    //normalizes getSwervoAngle from -180 degrees to 180 degrees
    protected double getSwervoAngle180(double swervoRotation){
        return UniversalFunctions.normalizeAngle180(swervoRotation / swervoRotationRatio * 360, 0);
    }
    //returns the angle coresponding to the spherical co-ordinates of the gamepad's left stick
    protected double getGamepadAngleL(){
        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        return (UniversalFunctions.round(y) / 2 + 0.5 * Math.abs(y)) * 180 + Math.toDegrees(Math.acos(x / (Math.sqrt(x * x + y * y))));
    }
    //Returns  the angle of the gamepad's left stick in relation to the angle parameter
    protected double normalizeGamepadAngleL(double angle){
        return UniversalFunctions.normalizeAngle(getGamepadAngleL(), angle);
    }
    //returns the angle coresponding to the spherical co-ordinates of the gamepad's right stick
    protected double getGamepadAngleR(){
        double x = gamepad1.right_stick_x;
        double y = gamepad1.right_stick_y;
        return (UniversalFunctions.round(y) / 2 + 0.5 * Math.abs(y)) * 180 + Math.toDegrees(Math.acos(x / (Math.sqrt(x * x + y * y))));
    }
    //Returns  the angle of the gamepad's left stick in relation to the angle parameter
    protected double normalizeGamepadAngleR(double swervoAngle){
        return UniversalFunctions.normalizeAngle(getGamepadAngleR(), normalizeGyroAngle());
    }
    //sets motor power within the specified parameters
    protected void refreshMotors(double I, double II, double III, double IV, boolean brake){
        if(brake)
            brake();
        lf.setPower(SPEED * II);
        lr.setPower(SPEED * III);
        rf.setPower(SPEED * I);
        rr.setPower(SPEED * IV);
    }
    //Gives the motors holding power
    protected void brake(){
        if(rf.getPower() == 0 && lf.getPower() == 0 && lr.getPower() == 0 && rr.getPower() == 0){
            rf.setPower(-BRAKE_POW);
            rr.setPower(BRAKE_POW);
            lr.setPower(-BRAKE_POW);
            lf.setPower(BRAKE_POW);
        }
    }
    //returns the swervo position needed to achieve the next angle if rotated counter-clockwise
    protected double nextRotation(double angle){
        return UniversalFunctions.normalizeAngle(angle + swervoRotationRatio - getSwervoAngle(getSwervoRotation(angle) % 1));
    }

    //returns the swervo position needed to achieve the next angle if rotated clockwise
    protected double prevRotation(double angle){
        return UniversalFunctions.normalizeAngle(angle - getSwervoAngle(getSwervoRotation(angle) % 1));
    }
    //specifies the turn multiplier
    protected enum TurnMode{
        ARCADE,
        FIELD_CENTRIC;
    }

    //USED FOR CRABBOT
    //returns the number of encoder ticks coresponding to specified angle
    protected double getEncoderRotation(double angle){
        return angle / 360 * ENCODER_RATIO;
    }
    //returns the angle of rotation coresponding to a specified number of encoder ticks
    protected double getMotorAngle(double enc){
        return UniversalFunctions.normalizeAngle(enc * 360 / ENCODER_RATIO);
    }
    //normalizes getMotorAngle from -180 to 180 degrees
    protected double getMotorAngle180(double enc){
        return UniversalFunctions.normalizeAngle180(enc * 360 / ENCODER_RATIO);
    }
    //returns the number of encoder ticks coresponding to specified change in angle
    protected double getEncoderRotation(double desiredAngle, double currentAngle) {
        return UniversalFunctions.normalizeAngle180(desiredAngle, currentAngle) / 360 * ENCODER_RATIO;
    }
    //switches the control system from swerve to non-holonomic
    protected enum DriveMode{
        SWERVE,
        TANK;
    }
    //switches from the act of switching DriveModes
    protected enum TankDriveMode{
        SHIFT,
        TANK;
    }
}
