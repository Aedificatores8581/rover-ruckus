package org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

public class Intake {
    public Servo dispensor;
    public DcMotor motor;
    public double maxSpeed = 1;
    //TODO: find these values
    public final double CLOSED_DISPENSOR_POSITION = 0, OPEN_DISPENSOR_POSITION = 1;

    public Intake(){
    }
    public void init(HardwareMap hardwareMap){
        motor = hardwareMap.dcMotor.get("int");
        dispensor = hardwareMap.servo.get("idis");
    }

    public double getPower(){
        return motor.getPower() * maxSpeed;
    }

    public void setPower(double pow){
        motor.setPower(pow * maxSpeed);
    }

    public void dispense(){
        dispensor.setPosition(getPower() == 0 ? OPEN_DISPENSOR_POSITION : CLOSED_DISPENSOR_POSITION);
    }


    public String toString(){
        String dispensorState = "closed";
        if(dispensor.getPosition() == OPEN_DISPENSOR_POSITION)
            dispensorState = "open";
        return getPower() + ", " + "dispensor " + dispensorState;
    }
}
