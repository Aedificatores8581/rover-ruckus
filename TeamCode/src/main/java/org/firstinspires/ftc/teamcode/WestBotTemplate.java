package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Frank Portman on 3/31/2018.
 */

public abstract class WestBotTemplate extends OpMode{
    DcMotor lf, lr, rf, rr;
    public static final DcMotor.Direction
            LDIR = DcMotorSimple.Direction.FORWARD,
            RDIR = DcMotorSimple.Direction.REVERSE;
    public static final double TURN_MULT = 0.75;
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
    public void setLeftPow(double pow){
        lf.setPower(pow);
        lr.setPower(pow);
    }
    public void setRightPow(double pow){
        rf.setPower(pow);
        rr.setPower(pow);
    }
    public void brake(int dir){
        setLeftPow(0.05 * dir);
        setRightPow(0.05 * dir);
    }
}
