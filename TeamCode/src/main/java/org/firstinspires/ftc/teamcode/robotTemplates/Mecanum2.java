package org.firstinspires.ftc.teamcode.robotTemplates;

import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;

/**
 * Created by Frank Portman on 5/21/2018
 */
public class Mecanum2 extends MecanumDT {
    double angle, y, x, rad, rt, rx, max, I, II, III, IV, speed;
    double turnMult;
    ControlState cs;
    public Mecanum2(double brakePow, double sped) {
        super(brakePow);
        speed = sped;
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
        angle = Math.toRadians(normalizeGamepadAngleL(normalizeGyroAngle()));
        y = Math.cos(angle) * rad;
        x = Math.sin(angle) * rad;
        switch (cs) {
            case ARCADE:
                turnMult = 1 - rad * (1 - super.turnMult);
                rx = gamepad1.right_stick_x * turnMult;
                break;
            case FIELD_CENTRIC:
                rx = Math.sin(normalizeGamepadAngleR(normalizeGyroAngle()));
                break;
        }
        I = y - x - rx;
        II = y + x + rx;
        III = y - x + rx;
        IV = y + x - rx;
        max = UniversalFunctions.maxAbs(I, II, III, IV);
        I = I * y / max;
        II = II * y / max;
        III = III * y / max;
        IV = IV * y / max;
        refreshMotors(I, II, III, IV, speed);
    }
}
