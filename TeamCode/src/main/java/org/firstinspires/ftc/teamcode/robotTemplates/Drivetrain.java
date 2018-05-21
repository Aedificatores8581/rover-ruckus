package org.firstinspires.ftc.teamcode.robotTemplates;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.robotUniversal.GyroAngles;

import java.util.ArrayList;

public abstract class Drivetrain extends Robot {
    ArrayList<DcMotor> driveMotors;
    double brakePow;
    public Drivetrain(double pow){
        String[] names = names();
        DcMotor[] motors = motors();
        DcMotor.Direction[] dir = dir();
        for(int i = 0; i < dir.length; i++){
            motors[i] = hardwareMap.dcMotor.get(names[i]);
            motors[i].setDirection(dir[i]);
            driveMotors.add(motors[i]);
        }
        brakePow = pow;
    }
    public void init(){
        super.init();
    }
    public abstract void initialize();
    public void brake(){
        for(int i = 0; i < driveMotors.size(); i++)
            driveMotors.get(i).setPower(brakePow);
    }

    public abstract String[] names();
    public abstract DcMotor[] motors();
    public abstract DcMotor.Direction[] dir();
}
