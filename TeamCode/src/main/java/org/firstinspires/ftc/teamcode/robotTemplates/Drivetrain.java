package org.firstinspires.ftc.teamcode.robotTemplates;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


/**
 * Created by Frank Portman on 5/21/2018
 */
import java.util.ArrayList;

public abstract class Drivetrain extends Robot {
    public final DcMotor.Direction FORWARD = DcMotor.Direction.FORWARD, REVERSE = DcMotor.Direction.REVERSE;
<<<<<<< HEAD
    public double minTurn;
    public double brakePow;
    public double maxSpeed = 1;
    public Drivetrain(double pow){
        super(false);
        brakePow = pow;
=======
    ArrayList<DcMotor> driveMotors;
    public double speed = 1;

    public Drivetrain(DcMotor.ZeroPowerBehavior z){
        super(true);
        String[] names = names();
        DcMotor[] motors = motors();
        DcMotor.Direction[] dir = dir();
        for(int i = 0; i < dir.length; i++){
            motors[i] = hardwareMap.dcMotor.get(names[i]);
            motors[i].setDirection(dir[i]);
            driveMotors.add(motors[i]);
        }
        for (DcMotor i : driveMotors) {
            i.setZeroPowerBehavior(z);
        }
>>>>>>> bded60090eafc2d2157f1a924ecde0559db2f773
    }
    //Maps the drive motors to the rev module
    public abstract void initMotors();
    @Override
    public void init(){
        super.init();
    }
<<<<<<< HEAD
    //gives the motors holding power
    public abstract void brake();
=======

    //returns the names of the motors of the drivetrain
    public abstract String[] names();

    //returns the motors of the drivetrain
    public abstract DcMotor[] motors();

    //returns the directions of the motors of the drivetrain
    public abstract DcMotor.Direction[] dir();

>>>>>>> bded60090eafc2d2157f1a924ecde0559db2f773
    public enum Direction{
        FOR,
        BACK
    }
    public void start(){
        super.start();
    }
    public void setPower(DcMotor m, double pow){
        m.setPower(pow *  maxSpeed);
    }
    public abstract void normalizeMotors();
}
