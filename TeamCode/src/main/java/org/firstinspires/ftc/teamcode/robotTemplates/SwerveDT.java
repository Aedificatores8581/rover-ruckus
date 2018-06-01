package org.firstinspires.ftc.teamcode.robotTemplates;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public abstract class SwerveDT extends Drivetrain {
    DcMotor rf, lf, lr, rr;
    Servo rfSwervo, lfSwervo, lrSwervo, rrSwervo;
    public SwerveDT(DcMotor.ZeroPowerBehavior z, double rfSwervoStart, double lfSwervoStart, double lrSwervoStart, double rrSwervoStart){
        super(z);
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
}
