package org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

public class Intake {
    public Servo dispensor;
    public DcMotor motor;
    public Servo articulator;

    public double maxSpeed = 1;
    //TODO: find these values
    public static final double CLOSED_DISPENSOR_POSITION = 1 , OPEN_DISPENSOR_POSITION = 0.4;
    public double INTAKE_ARTICULATOR_MIDDLE_POSITION = 0.4 ;

    //adjusted from .455 for middle articulator and open dispensor to 0
    public static final double INTAKE_ARTICULATOR_DOWN_POSITION = .55,
            INTAKE_ARTICULATOR_UP_POSITION = .2  ;

    public Intake() { }

    public void init(HardwareMap hardwareMap){
        motor = hardwareMap.dcMotor.get("int");
        dispensor = hardwareMap.servo.get("idis");
        articulator = hardwareMap.servo.get("iart");
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
    public void openDispensor(){
        dispensor.setPosition(OPEN_DISPENSOR_POSITION);
    }
    public void closeDispensor(){
        dispensor.setPosition(CLOSED_DISPENSOR_POSITION);
    }
    public void articulateUp() {
        articulator.setPosition(INTAKE_ARTICULATOR_UP_POSITION);
    }

    public void articulateDown() {
        articulator.setPosition(INTAKE_ARTICULATOR_DOWN_POSITION);
    }

    public String toString(){
        String dispensorState = "closed";
        if (dispensor.getPosition() == OPEN_DISPENSOR_POSITION) {
            dispensorState = "open";
        }

        return "Pow: " + String.valueOf(getPower()) +
                "\nDispensor: " + dispensorState +
                "\nArticulator: " + String.valueOf(articulator.getPosition());
    }
}
