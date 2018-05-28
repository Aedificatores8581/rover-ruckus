package org.firstinspires.ftc.teamcode.robotTemplates;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.robotUniversal.GyroAngles;

/**
 * Created by Frank Portman on 5/21/2018
 */
import java.util.ArrayList;

public abstract class Drivetrain extends Robot {
    public final DcMotor.Direction FORWARD = DcMotor.Direction.FORWARD, REVERSE = DcMotor.Direction.REVERSE;
    public ArrayList<DcMotor> driveMotors;
    public double brakePow;
    public double speed = 1;
    public Drivetrain(double pow){
        super(false);
        brakePow = pow;
    }
    public abstract void initMotors();
    @Override
    public void init(){
        super.init();
    }
    //gives the motors holding power
    public void brake(){
        for(int i = 0; i < driveMotors.size(); i++)
            driveMotors.get(i).setPower(brakePow);
    }
    public enum Direction{
        FOR,
        BACK
    }
    public void start(){
        super.start();
    }
    public void setPower(DcMotor m, double pow){
        m.setPower(pow *  speed);
    }
    public void setMotor(DcMotor m, String s){
        m = hardwareMap.dcMotor.get(s);
    }
}
