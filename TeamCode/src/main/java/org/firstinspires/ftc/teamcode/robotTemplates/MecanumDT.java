package org.firstinspires.ftc.teamcode.robotTemplates;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.robotUniversal.GyroAngles;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;
import org.firstinspires.ftc.teamcode.robotUniversal.Vector2;

public abstract class MecanumDT extends Drivetrain {
    DcMotor lf, lr, rf, rr;
    DcMotor[] motors = {lf, lr, rf, rr};
    public MecanumDT(String[] names, DcMotor[] m, DcMotor.Direction[] dir){
        super(names, m, dir);
    }
    public void init(){
        super.init();
    }
    public void start(){
        super.start();
    }
    public void setDirection(double ang, double speed){

    }
    public void setDirection(Vector2 vel){

    }
    public void setDirection(){

    }
}
