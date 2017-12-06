package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Conjured into existence by The Saminator on 12-05-2017.
 */
public abstract class Sensor2BotTemplate extends OpMode {
    protected DigitalChannel touch;
    protected DcMotor lm, rm;
    protected OpticalDistanceSensor ods;

    public static class Constants {
        public static final double MOTOR_POWER = 0.25;
        public static final DcMotor.Direction LM_DIR = DcMotorSimple.Direction.FORWARD;
        public static final DcMotor.Direction RM_DIR = DcMotorSimple.Direction.REVERSE;
    }

    @Override
    public void init() {
        touch = hardwareMap.digitalChannel.get("touch");
        lm = hardwareMap.dcMotor.get("lm");
        rm = hardwareMap.dcMotor.get("rm");
        ods = hardwareMap.opticalDistanceSensor.get("ods");

        touch.setMode(DigitalChannel.Mode.OUTPUT);
        lm.setDirection(Constants.LM_DIR);
        rm.setDirection(Constants.RM_DIR);
    }

    public void go() {
        lm.setPower(Constants.MOTOR_POWER);
        rm.setPower(Constants.MOTOR_POWER);
    }

    public void stop() {
        lm.setPower(0);
        rm.setPower(0);
    }
}
