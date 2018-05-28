package org.firstinspires.ftc.teamcode.robotTemplates;

/**
 * Created by Frank Portman on 5/21/2018
 */
public class WestCoast15 extends WestCoastDT{
    public WestCoast15(){
        super(0.001);
        maxSpeed = 1;
    }
    public WestCoast15(double brakePow, double speed){
        super(brakePow);
        maxSpeed = speed;
    }
    @Override
    public void init(){
        usingIMU = true;
        super.init();
    }
    @Override
    public void start(){
        super.start();
    }
    @Override
    public void loop(){
        super.loop();
        initMotors();
    }
    public void setLeftPow(double pow) {
        lf.setPower(pow * maxSpeed);
        la.setPower(pow * maxSpeed);
        leftPow = pow;
    }
    public void setRightPow(double pow){
        rf.setPower(pow * maxSpeed);
        ra.setPower(pow * maxSpeed);
        rightPow = pow;
    }
}
