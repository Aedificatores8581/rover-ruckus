package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Frank Portman on 3/31/2018.
 */
@TeleOp(name = "westBotTestDrive", group = "Test_Drive")
public class WestBotDriveTest extends WestBotTemplate {
    ControlState cs;
    TurnDir td;
    int mult = 0;
    double rt = 0, x = 0, y = 0, b = 0, angle = 0, rp, lp, rad, cos, max;
    boolean switchMode = false, switchBool = false, turn = false;
    @Override
    public void init(){
        super.init();
        cs = ControlState.ARCADE;
        td = TurnDir.FOR;
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
                b = gamepad1.right_trigger;
                if(b >= UniversalConstants.Triggered.TRIGGER)
                    y = b;
                else {
                    y = gamepad1.left_stick_y;
                    y = -Math.sqrt(x * x + y * y) * UniversalFunctions.round(y);
                }
                mult = 0;
                if(gamepad1.left_stick_y > 0)
                    mult = 1;
                else if(gamepad1.left_stick_y < 0)
                    mult = -1;
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
                y = gamepad1.left_stick_y;
                x = gamepad1.left_stick_x;
                rad = Math.sqrt(x * x + y * y);

                cos = normalizeGamepadAngle(normalizeGyroAngle(getGyroAngle()));
                if(rad < UniversalConstants.Triggered.STICK) {
                    setLeftPow(0);
                    setRightPow(0);
                }
                else {
                    switch (td) {
                        case FOR:
                            if (cos > 180 && turn) {
                                td = TurnDir.BACK;
                                mult *= -1;
                                turn = false;
                            } else if (cos <= 180)
                                turn = true;
                            break;
                        case BACK:
                            if (cos > 180 && turn) {
                                td = TurnDir.FOR;
                                turn = false;
                                mult *= -1;
                            } else if (cos <= 180)
                                turn = true;
                            break;
                    }
                    lp = -rad * mult - mult * Math.cos(Math.toRadians(cos));
                    rp = -rad * mult + mult * Math.cos(Math.toRadians(cos));
                    max = Math.max(Math.abs(rp), Math.abs(lp));
                    rp = rp / max * rad;
                    lp = lp / max * rad;
                    setLeftPow(-lp * 0.75);
                    setRightPow(-rp * 0.75);
                }
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