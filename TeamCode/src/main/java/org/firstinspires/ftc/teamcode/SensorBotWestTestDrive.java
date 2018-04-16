package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Frank Portamn on 4/8/2018.
 */

@TeleOp(name = "SensorBot Test_Drive", group = "sensorBotWest")
public class SensorBotWestTestDrive extends SensorBotWestTemplate{
    ControlState cs;
    double mult = 0;
    double normAngle;
    double fsTurn;
    boolean turn = true;
    double max;
    boolean bumpPressed;
    TurnDir td;
    FCTurnState ts;
    double rt = 0, x = 0, y = 0, b = 0, angle = 0, rx = 0, ry = 0, servo1Position, servo2Position, lp, rp, rad;
    boolean switchMode = false, switchBool = false;
    @Override
    public void init(){
        super.init();
        servo1Position = 0;
        servo2Position = 0;
        cs = ControlState.ARCADE;
        td = TurnDir.FOR;
        rad = 0;
        ts = FCTurnState.SMOOTH;
        fsTurn = 1;
        bumpPressed  = false;
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

                if(gamepad1.left_stick_y > 0)
                    mult = 1;
                else
                    mult = -1;

                setLeftPow((-gamepad1.left_stick_y) - TURN_MULT * (gamepad1.right_stick_x * mult));
                setRightPow((-gamepad1.left_stick_y) + TURN_MULT * (gamepad1.right_stick_x * mult));
                brake(1);
                if(switchMode) {
                    cs = cs.FIELD_CENTRIC;
                    switchMode = false;
                    switchBool = false;
                    mult = 1;
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
                normAngle = Math.toRadians(normalizeGamepadAngle(normalizeGyroAngle(getGyroAngle())));
                if(rad < UniversalConstants.Triggered.STICK) {
                    setLeftPow(0);
                    setRightPow(0);
                }
                else {
                    switch (td) {
                        case FOR:
                            if (Math.sin(normAngle) < Math.sin(Math.PI / 12 * 7) && turn) {
                                td = TurnDir.BACK;
                                mult *= -1;
                                turn = false;
                            } else if (Math.sin(normAngle) >= Math.sin(Math.PI / 12 * 7))
                                turn = true;
                            break;
                        case BACK:
                            if (Math.sin(normAngle) > Math.sin(Math.PI / 12 * 7) && turn) {
                                td = TurnDir.FOR;
                                turn = false;
                                mult *= -1;
                            } else if (Math.sin(normAngle) <= Math.sin(Math.PI / 12 * 7))
                                turn = true;
                            break;
                    }
                    switch(ts){
                        case FAST:
                            fsTurn = (Math.abs(Math.cos(normAngle)) + 1);
                            if(gamepad1.right_bumper && bumpPressed){
                                ts = FCTurnState.SMOOTH;
                                bumpPressed = false;
                            }
                            else if(!gamepad1.right_bumper)
                                bumpPressed = true;
                            break;
                        case SMOOTH:
                            fsTurn = 1;
                            if(gamepad1.right_bumper && bumpPressed){
                                ts = FCTurnState.FAST;
                                bumpPressed = false;
                            }
                            else if(!gamepad1.right_bumper)
                                bumpPressed = true;
                            break;

                    }
                    lp = -rad * mult + mult * fsTurn * Math.cos(normAngle);
                    rp = -rad * mult - mult * fsTurn * Math.cos(normAngle);
                    if(ts == FCTurnState.SMOOTH) {
                        max = Math.max(Math.abs(rp), Math.abs(lp));
                        rp = rp / max * rad;
                        lp = lp / max * rad;
                    }
                    setLeftPow(-lp);
                    setRightPow(-rp);
                }
                rx = gamepad1.right_stick_x;
                ry = gamepad1.right_stick_y;
                servo1Position += rx;
                servo2Position += ry;
                //serv1.setPosition(servo1Position);
                //serv2.setPosition(servo2Position);
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
        telemetry.addData("Drive mode", cs);
        telemetry.addData("angle", getGyroAngle() - startAngle);
        telemetry.addData("angle", normalizeGamepadAngle(normalizeGyroAngle(getGyroAngle())));
        telemetry.addData("gpangle", getGamepadAngle());
        telemetry.addData("lp", lp);
        telemetry.addData("rp", rp);
        telemetry.addData("dr", -rad * mult);
        telemetry.addData("angle", Math.toDegrees(normAngle));
        telemetry.addData("cos(cos)", Math.cos(normAngle));
        telemetry.addData("sin(sin)", Math.sin(normAngle));
        telemetry.addData("Control state", td);
        telemetry.addData("turn", turn);
    }
}
