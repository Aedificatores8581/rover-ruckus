package org.firstinspires.ftc.teamcode.robotTemplates;


import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;

/**
 * Created by Frank Portman on 5/21/2018
 */
public class WestCoast15 extends WestCoastDT{
    public DcMotor rf, lf, lr, rr;
    public WestCoast15(){
        super(0.001);
        speed = 1;
    }
    public WestCoast15(double brakePow, double sped){
        super(brakePow);
        speed = sped;
    }
    public void loop(){
        super.loop();
    }
    public void setLeftPow(double pow) {
        lf.setPower(pow * speed);
        lr.setPower(pow * speed);
        leftPow = pow;
    }
    public void setRightPow(double pow){
        rf.setPower(pow * speed);
        rr.setPower(pow * speed);
        rightPow = pow;
    }
    public DcMotor[] motors(){
        DcMotor[] motors = {rf, lf, lr, rr};
        return motors;
    }
    public String[] names(){
        String[] names = {"rf", "lf", "lr", "rr"};
        return names;
    }
    public DcMotor.Direction[] dir(){
        DcMotor.Direction[] dir = {FORWARD, REVERSE, REVERSE, FORWARD};
        return dir;
    }
}
