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

/**
 * Created by Frank Portman on 5/21/2018
 */
public abstract class MecanumDT extends Drivetrain {
    DcMotor lf, lr, rf, rr;
    double turnMult = 1;
    public MecanumDT(double brakePow){
        super(brakePow);
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
    @Override
    public String[] names(){
        String[] names = {"rf", "lf", "lr", "rr"};
        return names;
    }

    @Override
    public DcMotor[] motors(){
        DcMotor[] motors = {rf, lf, lr, rr};
        return motors;
    }
    @Override
    public DcMotor.Direction[] dir(){
        DcMotor.Direction[] dir = {DcMotor.Direction.FORWARD, DcMotor.Direction.REVERSE, DcMotor.Direction.REVERSE, DcMotor.Direction.FORWARD};
        return dir;
    }
    public void refreshMotors(double I, double II, double III, double IV){
        rf.setPower(I);
        lf.setPower(II);
        lr.setPower(III);
        rr.setPower(IV);
    }

    protected enum ControlState{
        ARCADE,
        FIELD_CENTRIC,
    }
    public void setTurnMult(double tm){
        turnMult = tm;
    }
}
