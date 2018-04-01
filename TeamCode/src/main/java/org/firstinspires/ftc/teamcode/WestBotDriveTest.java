package org.firstinspires.ftc.teamcode;

/**
 * Created by Frank Portman on 3/31/2018.
 */

public class WestBotDriveTest extends WestBotTemplate {

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
        setLeftPow((-gamepad1.left_stick_y) - TURN_MULT * (gamepad1.right_stick_x * Math.round(gamepad1.left_stick_y + .0000000000000000000001)));
        setRightPow((-gamepad1.left_stick_y) + TURN_MULT * (gamepad1.right_stick_x * Math.round(gamepad1.left_stick_y) + .000000000000000000001));
        brake(1);
    }

}
