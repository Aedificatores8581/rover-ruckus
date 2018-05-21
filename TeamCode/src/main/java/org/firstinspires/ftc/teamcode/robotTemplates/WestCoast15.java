package org.firstinspires.ftc.teamcode.robotTemplates;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;

/**
 * Created by Frank Portman on 5/21/2018
 */
public class WestCoast15 extends WestCoastDT{
    DcMotor rf, lf, lr, rr;
    ControlState cs;
    TurnDir td;
    double rt, x, y, turnMult, rad, normAngle, sin, mult, cos, fsTurn, lp, rp, speed;
    boolean turn;
    public WestCoast15(double brakePow, double sped){
        super(brakePow);
        speed = sped;
    }
    public void loop(){
        rt = gamepad1.right_trigger;
        switch(cs) {
            case ARCADE:
                x = gamepad1.right_stick_x;
                y = gamepad1.left_stick_y;
                y = -Math.sqrt(x * x + y * y) * UniversalFunctions.round(y);
                turnMult = 1 - gamepad1.left_stick_y * (1 - super.turnMult);
                setLeftPow((-y - turnMult * x) * speed);
                setRightPow((-y + turnMult * x)* speed);
                break;
            case FIELD_CENTRIC:
                y = gamepad1.left_stick_y;
                x = gamepad1.left_stick_x;
                rad = Math.sqrt(x * x + y * y);
                normAngle = Math.toRadians(normalizeGamepadAngleL(normalizeGyroAngle()));
                if (rad < UniversalConstants.Triggered.STICK)
                    brake();
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
                    lp = -rad * mult - mult * fsTurn * cos;
                    rp = -rad * mult + mult * fsTurn * cos;
                    setLeftPow(lp * speed);
                    setRightPow(rp * speed);
                }
                break;
            case TANK:
                setLeftPow(-gamepad1.left_stick_y * speed);
                setRightPow(-gamepad1.right_stick_y * speed);
                break;
        }
    }
    @Override
    public DcMotor[] motors(){
        DcMotor[] motors = {rf, lf, lr, rr};
        return motors;
    }
    @Override
    public String[] names(){
        String[] names = {"rf", "lf", "lr", "rr"};
        return names;
    }
    @Override
    public DcMotor.Direction[] dir(){
        DcMotor.Direction[] dir = {DcMotor.Direction.FORWARD, DcMotor.Direction.REVERSE, DcMotor.Direction.REVERSE, DcMotor.Direction.FORWARD};
        return dir;
    }
    public void setLeftPow(double pow) {
        lf.setPower(pow);
        lr.setPower(pow);
    }
    public void setRightPow(double pow){
        rf.setPower(pow);
        rr.setPower(pow);
    }
}
