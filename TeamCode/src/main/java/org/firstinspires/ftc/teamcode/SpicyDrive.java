package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Hunter Seachrist on 3/17/2018.
 */

@TeleOp(name = "Now that's a Spicy One!", group = "spicy bepis")
public class SpicyDrive extends DriveBotTestTemplate {

    private static final double TURN_MULT = .75;
    private static final double REV_POINT = 0.25;
    int rsx = 1;
    int mult = 0;
    @Override
    public void init(){
        super.init();
    }

    @Override
    public void loop(){
        mult = 0;
        if(gamepad1.left_stick_y > 0)
            mult = 1;
        else if(gamepad1.left_stick_y < 0)
            mult = -1;
        setLeftPow((-gamepad1.left_stick_y) - TURN_MULT * (gamepad1.right_stick_x * mult));
        setRightPow((-gamepad1.left_stick_y) + TURN_MULT * (gamepad1.right_stick_x * mult));
    }

}
