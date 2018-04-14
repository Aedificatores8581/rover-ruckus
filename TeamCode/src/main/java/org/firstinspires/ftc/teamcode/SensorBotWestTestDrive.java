package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Frank Portamn on 4/8/2018.
 */

@TeleOp(name = "SensorBot Test_Drive", group = "sensorBotWest")
public class SensorBotWestTestDrive extends SensorBotWestTemplate{
    ControlState cs;
    double mult = 0;
    double cos;
    boolean turn = false;
    double max;
    TurnDir td;
    double rt = 0, x = 0, y = 0, b = 0, angle = 0, rx = 0, ry = 0, servo1Position, servo2Position, lp, rp;
    boolean switchMode = false, switchBool = false;
    @Override
    public void init(){
        super.init();
        servo1Position = 0;
        servo2Position = 0;
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

                cos = normalizeGamepadAngle(normalizeGyroAngle(getGyroAngle()));
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
                cos = normalizeGamepadAngle(normalizeGyroAngle(getGyroAngle()));
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
                lp = -Math.sqrt(y * y + x * x) * mult + mult * 0.75 * Math.cos(Math.toRadians(cos));
                rp = -Math.sqrt(y * y + x * x) * mult - mult * 0.75 * Math.cos(Math.toRadians(cos));
                //max = Math.max(Math.abs(rp), Math.abs(lp));
                //rp = rp / max* Math.sqrt(y * y + x * x) ;
                //lp = lp / max * Math.sqrt(y * y + x * x) ;
                setLeftPow(lp * 0.75);
                setRightPow(rp * 0.75);

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
        telemetry.addData("dr", -Math.sqrt(y * y + x * x) * mult);
        telemetry.addData("cos", cos);
        telemetry.addData("coscos", Math.cos(cos));
    }
}
