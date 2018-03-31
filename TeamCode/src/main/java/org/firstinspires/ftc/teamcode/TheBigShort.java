package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * No.
 */
@TeleOp(name = "Rough Drive", group = "B-Tubs")
public class TheBigShort extends DriveBotTestTemplate{
    double y;
    double x;
    @Override
    public void init(){

        super.init();


    }
    @Override
    public void loop(){
        y = gamepad1.right_stick_y;
        x = gamepad1.left_stick_x;
        if(x > 0) {
            setLeftPow(-1 * y + x);
            setRightPow(-1 * y);
        }
        if(x < 0){

            setLeftPow(-1 * y);
            setRightPow(-1 * y + x);

        }

    }

}
