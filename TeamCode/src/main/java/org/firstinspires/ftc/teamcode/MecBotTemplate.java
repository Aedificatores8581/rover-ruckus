package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Frank Portman on 3/31/2018.
 */

public abstract class MecBotTemplate extends OpMode{
    DcMotor lf, lr, rf, rr;
    private static final DcMotor.Direction
            LDIR = DcMotorSimple.Direction.FORWARD,
            RDIR = DcMotorSimple.Direction.REVERSE;
    private static final double BRAKE_POW = 0.01;

    public void init(){
        lf = hardwareMap.dcMotor.get("lf");
        lr = hardwareMap.dcMotor.get("lr");
        rf = hardwareMap.dcMotor.get("rf");
        rr = hardwareMap.dcMotor.get("rr");
        lf.setDirection(LDIR);
        lr.setDirection(LDIR);
        rf.setDirection(RDIR);
        rr.setDirection(RDIR);

    }
    public void start(){

    }
    public void refreshMotors(double I, double II, double III, double IV, boolean brake){
        if(brake){
            brake();
        }
        lf.setPower(II);
        lr.setPower(III);
        rf.setPower(I);
        rr.setPower(IV);
    }
    public void normalize(double I, double II, double III, double IV){
        double max = Math.max(Math.max(Math.abs(I), Math.abs(II)), Math.max(Math.abs(III), Math.abs(IV)));
        if(max > 1){
            I /= max;
            II /= max;
            III /= max;
            IV /= max;
        }
        refreshMotors(I, II, III, IV, true);
    }
    public void brake(){
        if(rf.getPower() == 0 && lf.getPower() == 0 && lr.getPower() == 0 && rr.getPower() == 0){
            rf.setPower(-BRAKE_POW);
            rr.setPower(BRAKE_POW);
            lr.setPower(-BRAKE_POW);
            lf.setPower(BRAKE_POW);
        }
    }
}
