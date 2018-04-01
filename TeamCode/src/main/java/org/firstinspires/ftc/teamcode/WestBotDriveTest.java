package org.firstinspires.ftc.teamcode;

/**
 * Created by Frank Portman on 3/31/2018.
 */

public class WestBotDriveTest extends WestBotTemplate {
    int mult = 0;
    @Override
    public void init(){
        super.init();
    }
    @Override
    public void start(){
        super.start();
    }
    @Override
    public void loop() {
        mult = 0;
        if(gamepad1.left_stick_y > 0)
            mult = 1;
        else if(gamepad1.left_stick_y < 0)
            mult = -1;
        setLeftPow((-gamepad1.left_stick_y) - TURN_MULT * (gamepad1.right_stick_x * mult));
        setRightPow((-gamepad1.left_stick_y) + TURN_MULT * (gamepad1.right_stick_x * mult));
        brake(1);
    }

}
