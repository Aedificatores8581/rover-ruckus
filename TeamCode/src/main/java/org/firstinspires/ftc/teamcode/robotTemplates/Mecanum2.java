package org.firstinspires.ftc.teamcode.robotTemplates;

import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;
import org.firstinspires.ftc.teamcode.robotUniversal.Vector2;

/**
 * Created by Frank Portman on 5/21/2018
 */
public class Mecanum2 extends MecanumDT {
    double turnMult, angleBetween;
    ControlState cs;
    public Mecanum2(double brakePow, double sped) {
        super(brakePow);
        speed = sped;
    }
    @Override
    public void init() {
        super.init();
    }
    @Override
    public void start(){
        super.start();
    }
    @Override
    public void loop() {
        updateGamepad1();
        setGyroAngle();
        angleBetween = lStick1.angleBetween(robotAngle);
        setRightForePow(angleBetween, lStick1.magnitude());
        setLeftForePow(angleBetween, lStick1.magnitude());
        switch (cs) {
            case ARCADE:
                turnMult = 1 - lStick1.magnitude() * (1 - super.turnMult);
                setTurn(rStick1.magnitude() * turnMult);
                break;
            case FIELD_CENTRIC:
                setTurn(Math.sin(rStick1.angleBetween(robotAngle)));
                break;
        }
        normalizeMotorPow();
        refreshMotors();
    }
}
