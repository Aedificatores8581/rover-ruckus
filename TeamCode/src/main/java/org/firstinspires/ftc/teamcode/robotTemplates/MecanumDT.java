package org.firstinspires.ftc.teamcode.robotTemplates;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;
import org.firstinspires.ftc.teamcode.robotUniversal.Vector2;

/**
 * Created by Frank Portman on 5/21/2018
 */
public class MecanumDT extends Drivetrain {
    DcMotor lf, la, rf, ra;
    double turnMult = 1;
    public ControlState ControlState;
    public double leftForePow, rightForePow, turnPow, angleBetween;
    public MecanumDT(double brakePow){
        super(brakePow);
    }
    public void init(){
        super.init();
        minTurn = 1;
    }
    public void start(){
        super.start();
    }

    //returns the power of the left rear and right fore motors needed to drive at a given angle at a given speed
    public double getRightForePow(double ang, double speed){
        return Math.sin(ang) * speed / 2;
    }

    //returns the power of the left rear and right fore motors needed to drive along a given vector
    public double getRightForePow(Vector2 vel){
        return getRightForePow(vel.angle(), vel.magnitude());
    }

    //returns the power of the left fore and right rear motors needed to drive at a given angle at a given speed
    public double getLeftForePow(double ang, double speed){
        return Math.cos(ang) * speed / 2;
    }
    //returns the power of the left fore and right rear motors needed to drive along a given vector
    public double getLeftForePow(Vector2 vel){
        return getLeftForePow(vel.angle(), vel.magnitude());
    }
    //sets the power of the motors in order to drive at a given angle at a given speed
    public void setDirection(double ang, double speed){
        rightForePow = getRightForePow(ang, speed);
        leftForePow= getLeftForePow(ang, speed);
    }
    //sets the power of the motors in order to drive at a given velocity
    public void setDirection(Vector2 vel){
        setDirection(vel.angle(), vel.magnitude());
    }
    //sets the power of the left fore and right rear motors
    public void setLeftForePow(double pow){
        leftForePow = pow;
    }
    //sets the power of the left fore and right rear motors in order to drive at a given angle at a given speed
    public void setLeftForePow(double ang, double speed){
        setLeftForePow(getLeftForePow(ang, speed));
    }
    //sets the direction of the right fore and left rear motors
    public void setRightForePow(double pow){
        rightForePow = pow;
    }
    //sets the direction of the right fore and left rear motors in order to drive at a given angle at a given speed
    public void setRightForePow(double ang, double speed){
        setRightForePow(getLeftForePow(ang, speed));
    }
    //sets the direction of the motors in order to drive at a given velocity
    public void setRightForePow(Vector2 vel){
        setRightForePow(vel.angle(), vel.magnitude());
    }
    //sets the turnPow variable
    public void setTurn(double pow){
        pow *= turnMult;
        turnPow = pow;
    }
    //sets the motor powers to the specified values
    public void refreshMotors(double I, double II, double III, double IV){
        rf.setPower(I);
        lf.setPower(II);
        la.setPower(III);
        ra.setPower(IV);
    }
    //sets the motor powers to the specified values at the specified speed
    public void refreshMotors(double I, double II, double III, double IV, double speed){
        rf.setPower(speed * I);
        lf.setPower(speed * II);
        la.setPower(speed * III);
        ra.setPower(speed * IV);
    }
    //sets the motor powers to rightForePow and leftForePow respectively
    public void refreshMotors(){
        setPower(rf, rightForePow - turnPow);
        setPower(la, rightForePow + turnPow);
        setPower(lf, leftForePow + turnPow);
        setPower(ra, leftForePow - turnPow);
    }
    //sets the turnMult variable
    public void setTurnMult(double tm){
        turnMult = tm;
    }
    //normalizes the motor powers to never go above 1 or below -1
    public void normalizeMotorPow(){
        double max = UniversalFunctions.maxAbs(leftForePow, rightForePow);
        leftForePow /= max;
        rightForePow /= max;
    }
    public enum ControlState{
        ARCADE,
        FIELD_CENTRIC
    }
    public void loop() {
        updateGamepad1();
        setRobotAngle();
        angleBetween = leftStick1.angleBetween(robotAngle);
        setRightForePow(angleBetween, leftStick1.magnitude());
        setLeftForePow(angleBetween, leftStick1.magnitude());
        switch (ControlState) {
            case ARCADE:
                turnMult = 1 - leftStick1.magnitude() * (1 - minTurn);
                setTurn(rightStick1.magnitude() * turnMult);
                break;
            case FIELD_CENTRIC:
                setTurn(Math.sin(rightStick1.angleBetween(robotAngle)));
                break;
        }
        normalizeMotorPow();
        refreshMotors();
    }
    public void brake(){
        refreshMotors(brakePow, brakePow, -brakePow, -brakePow);
    }
    public void  initMotors(){
        rf = hardwareMap.dcMotor.get("rf");
        lf = hardwareMap.dcMotor.get("lf");
        la = hardwareMap.dcMotor.get("la");
        ra = hardwareMap.dcMotor.get("ra");
        rf.setDirection(REVERSE);
        ra.setDirection(REVERSE);
        lf.setDirection(FORWARD);
        la.setDirection(FORWARD);
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.UNKNOWN);
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.UNKNOWN);
        la.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.UNKNOWN);
        ra.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.UNKNOWN);
    }

}
