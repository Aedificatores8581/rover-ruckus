package org.firstinspires.ftc.teamcode;

/**
 * Created by fgpor on 3/31/2018.
 */

public class MecBotDriveTest extends MecBotTemplate {
    double I, II, III, IV;
    boolean brake;
    @Override
    public void init(){
        I = 0;
        II = 0;
        III = 0;
        IV = 0;
        brake = true;
    }
    @Override
    public void start(){

    }
    @Override
    public void loop(){
        I = gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x;

        II = gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x;

        III = gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x;

            IV = gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x;
        if(gamepad1.right_trigger > 0.05){
            brake = false;
        }
        else
            brake = true;
        refreshMotors(I, II, III, IV, true);
    }
}
