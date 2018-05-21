package org.firstinspires.ftc.teamcode.robotTemplates;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.robotUniversal.GyroAngles;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;

/**
 * Created by Frank Portman on 3/31/2018.
 */
//
public abstract class WestBotTemplate extends OpMode{
    DcMotor lf, lr, rf, rr;
    public static final DcMotor.Direction LDIR = DcMotorSimple.Direction.FORWARD, RDIR = DcMotorSimple.Direction.REVERSE;
    public static final double TURN_MULT = 0.75;
    public static double SPEED = 0.5;
    BNO055IMU imu;
    GyroAngles gyroangles;
    Orientation angles;
    public double startAngle;
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
    //Returns angle read by the gyro sensor
    public double getGyroAngle(){
        return gyroangles.refreshGyroAngles(imu.getAngularOrientation(AxesReference.INTRINSIC, GyroAngles.ORDER, GyroAngles.UNIT));
        //return gyroSensor.rawX();
    }
    //Normalizes the bot's angle in relation to the start angle
    public double normalizeGyroAngle(){
        double angle = getGyroAngle();
        angle -= startAngle;
        double a2 = Math.abs(angle) %  360;
        if(Math.abs(angle) != angle){
            return 360 - a2;
        }
        return a2;
    }
    //Returns  the angle of the gamepad's left stick in relation to the angle parameter
    public double normalizeGamepadAngle(double angle){
        return UniversalFunctions.normalizeAngle(getGamepadAngle(), angle);
    }
    //returns the angle coresponding to the spherical co-ordinates of the gamepad's left stick
    public double getGamepadAngle(){
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
    }
    //sets the power of the left motors
    public void setLeftPow(double pow){
        lf.setPower(SPEED * pow);
        lr.setPower(SPEED * pow);
    }
    // sets the power of the right motors
    public void setRightPow(double pow){
        rf.setPower(SPEED * pow);
        rr.setPower(SPEED * pow);
    }
    //sets the bot's starting angle to its current angle
    public void setStartAngle(){
        startAngle = getGyroAngle();
    }
    //Gives the motors holding power
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
    //Control systems for the robot
    public enum ControlState{
        ARCADE,
        TANK,
        FIELD_CENTRIC,
    }
    //Used in field-centric mode to determine the robot's direction
    public enum TurnDir{
        FOR, BACK;
    }

    public enum FCTurnState{
        SMOOTH,
        FAST;
    }
}
