package org.firstinspires.ftc.teamcode;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.Range;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Locale;

/**
 * Created by Frank Portman on 3/31/2018.
 */

public abstract class SwerveBotTemplate extends OpMode{

    DcMotor lf, lr, rf, rr;
    Servo lfswervo, lrswervo, rfswervo, rrswervo;
    BNO055IMU imu;
    GyroSensor gyroSensor;
    double startAngle;
    double forwardAngle;
    double swervoRotationRatio;
    private static final DcMotor.Direction
            MDIR = DcMotorSimple.Direction.FORWARD,
            SDIR = DcMotorSimple.Direction.FORWARD;
    private static final double
            BRAKE_POW = 0.01;
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
        swervoRotationRatio = 0;
    }
    public void start(){
        startAngle = gyroSensor.rawX();
    }
    public double normalizeGyroAngle(double angle){
        angle += startAngle;
        double a2 = Math.abs(angle) %  360;
         if(Math.abs(angle) != angle){
             return 360 - a2;
         }
         return a2;
    }
    public double normalizeAngle(double angle){
        double a2 = Math.abs(angle) %  360;
        if(Math.abs(angle) != angle){
            return 360 - a2;
        }
        return a2;
    }
    public double normalizeAngle(double angle, double newStartAngle){
        angle += newStartAngle;
        double a2 = Math.abs(angle) %  360;
        if(Math.abs(angle) != angle){
            return 360 - a2;
        }
        return a2;
    }
    protected enum Direction{
        LEFT, FORWARD, RIGHT, BACKWARD;
    }
    protected double getSwervoRotation(double desiredAngle, double currentAngle) {
        return normalizeAngle(desiredAngle, currentAngle) / 360 * swervoRotationRatio;
    }
    protected double getSwervoAngle(double swervoRotation){
        return normalizeAngle(swervoRotation / swervoRotationRatio * 360, 0);
    }
    protected double getGamepadAngle(){
        double x = gamepad1.right_stick_x;
        double y = gamepad1.right_stick_y;
        return (round(y + 0.0000000000000001) / 2 + 0.5) * 180 + Math.toDegrees(Math.acos(x / (Math.sqrt(x * x + y * y))));
    }
    //I misuse the word "normalize". Don't change it.
    protected double normalizeGamepadAngle(double swervoAngle){
        return normalizeAngle(getGamepadAngle(), getSwervoAngle(swervoAngle));
    }
    protected double round(double d){
        if(d < 0){
            return Math.floor(d);
        }
        return Math.ceil(d);
    }
}
