package org.firstinspires.ftc.teamcode;

import java.lang.Object.*;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import java.math.*;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Locale;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * I made this from scratch.
 *
 * @author Pootis Man
 */
@TeleOp(name = "Teleop from Akron Minimaker Fair", group = "bepis")

public class SmallTetrixBotTeleop extends OpMode
{


    DcMotor left, right;

    double leftPow = 0.0;
    double rightPow = 0.0;
    public void start() {
    }

    public void init(){
        left = hardwareMap.dcMotor.get("lm");
        right = hardwareMap.dcMotor.get("rm");

        left.setDirection(Constants.LEFT_DIR);
        right.setDirection(Constants.RIGHT_DIR);
    }

    @Override public void loop () {
        if (gamepad1.left_stick_y < -0.5){
            leftPow = 1.0;
        } else if (gamepad1.left_stick_y > 0.5){
            leftPow = -1.0;
        }else{
            leftPow = 0.0;
        }

        if (gamepad1.right_stick_y < -0.5){
            rightPow = 1.0;
        } else if (gamepad1.right_stick_y> 0.5){
            rightPow = -1.0;
        }else{
            rightPow = 0.0;
        }

        setLeftPow(leftPow);
        setRightPow(rightPow);
    }

    protected void setLeftPow(double pow) {
        left.setPower(pow * Constants.LEFT_SPEED);
    }

    protected void setRightPow(double pow) {
        right.setPower(pow * Constants.RIGHT_SPEED);
    }


    protected boolean checkEncoder(int ticks) {
        int distance = Math.abs(ticks);
        int leftDist = Math.abs(left.getCurrentPosition());
        int rightDist = Math.abs(right.getCurrentPosition());

        return (distance <= leftDist) || (distance <= rightDist);
    }
}





