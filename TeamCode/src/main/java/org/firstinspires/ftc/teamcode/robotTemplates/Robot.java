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
import org.firstinspires.ftc.teamcode.robotUniversal.Vector2;

/**
 * Created by Frank Portman on 5/21/2018
 */

public abstract class Robot extends OpMode {
    GyroAngles gyroangles;
    Orientation angles;
    BNO055IMU imu;
    double startAngle;
    boolean usingIMU;
    Vector2 lStick1, rStick1, lStick2, rStick2;
    Vector2 robotAngle;
    public Robot(boolean imu){
        usingIMU = imu;
    }
    public void init() {
        if(usingIMU) {
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
            robotAngle = new Vector2();
        }
    }
    public void start(){
        if(usingIMU) {
            startAngle = getGyroAngle();
        }
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
    public void setRobotAngle(){
        robotAngle.setFromPolar(1, Math.toRadians(normalizeGyroAngle()));
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
    //instantiates the vectors representing the first gamepad's sticks
    public void activateGamepad1(){
        lStick1 = new Vector2();
        rStick1 = new Vector2();
    }
    //instantiates the vectors representing the second gamepad's sticks
    public void activateGamepad2(){
        lStick2 = new Vector2();
        rStick2 = new Vector2();
    }
    //Updates the vectors representing the left stick of the first gamepad
    public void updateLeftStick1(){
        lStick1.x = gamepad1.left_stick_x;
        lStick1.y = gamepad1.left_stick_y;
    }
    //Updates the vectors representing the right stick of the first gamepad
    public void updateRightStick1() {
        rStick1.x = gamepad1.right_stick_x;
        rStick1.y = gamepad1.right_stick_y;
    }
    //Updates the vectors representing the left stick of the second gamepad
    public void updateLeftStick2(){
        lStick2.x = gamepad2.left_stick_x;
        lStick2.y = gamepad2.left_stick_y;
    }
    //Updates the vectors representing the right stick of the second gamepad
    public void updateRightStick2(){
        rStick2.x = gamepad2.right_stick_x;
        rStick2.y = gamepad2.right_stick_y;
    }
    //Updates the vectors representing the sticks of the second gamepad
    public void updateGamepad2(){
        updateLeftStick2();
        updateRightStick2();
    }
    //Updates the vectors representing the sticks of the first gamepad
    public void updateGamepad1(){
        updateLeftStick1();
        updateRightStick1();
    }
    //Updates the vectors representing the sticks of the gamepads
    public void updateGamepads(){
        updateGamepad1();
        updateGamepad2();
    }

}
