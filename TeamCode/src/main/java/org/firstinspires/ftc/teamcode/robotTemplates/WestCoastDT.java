package org.firstinspires.ftc.teamcode.robotTemplates;


import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Frank Portman on 5/21/2018
 */
public abstract class WestCoastDT extends Drivetrain {
    double turnMult = 1;
    double leftPow, rightPow;
    TurnDir td;
    ControlState cs;
    public WestCoastDT(double brakePow){
        super(brakePow);
        leftPow = 0;
        rightPow = 0;
    }
    public void init(){
        super.init();
    }
    //Control systems for the robot
    public enum ControlState{
        ARCADE,
        TANK,
        FIELD_CENTRIC
    }
    //Used in field-centric mode to determine the robot's direction
    public enum TurnDir{
        FOR,
        BACK
    }
    public void setTurnMult(double mult){
        turnMult = mult;
    }
    public void switchTurnDir(double ang){

    }
    public TurnDir setTurnDir(){
        if(leftPow + rightPow > 0)
            td = TurnDir.FOR;
        else if(leftPow != rightPow)
            td = TurnDir.BACK;
        return td;
    }
    public abstract void setLeftPow(double pow);
    public abstract void setRightPow(double pow);
    public void setLeftPow(){
        setLeftPow(leftPow);
    }
    public void setRightPow(){
        setLeftPow(rightPow);
    }

}
