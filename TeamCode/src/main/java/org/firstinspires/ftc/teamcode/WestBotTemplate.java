package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by Frank Portman on 3/31/2018.
 */

public abstract class WestBotTemplate extends OpMode{
    DcMotor lf, lr, rf, rr;
    public static final DcMotor.Direction LDIR = DcMotorSimple.Direction.FORWARD, RDIR = DcMotorSimple.Direction.REVERSE;
    public static final double TURN_MULT = 0.75;
    public static double SPEED = 1.0;
    BNO055IMU imu;
    GyroAngles gyroangles;
    Orientation angles;
    double startAngle;
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
    public double getGyroAngle(){
        return gyroangles.refreshGyroAngles(angles);
        //return gyroSensor.rawX();
    }
    public double normalizeGyroAngle(double angle){
        angle -= startAngle;
        double a2 = Math.abs(angle) %  360;
        if(Math.abs(angle) != angle){
            return 360 - a2;
        }
        return a2;
    }
    protected double normalizeGamepadAngle(double angle){
        return UniversalFunctions.normalizeAngle(getGamepadAngle(), angle);
    }
    protected double getGamepadAngle(){
        double x = gamepad1.right_stick_x;
        double y = gamepad1.right_stick_y;
        return (UniversalFunctions.round(y) / 2 + 0.5 * Math.abs(y)) * 180 + Math.toDegrees(Math.acos(x / (Math.sqrt(x * x + y * y))));
    }
    public void setLeftPow(double pow){
        lf.setPower(SPEED * pow);
        lr.setPower(SPEED * pow);
    }
    public void setRightPow(double pow){
        rf.setPower(SPEED * pow);
        rr.setPower(SPEED * pow);
    }
    public boolean brake(int dir){
        if(lf.getPower() == 0 && rf.getPower() == 0 && rr.getPower() == 0 &&rf.getPower() == 0) {
            lf.setPower(0.05 * dir);
            rf.setPower(0.05 * dir);
            lr.setPower(-0.05 * dir);
            rr.setPower(-0.05 * dir);
            return true;
        }
        return false;
    }
    public enum CONTROL_STATE{
        ARCADE,
        TANK,
        FIELD_CENTRIC,
    }
    public enum DRIVE_MODE{
        TANK,
        ELSE,
    }
}
