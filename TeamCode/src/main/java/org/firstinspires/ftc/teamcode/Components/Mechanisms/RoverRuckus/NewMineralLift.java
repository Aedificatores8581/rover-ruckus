package org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Components.Sensors.MotorEncoder;

public class NewMineralLift {
    public DcMotor liftMotor;
    public CRServo vaex1, vaex2;
    public Servo raev1, raev2;
    public double maxSpeed = 1;
    public double maxSpeed1 = 0.5, maxSpeed2 = 0.5;
    public final boolean USING_VEX = false;
    public void init(HardwareMap hardwareMap){
        liftMotor = hardwareMap.dcMotor.get("lift");
        liftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        if(USING_VEX) {
            vaex1 = hardwareMap.crservo.get("vex1");
            vaex2 = hardwareMap.crservo.get("vex2");
            vaex1.setPower(0.01);
            vaex2.setPower(0.01);
            maxSpeed1 = 0.99;
            maxSpeed2 = 0.84;
        }
        else{
            raev1 = hardwareMap.servo.get("vex1");
            raev2 = hardwareMap.servo.get("vex2");
            raev1.setDirection(Servo.Direction.FORWARD);
            raev2.setDirection(Servo.Direction.FORWARD);
            raev1.setPosition(maxSpeed1);
            raev2.setPosition(maxSpeed2);
        }
    }
    public void lift(double value){
        liftMotor.setPower(value * maxSpeed);
    }
    public void articulate(double value){
        if(USING_VEX) {
            vaex1.setPower(value * maxSpeed1);
            vaex2.setPower(value * maxSpeed2);
        }
        else{
            raev1.setPosition(value * maxSpeed1 + 1);
            raev1.setPosition(value * maxSpeed2 + 1);
        }
    }

}
