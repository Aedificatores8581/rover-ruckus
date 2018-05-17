package org.firstinspires.ftc.teamcode.robotOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotTemplates.MecBotTemplate;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;

/**
 * Created by Frank Portman on 3/31/2018.
 */
//
@TeleOp(name = "MecBotTestDrive", group = "Test_Drive")
public class MecBotDriveTest extends MecBotTemplate {
    double I, II, III, IV, max, min, x, y, angle, rt, rx, rad;
    boolean brake;
    ControlState cs;
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
        angle = 0;
        rt = 0;
        rad = 0;
        cs = ControlState.ARCADE;
    }
    @Override
    public void start(){
        super.start();
    }
    @Override
    public void loop(){
        x = gamepad1.left_stick_x;
        y = gamepad1.left_stick_y;

        rt = gamepad1.right_trigger;
        rx = gamepad1.right_stick_x;
        switch(cs){
            case ARCADE:
                x = Math.sqrt(x * x + y * y) * UniversalFunctions.round(x);
                y = Math.sqrt(x * x + y * y) * UniversalFunctions.round(y);
                if(switchMode) {
                    cs = cs.FIELD_CENTRIC;
                    switchMode = false;
                    switchBool = false;
                }
                else if(rt < UniversalConstants.Triggered.TRIGGER){
                    switchMode = false;
                    switchBool = true;
                }
                else if(rt > UniversalConstants.Triggered.TRIGGER && switchBool)
                    switchMode = true;
                break;
            case FIELD_CENTRIC:
                angle = Math.toRadians(normalizeGamepadAngle(normalizeGyroAngle(getGyroAngle())));
                telemetry.addData("angle", Math.toDegrees(angle));
                y = Math.cos(angle) * rad;
                x = Math.sin(angle) * rad;
                if(switchMode) {
                    cs = cs.ARCADE;
                    switchMode = false;
                    switchBool = false;
                }
                else if(rt < UniversalConstants.Triggered.TRIGGER){
                    switchMode = false;
                    switchBool = true;
                }
                else if(rt > UniversalConstants.Triggered.TRIGGER && switchBool)
                    switchMode = true;
                break;
        }
        I = y - x - rx;
        II = y + x + rx;
        III = y - x + rx;
        IV = y + x - rx;
        max = Math.max(Math.max(Math.abs(I), Math.abs(II)), Math.max(Math.abs(III), Math.abs(IV)));
        I = I * y/ max;
        II = II * y  / max;
        III = III *  y/ max;
        IV = IV * y / max;
        refreshMotors(I, II, III, IV, true);

        telemetry.addData("Control State: ", cs);
        telemetry.addData("x", x);
        telemetry.addData("y", y);
    }
}
