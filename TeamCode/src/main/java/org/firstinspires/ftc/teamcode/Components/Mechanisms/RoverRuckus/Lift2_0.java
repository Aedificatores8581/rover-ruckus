package org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Components.Sensors.MotorEncoder;

public class Lift2_0 {
    public DcMotor leftMotor, rightMotor;
    public MotorEncoder leftEnc, rightEnc;

    public double maxSpeed = 1;
    public final double ENC_PER_INCH = 0;

    public void init(HardwareMap hardwareMap, boolean isAutonomous){
        leftMotor = hardwareMap.dcMotor.get("llif");
        rightMotor = hardwareMap.dcMotor.get("rlif");
        leftEnc = new MotorEncoder(leftMotor);
        rightEnc = new MotorEncoder(rightMotor);
    }
    public void lift(double value){
        leftMotor.setPower(value * maxSpeed);
        rightMotor.setPower(value * maxSpeed);
    }
    public double getLiftPosition(){
        return ENC_PER_INCH * (leftEnc.updateEncoder() + rightEnc.updateEncoder()) / 2;
    }
}
