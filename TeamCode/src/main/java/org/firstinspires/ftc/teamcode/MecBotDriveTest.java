package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Frank Portman on 3/31/2018.
 */
@TeleOp(name = "MecBotTestDrive", group = "Test_Drive")
public class MecBotDriveTest extends MecBotTemplate {
    double I, II, III, IV, max, min, x, y, mult, rt;
    boolean brake;
    ControlState cs;
    DriveMode dm;
    boolean switchMode = false, switchBool = false;
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
        mult = 0;
        rt = 0;
    }
    @Override
    public void start(){
        super.start();
    }
    @Override
    public void loop(){
        x = gamepad1.left_stick_x;
        y = gamepad1.left_stick_y;
        x = Math.sqrt(x * x + y * y) * UniversalFunctions.round(x);
        y = Math.sqrt(x * x + y * y) * UniversalFunctions.round(y);
        rt = gamepad1.right_trigger;
        switch(cs){
            case ARCADE:
                I = y - x - gamepad1.right_stick_x;
                II = y + x + gamepad1.right_stick_x;
                III = y - x + gamepad1.right_stick_x;
                IV = y + x - gamepad1.right_stick_x;
                if(switchMode) {
                    cs = cs.FIELD_CENTRIC;
                    switchMode = false;
                    switchBool = false;
                }
                else if(rt < 0.2){
                    switchMode = false;
                    switchBool = true;
                }
                else if(rt > 0.2 && switchBool)
                    switchMode = true;
                break;
            case FIELD_CENTRIC:
                mult = Math.cos(normalizeGamepadAngle(normalizeGyroAngle(getGyroAngle())));
                I = y - x - mult;
                II = y + x + mult;
                III = y - x + mult;
                IV = y + x - mult;
                if(switchMode) {
                    cs = cs.ARCADE;
                    switchMode = false;
                    switchBool = false;
                }
                else if(rt < 0.2){
                    switchMode = false;
                    switchBool = true;
                }
                else if(rt > 0.2 && switchBool)
                    switchMode = true;
                break;
        }
        max = Math.max(Math.max(Math.abs(I), Math.abs(II)), Math.max(Math.abs(III), Math.abs(IV)));
        I = I * y/ max;
        II = II * y  / max;
        III = III *  y/ max;
        IV = IV * y / max;
        refreshMotors(I, II, III, IV, true);
    }
}
