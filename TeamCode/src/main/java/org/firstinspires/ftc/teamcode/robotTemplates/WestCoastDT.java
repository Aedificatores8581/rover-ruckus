package org.firstinspires.ftc.teamcode.robotTemplates;

/**
 * Created by Frank Portman on 5/21/2018
 */
public abstract class WestCoastDT extends Drivetrain {
    public double turnMult = 1;
    public double leftPow, rightPow;
    public Direction direction;
    public ControlState cs;
    public FCTurnState ts;
    public WestCoastDT(double brakePow){
        super(brakePow);
        leftPow = 0;
        rightPow = 0;
    }
    public enum FCTurnState{
        SMOOTH,
        FAST
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
    //returns the direction the robot is moving
    public Direction setTurnDir(){
        if(leftPow + rightPow > 0)
            direction = Direction.FOR;
        else if(leftPow != rightPow)
            direction = Direction.BACK;
        return direction;
    }
    //Sets the power of the left motor(s)
    public abstract void setLeftPow(double pow);
    //Sets the power of the right motor(s)
    public abstract void setRightPow(double pow);
    //Sets the power of the left motor(s) to the leftPow variable
    public void setLeftPow(){
        setLeftPow(leftPow);
    }
    //Sets the power of the right motor(s) to the rightPow variable
    public void setRightPow(){
        setRightPow(rightPow);
    }
    //Sets the maximum speed of the drive motors
    public void setSpeed(double s){
        speed = s;
    }

}
