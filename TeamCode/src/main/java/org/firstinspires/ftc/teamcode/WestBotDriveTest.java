package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Frank Portman on 3/31/2018.
 */
@TeleOp(name = "westBotTestDrive", group = "Test_Drive")
public class WestBotDriveTest extends WestBotTemplate {
    CONTROL_STATE cs;
    DRIVE_MODE dm;
    int mult = 0;
    double rt = 0, x = 0, y = 0, b = 0, angle = 0;
    boolean switchMode = false, switchBool = false;
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
        x = gamepad1.left_stick_x;
        rt = gamepad1.right_trigger;
        switch(cs){
            case ARCADE:
                mult = 0;
                if(gamepad1.left_stick_y > 0)
                    mult = 1;
                else if(gamepad1.left_stick_y < 0)
                    mult = -1;
                b = gamepad1.right_trigger;
                if(b >= UniversalConstants.Triggered.TRIGGER)
                    y = b;
                else {
                    y = gamepad1.left_stick_y;
                    y = -Math.sqrt(x * x + y * y) * UniversalFunctions.round(y);
                }
                setLeftPow((-gamepad1.left_stick_y) - TURN_MULT * (gamepad1.right_stick_x * mult));
                setRightPow((-gamepad1.left_stick_y) + TURN_MULT * (gamepad1.right_stick_x * mult));
                brake(1);
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
                b = gamepad1.right_trigger;
                if(b >= UniversalConstants.Triggered.TRIGGER)
                    y = b;
                else {
                    y = gamepad1.left_stick_y;
                    y = -Math.sqrt(x * x + y * y) * UniversalFunctions.round(y);
                }
                if(y > 0)
                    mult = 1;
                else if(y < 0)
                    mult = -1;
                setLeftPow(y - TURN_MULT * (normalizeGamepadAngle(normalizeGyroAngle(getGyroAngle()))));
                setRightPow(y + TURN_MULT * (normalizeGamepadAngle(normalizeGyroAngle(getGyroAngle()))));

                if(switchMode) {
                    cs = cs.TANK;
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
            case TANK:
                setLeftPow(-gamepad1.left_stick_y);
                setRightPow(-gamepad1.right_stick_y);
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

    }

}
