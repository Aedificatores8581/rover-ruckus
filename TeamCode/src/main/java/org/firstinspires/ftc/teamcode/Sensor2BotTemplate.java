package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Const;

/**
 * Conjured into existence by The Saminator on 12-05-2017.
 */
public abstract class Sensor2BotTemplate extends OpMode {
    protected DigitalChannel touch;
    protected PingSensor ping;
    protected SharpIRSensor ir;
    protected DcMotor lm, rm;
    protected Servo relicHand, relicFingers; // rah, raf
    protected DcMotor relicArm;

    public static class Constants {
        public static final double MOTOR_POWER = 0.25;

        public static final double RELIC_MOTOR_POWER = 0.5;
        public static final double RELIC_HAND_DELTA_MULT = 0.125;
        public static final double RELIC_FINGERS_DELTA = 0.0625;

        public static final DcMotor.Direction LM_DIR = DcMotorSimple.Direction.FORWARD;
        public static final DcMotor.Direction RM_DIR = DcMotorSimple.Direction.REVERSE;
        public static final DcMotor.Direction RA_DIR = DcMotorSimple.Direction.REVERSE;
    }

    @Override
    public void init() {
        touch = hardwareMap.digitalChannel.get("touch");

        ping = new PingSensor(hardwareMap.digitalChannel.get("ping"));
        ir = new SharpIRSensor(hardwareMap.analogInput.get("ir"));

        lm = hardwareMap.dcMotor.get("lm");
        rm = hardwareMap.dcMotor.get("rm");

        relicArm = hardwareMap.dcMotor.get("ram");
        relicHand = hardwareMap.servo.get("rah");
        relicFingers = hardwareMap.servo.get("raf");

        touch.setMode(DigitalChannel.Mode.INPUT);
        lm.setDirection(Constants.LM_DIR);
        rm.setDirection(Constants.RM_DIR);
        relicArm.setDirection(Constants.RA_DIR);
    }

    public void go() {
        lm.setPower(Constants.MOTOR_POWER);
        rm.setPower(Constants.MOTOR_POWER);
    }

    public void stop() {
        lm.setPower(0.0);
        rm.setPower(0.0);
    }

    protected boolean triggered(double value) {
        return value >= 0.05;
    }
}
