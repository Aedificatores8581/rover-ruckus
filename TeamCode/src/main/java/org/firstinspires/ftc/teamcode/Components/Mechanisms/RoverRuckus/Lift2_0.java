package org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lift2_0 {
    public DcMotor liftMotor;
    public CRServo vaex1, vaex2;
    public double maxSpeed = 1;

    public void init(HardwareMap hardwareMap, boolean isAutonomous){
        liftMotor = hardwareMap.dcMotor.get("lift");
        liftMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        vaex1 = hardwareMap.crservo.get("vex1");
        vaex2 = hardwareMap.crservo.get("vex2");
    }

    public void lift(double value){
        liftMotor.setPower(value * maxSpeed);
    }

    public void articulate(double value) {
        vaex1.setPower(value * 0.99);
        vaex2.setPower(value * 0.99);
    }
}
