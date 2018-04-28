package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by Frank Portman on 4/8/2018.
 */
//
public abstract class SensorBotWestTemplate extends OpMode{
    DcMotor left, right;
    Servo serv1, serv2;
    public static final DcMotor.Direction LDIR = DcMotorSimple.Direction.FORWARD, RDIR = DcMotorSimple.Direction.REVERSE;
    public static final double TURN_MULT = 0.75;
    public static double SPEED = 1.0;
    BNO055IMU imu;
    GyroAngles gyroangles;
    Orientation angles;
    double startAngle;
    public void init(){
        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");
        //serv1 = hardwareMap.servo.get("s1");
        //serv2 = hardwareMap.servo.get("s2");
        left.setDirection(LDIR);
        right.setDirection(RDIR);

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
    protected double getGyroAngle(){
        return gyroangles.refreshGyroAngles(imu.getAngularOrientation(AxesReference.INTRINSIC, GyroAngles.ORDER, GyroAngles.UNIT));
        //return gyroSensor.rawX();
    }
    protected void setStartAngle(){
        startAngle = getGyroAngle();
    }
    protected double normalizeGyroAngle(double angle){
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

    protected enum TurnDir{
        FOR, BACK;
    }
    protected double getGamepadAngle(){
        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        if(y < 0)
            return Math.toDegrees(Math.acos(x / (Math.sqrt(x * x + y * y))));
        else if(y > 0)
            return -Math.toDegrees(Math.acos(x / (Math.sqrt(x * x + y * y))));
        else if(x < 0)
            return 180;
        else
            return 0;
       // return (UniversalFunctions.round(y) / 2.0 + 0.5 * Math.abs(UniversalFunctions.round(y))) * 180 + Math.toDegrees(Math.acos(x / (Math.sqrt(x * x + y * y))));
    }
    protected void setLeftPow(double pow){
        left.setPower(SPEED * pow);
    }
    protected void setRightPow(double pow){
        right.setPower(SPEED * pow);
    }
    protected boolean brake(int dir){
        if(left.getPower() == 0 && right.getPower() == 0) {
            setLeftPow(0.05 * dir);
            setRightPow(0.05 * dir);
            return true;
        }
        return false;
    }
    protected enum FCTurnState{
        SMOOTH,
        FAST;
    }
    protected enum ControlState{
        ARCADE,
        TANK,
        FIELD_CENTRIC,
    }
}
