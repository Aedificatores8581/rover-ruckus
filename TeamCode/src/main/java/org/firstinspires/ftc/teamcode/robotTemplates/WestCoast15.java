package org.firstinspires.ftc.teamcode.robotTemplates;


import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;

/**
 * Created by Frank Portman on 5/21/2018
 */
public class WestCoast15 extends WestCoastDT{
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
    @Override
    public void init(){
        usingIMU = true;
        super.init();
        rf = hardwareMap.dcMotor.get("rf");
        lf = hardwareMap.dcMotor.get("lf");
        la = hardwareMap.dcMotor.get("la");
        ra = hardwareMap.dcMotor.get("ra");
        rf.setDirection(REVERSE);
        ra.setDirection(REVERSE);
        lf.setDirection(FORWARD);
        la.setDirection(FORWARD);
    }
    public void setLeftPow(double pow) {
        lf.setPower(pow * speed);
        la.setPower(pow * speed);
        leftPow = pow;
    }
    public void setRightPow(double pow){
        rf.setPower(pow * speed);
        ra.setPower(pow * speed);
        rightPow = pow;
    }
    public void start(){
        super.start();
    }
}
