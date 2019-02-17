package org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Components.Sensors.MotorEncoder;
import org.firstinspires.ftc.teamcode.Universal.UniversalConfig;

public class NewMineralLift {
    private DcMotor liftMotor;
    private MotorEncoder liftEncoder;

    private Servo pivots[];

    public static final double PIVOT_AUTO_INIT_POS = 1.0;
    public static final double PIVOT_TELE_DOWN_POS = 0.0027;
    public static final double PIVOT_TELE_UP_POS = 0.855;

    public void init(HardwareMap hardwareMap){
        liftMotor = hardwareMap.dcMotor.get("lift");
        liftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftEncoder = new MotorEncoder(liftMotor);

        pivots = new Servo[2];

        pivots[0] = hardwareMap.servo.get(UniversalConfig.MINERAL_LIFT_PIVOT[0]);
        pivots[1] = hardwareMap.servo.get(UniversalConfig.MINERAL_LIFT_PIVOT[1]);

    }
    public void lift(double value){
        liftMotor.setPower(value);
    }

    public void initEncoder(){
        liftEncoder.initEncoder();
    }

    public int getLiftEncoder() {
        liftEncoder.updateEncoder();
        return liftEncoder.currentPosition;
    }

    public void resetLiftEncoder() {
        liftEncoder.resetEncoder();
    }

    public void hardResetLiftEncoer() {
        liftEncoder.hardResetEncoder();
    }

    public void articulate(double value){
        pivots[0].setPosition(value);
        pivots[1].setPosition(value);
    }

}
