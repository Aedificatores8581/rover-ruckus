package org.firstinspires.ftc.teamcode.robotTemplates;

import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;

/**
 * Created by Frank Portman on 5/21/2018
 */
public abstract class WestCoastDT extends Drivetrain {
    public double turnMult, angleBetween, directionMult = 1, cos;
    public double maxTurn = 1;
    public double leftPow, rightPow;
    public Direction direction;
    public ControlState cs;
    private boolean turn = false;
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
    public void loop(){
        updateGamepad1();
        switch(cs) {
            case ARCADE:
                turnMult = 1 - lStick1.magnitude() * (1 - maxTurn);
                leftPow = -lStick1.y - turnMult * rStick1.x;
                rightPow = -lStick1.y + turnMult * rStick1.x;
                break;
            case FIELD_CENTRIC:
                setRobotAngle();
                angleBetween = lStick1.angleBetween(robotAngle);
                if (lStick1.magnitude() < UniversalConstants.Triggered.STICK)
                    brake();
                else {
                    switch (direction) {
                        case FOR:
                            if (Math.sin(angleBetween) < 0 && turn) {
                                direction = Direction.BACK;
                                directionMult *= -1;
                                turn = false;
                            } else if (Math.sin(angleBetween) >= 0)
                                turn = true;
                            break;
                        case BACK:
                            if (Math.sin(angleBetween) > 0 && turn) {
                                direction = Direction.FOR;
                                turn = false;
                                directionMult *= -1;
                            } else if (Math.sin(angleBetween) <= 0)
                                turn = true;
                            break;
                    }
                    cos = Math.cos(angleBetween);
                    switch(ts){
                        case FAST:
                            turnMult = Math.abs(cos) + 1;
                            leftPow = directionMult * (-lStick1.magnitude() - turnMult * cos);
                            rightPow = directionMult * (-lStick1.magnitude() + turnMult * cos);
                            break;
                        case SMOOTH:
                            if(cos > 0) {
                                rightPow = -directionMult;
                                leftPow = directionMult * Math.cos(2 * angleBetween);
                            }
                            else{
                                leftPow = -directionMult;
                                rightPow = directionMult * Math.cos(2 * angleBetween);
                            }
                    }
                }
                break;
            case TANK:
                leftPow = -gamepad1.left_stick_y;
                rightPow = -gamepad1.right_stick_y;
                break;
        }
        setLeftPow();
        setRightPow();
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
