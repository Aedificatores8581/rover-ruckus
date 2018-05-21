package org.firstinspires.ftc.teamcode.robotTemplates;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.robotUniversal.GyroAngles;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.robotUniversal.GyroAngles;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;

/**
 * Created by Frank Portman on 5/21/2018
 */

public abstract class Robot extends OpMode {
    GyroAngles gyroangles;
    Orientation angles;
    BNO055IMU imu;
    double startAngle;
    public void init() {
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
        return gyroangles.refreshGyroAngles(imu.getAngularOrientation(AxesReference.INTRINSIC, GyroAngles.ORDER, GyroAngles.UNIT));
        //return gyroSensor.rawX();
    }
    public void setStartAngle(){
        startAngle = getGyroAngle();
    }
    public double normalizeGyroAngle(){
        double angle = getGyroAngle();
        angle -= startAngle;
        double a2 = Math.abs(angle) %  360;
        if(Math.abs(angle) != angle){
            return 360 - a2;
        }
        return a2;
    }
    public double normalizeGamepadAngleL(double angle){
        return UniversalFunctions.normalizeAngle(getGamepadAngleL(), angle);
    }
    public double getGamepadAngleL(){
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
    public double normalizeGamepadAngleR(double angle){
        return UniversalFunctions.normalizeAngle(getGamepadAngleL(), angle);
    }
    public double getGamepadAngleR(){
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

}
