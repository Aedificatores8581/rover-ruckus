package org.firstinspires.ftc.teamcode.robotTemplates;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.robotUniversal.GyroAngles;

import java.util.ArrayList;

public abstract class Drivetrain extends Robot {
    GyroAngles gyroangles;
    Orientation angles;
    BNO055IMU imu;
    ArrayList<DcMotor> motors;
    public Drivetrain(String[] names, DcMotor[] m, DcMotor.Direction[] dir){
        for(int j = 0; j < names.length; j++){
            m[j] = hardwareMap.dcMotor.get(names[j]);
            m[j].setDirection(dir[j]);
            motors.add(m[j]);
        }
    }
    public void init(){
        super.init();
    }
    public abstract void brake();
}
