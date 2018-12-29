package org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Components.Sensors.MotorEncoder;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

/**
 * Created by Frank Portman on 5/21/2018
 */

public class WestCoast15 extends TankDT {
    public DcMotor rightFore, leftFore, leftRear, rightRear;
    public MotorEncoder rfEncoder, lfEncoder, lrEncoder, rrEncoder;
    public DcMotor.ZeroPowerBehavior zeroPowerBehavior;
    private final double UNIQUE_ENC_PER_INCH = 70 / Math.PI;
    //TODO: Find this value

    public WestCoast15() {
        ENC_PER_INCH = UNIQUE_ENC_PER_INCH;
        DISTANCE_BETWEEN_WHEELS = 391.60085 / 25.4;
        zeroPowerBehavior = DcMotor.ZeroPowerBehavior.FLOAT;
        maxSpeed = 1;
        leftMotors = new DcMotor[]{leftFore, leftRear};
        rightMotors = new DcMotor[]{rightFore, rightRear};
    }

    public WestCoast15(DcMotor.ZeroPowerBehavior zeroPowBehavior, double speed) {
        ENC_PER_INCH = UNIQUE_ENC_PER_INCH;
        DISTANCE_BETWEEN_WHEELS = 391.60085 / 25.4;
        zeroPowerBehavior = zeroPowBehavior;
        maxSpeed = speed;

        leftMotors = new DcMotor[]{leftFore, leftRear};
        rightMotors = new DcMotor[]{rightFore, rightRear};
    }

    public void setLeftPow(double pow) {
        if(pow >= 1)
            pow = 1;
        if(pow <= -1)
            pow = -1;
        leftFore.setPower(pow * maxSpeed);
        leftRear.setPower(pow * maxSpeed);
        leftPow = pow;
    }

    public void setRightPow(double pow) {
        if(pow >= 1)
            pow = 1;
        if(pow <= -1)
            pow = -1;
        rightFore.setPower(pow * maxSpeed);
        rightRear.setPower(pow * maxSpeed);
        rightPow = pow;
    }

    public void initMotors(HardwareMap map) {
        rightFore = map.dcMotor.get("rf");
        leftFore = map.dcMotor.get("lf");
        leftRear = map.dcMotor.get("la");
        rightRear = map.dcMotor.get("ra");

        //leftFore.setZeroPowerBehavior(zeroPowerBehavior);
        //leftRear.setZeroPowerBehavior(zeroPowerBehavior);
        //rightFore.setZeroPowerBehavior(zeroPowerBehavior);
        //rightRear.setZeroPowerBehavior(zeroPowerBehavior);

        rightFore.setDirection(REVERSE);
        rightRear.setDirection(REVERSE);
        leftFore.setDirection(FORWARD);
        leftRear.setDirection(FORWARD);
        leftFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFore.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFore.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftFore.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFore.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lfEncoder = new MotorEncoder(leftFore);
        lrEncoder = new MotorEncoder(leftRear);
        rfEncoder = new MotorEncoder(rightFore);
        rrEncoder = new MotorEncoder(rightRear);
        lfEncoder.initEncoder();
        lrEncoder.initEncoder();
        rfEncoder.initEncoder();
        rrEncoder.initEncoder();
    }
    public void hardResetEncoders(){
        lfEncoder.hardResetEncoder();
        rfEncoder.hardResetEncoder();
        lrEncoder.hardResetEncoder();
        rrEncoder.hardResetEncoder();
    }
    public void normalizeMotors() {}

    @Override
    public void resetEncoders() {}

    public void updateEncoders(){
        lfEncoder.updateEncoder();
        rfEncoder.updateEncoder();
        rrEncoder.updateEncoder();
        lrEncoder.updateEncoder();
    }

    public double averageLeftEncoders(){
        return (lfEncoder.currentPosition + lrEncoder.currentPosition) / 2;
    }
    public double averageRightEncoders(){
        return (rfEncoder.currentPosition + rrEncoder.currentPosition) / 2;
    }
    public void draev(Vector2 stick, double leftTrigger, double rightTrigger){
        double velocity = rightTrigger - leftTrigger;
        leftPow = rightTrigger - leftTrigger - stick.x;
        rightPow = rightTrigger - leftTrigger + stick.x;
    }
}