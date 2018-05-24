package org.firstinspires.ftc.teamcode.robotTemplates;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;


public class SensorBot extends WestCoastDT {
    DcMotor rm, lm;
    double rt, x, y, rad, normAngle, sin, cos, fsTurn;
    int mult;
    boolean turn = false;
    public SensorBot(double brakePow){
        super(brakePow);
    }
    public void init(){
        super.init();

    }
    public void start(){
        super.start();
    }

    @Override
    public void loop(){
        rt = gamepad1.right_trigger;
        switch(cs) {
            case ARCADE:
                x = gamepad1.right_stick_x;
                y = gamepad1.left_stick_y;
                y = -Math.sqrt(x * x + y * y) * UniversalFunctions.round(y);
                turnMult = 1 - Math.abs(y * (1 - turnMult));
                leftPow = y + turnMult * x;
                rightPow = y - turnMult * x;
                break;
            case FIELD_CENTRIC:
                y = gamepad1.left_stick_y;
                x = gamepad1.left_stick_x;
                rad = Math.sqrt(x * x + y * y);
                normAngle = Math.toRadians(normalizeGamepadAngleL(normalizeGyroAngle()));
                if (rad < UniversalConstants.Triggered.STICK) {
                    setLeftPow(0);
                    setRightPow(0);
                } else {
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
                    leftPow = -rad * mult + mult * fsTurn * cos;
                    rightPow = -rad * mult - mult * fsTurn * cos;
                }
                break;
            case TANK:
                leftPow = gamepad1.left_stick_y;
                rightPow = gamepad1.right_stick_y;
                break;
        }
        setLeftPow();
        setRightPow();
    }

    public void setLeftPow(double pow){
        pow *= speed;
        lm.setPower(pow);
    }
    public void setRightPow(double pow){
        pow *= speed;
        rm.setPower(pow);
    }
    @Override
    public DcMotor[] motors(){
        DcMotor[] motors = {rm, lm};
        return motors;
    }
    @Override
    public String[] names(){
        String[] names = {"rm", "lm"};
        return names;
    }
    @Override
    public DcMotor.Direction[] dir(){
        DcMotor.Direction[] dir = {DcMotor.Direction.FORWARD, DcMotor.Direction.REVERSE};
        return dir;
    }
}