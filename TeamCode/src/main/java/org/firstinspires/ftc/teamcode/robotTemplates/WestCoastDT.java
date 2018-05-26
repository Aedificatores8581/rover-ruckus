package org.firstinspires.ftc.teamcode.robotTemplates;


import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Frank Portman on 5/21/2018
 */
public abstract class WestCoastDT extends Drivetrain {
    double turnMult = 1;
    double leftPow, rightPow;
    Direction direction;
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
    public enum Direction{
        FOR,
        BACK
    }
    public void setTurnMult(double mult){
        turnMult = mult;
    }
    public void switchTurnDir(double ang){

    }
    public Direction setTurnDir(){
        if(leftPow + rightPow > 0)
            direction = Direction.FOR;
        else if(leftPow != rightPow)
            direction = Direction.BACK;
        return direction;
    }
    public abstract void setLeftPow(double pow);
    public abstract void setRightPow(double pow);
    public void setLeftPow(){
        setLeftPow(leftPow);
    }
    public void setRightPow(){
        setRightPow(rightPow);
    }
    public void setSpeed(double s){
        speed = s;
    }

}
