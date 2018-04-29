package org.firstinspires.ftc.teamcode.robotOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotTemplates.WestBotTemplate;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;

/**
 * Created by Frank Portman on 3/31/2018.
 */
//
@TeleOp(name = "westBotTestDrive", group = "Test_Drive")
public class WestBotDriveTest extends WestBotTemplate {
    ControlState cs;

    double fsTurn;
    boolean bumpPressed;
    TurnDir td;
    FCTurnState ts;
    double normAngle;
    int mult = 0;
    double rt = 0, x = 0, y = 0, b = 0, angle = 0, rp, lp, rad, max, turnMult = 0, cos = 0, sin = 0, switchVal = Math.sin(Math.PI / 12 * 7);
    boolean switchMode = false, switchBool = false, turn = false, isAngleChanged = false;
    @Override
    public void init(){
        super.init();
        cs = ControlState.ARCADE;
        td = TurnDir.FOR;
        max = 0;
        rad = 0;
    }
    @Override
    public void start(){
        super.start();
    }
    @Override
    public void loop() {
        rt = gamepad1.right_trigger;
        switch(cs){
            case ARCADE:
                rt = gamepad1.right_trigger;
                switch(cs){
                    case ARCADE:
                        x = gamepad1.left_stick_x;
                        b = gamepad1.right_trigger;
                        if(b >= UniversalConstants.Triggered.TRIGGER)
                            y = b;
                        else {
                            y = gamepad1.left_stick_y;
                            y = -Math.sqrt(x * x + y * y) * UniversalFunctions.round(y);
                        }
                        turnMult = 1 - gamepad1.left_stick_y * (1 - TURN_MULT);
                        setLeftPow(-y - turnMult * x);
                        setRightPow(-y + turnMult * x);
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
                    if(gamepad1.left_stick_button && !isAngleChanged) {
                        setStartAngle();
                        isAngleChanged = true;
                    }
                    else if(!gamepad1.left_stick_button){
                        isAngleChanged = false;
                    }
                }
                else {
                    switch (td) {
                        case FOR:
                            sin = Math.sin(normAngle);
                            if (Math.sin(normAngle) < 0 && turn) {
                                td = TurnDir.BACK;
                                mult *= -1;
                                turn = false;
                            } else if (Math.sin(normAngle) >= 0)
                                turn = true;
                            break;
                        case BACK:
                            sin = Math.sin(normAngle);
                            if (Math.sin(normAngle) > 0 && turn) {
                                td = TurnDir.FOR;
                                turn = false;
                                mult *= -1;
                            } else if (Math.sin(normAngle) <= 0)
                                turn = true;
                            break;
                    }
                    cos = Math.cos(normAngle);
                    fsTurn = (Math.abs(cos) + 1);
                    /*switch(ts){
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
                    }*/
                    lp = -rad * mult - mult * fsTurn * cos;
                    rp = -rad * mult + mult * fsTurn * cos;
/*                    if(ts == FCTurnState.SMOOTH) {
                        max = Math.max(Math.abs(rp), Math.abs(lp));
                        rp = rp / max * rad;
                        lp = lp / max * rad;
                    }*/
                    setLeftPow(lp / 2);
                    setRightPow(rp / 2);
                }/*
                rx = gamepad1.right_stick_x;
                ry = gamepad1.right_stick_y;
                servo1Position += rx;
                servo2Position += ry;
                serv1.setPosition(servo1Position);
                serv2.setPosition(servo2Position);*/
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

        telemetry.addData("Drive mode", cs);
        telemetry.addData("angle", getGyroAngle() - startAngle);
        telemetry.addData("angle", normalizeGamepadAngle(normalizeGyroAngle(getGyroAngle())));
        telemetry.addData("gpangle", getGamepadAngle());
        telemetry.addData("lp", lp);
        telemetry.addData("rp", rp);
        telemetry.addData("dr", -rad * mult);
        telemetry.addData("angle", Math.toDegrees(normAngle));
        telemetry.addData("cos", cos);
        telemetry.addData("sin", cos);
        telemetry.addData("Control state", td);
        telemetry.addData("turn", turn);
    }
}