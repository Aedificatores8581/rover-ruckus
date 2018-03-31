package org.firstinspires.ftc.teamcode;


import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.sun.tools.javac.comp.Todo;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Locale;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by fgpor on 3/22/2018.
 */
@TeleOp(name = "locate Single Thread", group = "no.")
public class LocateSingleThread extends OpMode{
    Point point = new Point();
    int n = 0;
        BNO055IMU imu;
        Orientation theta;
        Acceleration acc, a;
        double A, ti, tf;
        private final double C = 1;
        final double mult = 0.8;
        boolean trig = false;
        int b = 1;
        int d = 0;
        double thi;
        DcMotor leftFore, leftRear, rightFore, rightRear;
        @Override
        public void init() {
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.mode = BNO055IMU.SensorMode.IMU;
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "AdafruitIMUCalibration.json";
            parameters.loggingEnabled = true;
            parameters.loggingTag = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
            acc = new Acceleration();
            imu = hardwareMap.get(BNO055IMU.class, "imu");
            imu.initialize(parameters);
            leftFore = hardwareMap.dcMotor.get("lfm"); // port 2
            leftRear = hardwareMap.dcMotor.get("lrm"); // port 3
            rightFore = hardwareMap.dcMotor.get("rfm"); // port 0
            rightRear = hardwareMap.dcMotor.get("rrm"); // port 1
            theta = imu.getAngularOrientation();
            a = imu.getLinearAcceleration();
        }
        @Override
        public void start() {
            theta = imu.getAngularOrientation();
            a = imu.getLinearAcceleration();
            ti = System.currentTimeMillis();
        }
        @Override
        public void loop() {
            tf = System.currentTimeMillis();
            A = Math.sqrt(Math.pow(acc.xAccel, 2) + Math.pow(acc.yAccel, 2) + Math.pow(acc.zAccel, 2));
            thi = (double) theta.thirdAngle;
            point.addX(C * A * Math.pow((tf - ti), 2) * Math.cos(thi) / 2 - b * d * Math.cos(180 - thi));
            point.addY(C * A * Math.pow((tf - ti), 2) * Math.sin(thi) / 2 - b * d * Math.sin(180 - thi));
            ti = tf;
            if(Ltriggered()) {
                setLeftPow(gamepad1.left_stick_y + gamepad1.right_stick_x * mult);
                trig = true;
            }
            else
                trig = false;
            if(Rtriggered()) {
                setRightPow(gamepad1.left_stick_y - gamepad1.right_stick_x * mult);
                trig = true;
            }
            else
                trig = false;
            if(trig = false){
                setLeftPow(0);
                setRightPow(0);
                trig = true;
            }
        }
        public void setLeftPow(double pow) {
            leftRear.setPower(pow);
            leftFore.setPower(pow);
        }
        public void setRightPow(double pow) {
            rightRear.setPower(pow);
            rightFore.setPower(pow);
        }
        public boolean Ltriggered(){
            if(Math.abs(gamepad1.left_stick_y) >= 0.1)
                return true;
            return false;
        }
        public boolean Rtriggered(){
        if(Math.abs(gamepad1.left_stick_x) >= 0.1)
            return true;
        return false;
    }
}