package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by Frank Portman on 3/31/2018.
 */

public abstract class MecBotTemplate extends OpMode{
    DcMotor lf, lr, rf, rr;
    double startAngle;
    BNO055IMU imu;
    GyroAngles gyroangles;
    Orientation angles;
    public static double SPEED = 1.0;
    private static final DcMotor.Direction
            LDIR = DcMotorSimple.Direction.FORWARD,
            RDIR = DcMotorSimple.Direction.REVERSE;
    private static final double BRAKE_POW = 0.01;

    public void init(){
        lf = hardwareMap.dcMotor.get("lf");
        lr = hardwareMap.dcMotor.get("lr");
        rf = hardwareMap.dcMotor.get("rf");
        rr = hardwareMap.dcMotor.get("rr");
        lf.setDirection(LDIR);
        lr.setDirection(LDIR);
        rf.setDirection(RDIR);
        rr.setDirection(RDIR);

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
    protected void refreshMotors(double I, double II, double III, double IV, boolean brake){
        if(brake)
            brake();
        lf.setPower(SPEED * II);
        lr.setPower(SPEED * III);
        rf.setPower(SPEED * I);
        rr.setPower(SPEED * IV);
    }
    protected void normalize(double I, double II, double III, double IV){
        double max = Math.max(Math.max(Math.abs(I), Math.abs(II)), Math.max(Math.abs(III), Math.abs(IV)));
        if(max > 1){
            I /= max;
            II /= max;
            III /= max;
            IV /= max;
        }
        refreshMotors(I, II, III, IV, true);
    }
    protected void brake(){
        if(rf.getPower() == 0 && lf.getPower() == 0 && lr.getPower() == 0 && rr.getPower() == 0){
            rf.setPower(-BRAKE_POW);
            rr.setPower(BRAKE_POW);
            lr.setPower(-BRAKE_POW);
            lf.setPower(BRAKE_POW);
        }
    }
    protected enum ControlState{
        ARCADE,
        FIELD_CENTRIC,
    }
    protected double normalizeGamepadAngle(double angle){
        return UniversalFunctions.normalizeAngle(getGamepadAngle(), angle);
    }
    protected double getGamepadAngle(){
        double x = gamepad1.right_stick_x;
        double y = gamepad1.right_stick_y;
        return (UniversalFunctions.round(y) / 2 + 0.5 * Math.abs(y)) * 180 + Math.toDegrees(Math.acos(x / (Math.sqrt(x * x + y * y))));
    }
    protected double getGyroAngle(){
        return gyroangles.refreshGyroAngles(angles);
        //return gyroSensor.rawX();
    }
    protected double normalizeGyroAngle(double angle){
        angle -= startAngle;
        double a2 = Math.abs(angle) %  360;
        if(Math.abs(angle) != angle){
            return 360 - a2;
        }
        return a2;
    }
}
