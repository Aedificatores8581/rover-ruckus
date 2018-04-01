package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Frank Portman on 3/31/2018.
 */
@TeleOp(name = "MecBotTestDrive", group = "Test_Drive")
public class MecBotDriveTest extends MecBotTemplate {
    double I, II, III, IV, max, min;
    boolean brake;

    @Override
    public void init(){
        super.init();
        I = 0;
        II = 0;
        III = 0;
        IV = 0;
        brake = true;
        max = 1;
        min = -1;

    }
    @Override
    public void start(){
        super.init();

    }
    @Override
    public void loop(){
        I = gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x;
        II = gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x;
        III = gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x;
        IV = gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x;
        max = Math.max(Math.max(Math.abs(I), Math.abs(II)), Math.max(Math.abs(III), Math.abs(IV)));
        if(max> 1){
            I /= max;
            II /= max;
            III /= max;
            IV /= max;
        }
        refreshMotors(I, II, III, IV, true);
    }
}
