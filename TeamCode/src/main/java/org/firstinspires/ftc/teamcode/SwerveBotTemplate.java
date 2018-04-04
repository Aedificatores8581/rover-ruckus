package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by Frank Portman on 3/31/2018.
 */

public abstract class SwerveBotTemplate extends OpMode{

    DcMotor lf, lr, rf, rr;
    Servo lfswervo, lrswervo, rfswervo, rrswervo;
    BNO055IMU imu;
    GyroAngles gyroangles;
    Orientation angles;
    double startAngle;
    double forwardAngle;
    double swervoRotationRatio;

    private static final DcMotor.Direction MDIR = DcMotorSimple.Direction.FORWARD;
    private static final double BRAKE_POW = 0.01;
    public void init(){
        forwardAngle = 0;
        swervoRotationRatio = 0;

        lf = hardwareMap.dcMotor.get("lf");
        lr = hardwareMap.dcMotor.get("lr");
        rf = hardwareMap.dcMotor.get("rf");
        rr = hardwareMap.dcMotor.get("rr");
        rfswervo = hardwareMap.servo.get("trf");
        lrswervo = hardwareMap.servo.get("tlr");
        lfswervo = hardwareMap.servo.get("tlf");
        rrswervo = hardwareMap.servo.get("trr");

        lf.setDirection(MDIR);
        lr.setDirection(MDIR);
        rf.setDirection(MDIR);
        rr.setDirection(MDIR);
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
    public double getGyroAngle(){
        return gyroangles.refreshGyroAngles(angles);
        //return gyroSensor.rawX();
    }
    public double normalizeGyroAngle(double angle){
        angle += startAngle;
        double a2 = Math.abs(angle) %  360;
         if(Math.abs(angle) != angle){
             return 360 - a2;
         }
         return a2;
    }

    protected double getSwervoRotation(double desiredAngle, double currentAngle) {
        return UniversalFunctions.normalizeAngle(desiredAngle, currentAngle) / 360 * swervoRotationRatio;
    }
    protected double getSwervoAngle(double swervoRotation){
        return UniversalFunctions.normalizeAngle(swervoRotation / swervoRotationRatio * 360, 0);
    }
    protected double getGamepadAngle(){
        double x = gamepad1.right_stick_x;
        double y = gamepad1.right_stick_y;
        return (UniversalFunctions.round(y) / 2 + 0.5 * Math.abs(y)) * 180 + Math.toDegrees(Math.acos(x / (Math.sqrt(x * x + y * y))));
    }
    //I misuse the word "normalize". Don't change it.
    protected double normalizeGamepadAngle(double swervoAngle){
        return UniversalFunctions.normalizeAngle(getGamepadAngle(), getSwervoAngle(swervoAngle));
    }
    public void refreshMotors(double I, double II, double III, double IV, boolean brake){
        if(brake)
            brake();
        lf.setPower(II);
        lr.setPower(III);
        rf.setPower(I);
        rr.setPower(IV);
    }
    public void brake(){
        if(rf.getPower() == 0 && lf.getPower() == 0 && lr.getPower() == 0 && rr.getPower() == 0){
            rf.setPower(-BRAKE_POW);
            rr.setPower(BRAKE_POW);
            lr.setPower(-BRAKE_POW);
            lf.setPower(BRAKE_POW);
        }
    }
}
