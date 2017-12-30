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
    protected Servo relicHand, relicFingers; // rah, raf
    protected DcMotor relicArm;
    protected DigitalChannel magnetSensor;

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
        relicArm = hardwareMap.dcMotor.get("ram");
        relicHand = hardwareMap.servo.get("rah");
        relicFingers = hardwareMap.servo.get("raf");
        magnetSensor = hardwareMap.digitalChannel.get("ms");
        relicArm.setDirection(Constants.RA_DIR);
    }

    protected boolean triggered(double value) {
        return value >= 0.05;
    }
}
