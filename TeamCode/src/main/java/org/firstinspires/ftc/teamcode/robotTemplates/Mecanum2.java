package org.firstinspires.ftc.teamcode.robotTemplates;

import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;

/**
 * Created by Frank Portman on 5/21/2018
 */
public class Mecanum2 extends MecanumDT {
    double angle, y, x, rad, rt, rx, max, I, II, III, IV;
    double turnMult;
    ControlState cs;

    public Mecanum2(double brakePow) {
        super(brakePow);
    }

    @Override
    public void init() {

    }

    @Override
    public String[] names() {
        String[] names = {"rf", "lf", "lr", "rr"};
        return names;
    }

    @Override
    public void loop() {
        x = gamepad1.left_stick_x;
        y = gamepad1.left_stick_y;
        rt = gamepad1.right_trigger;
        rx = gamepad1.right_stick_x;
        switch (cs) {
            case ARCADE:
                x = Math.sqrt(x * x + y * y) * UniversalFunctions.round(x);
                y = Math.sqrt(x * x + y * y) * UniversalFunctions.round(y);
                break;
            case FIELD_CENTRIC:
                angle = Math.toRadians(normalizeGamepadAngleL(normalizeGyroAngle()));
                y = Math.cos(angle) * rad;
                x = Math.sin(angle) * rad;
                break;
        }
        turnMult = 1 - rad * (1 - super.turnMult);
        I = y - x - rx * turnMult;
        II = y + x + rx * turnMult;
        III = y - x + rx * turnMult;
        IV = y + x - rx * turnMult;
        max = Math.max(Math.max(Math.abs(I), Math.abs(II)), Math.max(Math.abs(III), Math.abs(IV)));
        I = I * y / max;
        II = II * y / max;
        III = III * y / max;
        IV = IV * y / max;
        refreshMotors(I, II, III, IV);

    }
}
