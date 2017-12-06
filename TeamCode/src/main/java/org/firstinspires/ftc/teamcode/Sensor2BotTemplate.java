package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Conjured into existence by The Saminator on 12-05-2017.
 */
public abstract class Sensor2BotTemplate extends OpMode {
    private TouchSensor touch;
    private DcMotor lm, rm;
    private OpticalDistanceSensor ods;

    public static class Constants {
        public static final double MOTOR_POWER = 1.0;
        public static final DcMotor.Direction LM_DIR = DcMotorSimple.Direction.FORWARD;
        public static final DcMotor.Direction RM_DIR = DcMotorSimple.Direction.REVERSE;
    }

    @Override
    public void init() {
        touch = hardwareMap.touchSensor.get("touch");
        lm = hardwareMap.dcMotor.get("lm");
        rm = hardwareMap.dcMotor.get("rm");
        ods = hardwareMap.opticalDistanceSensor.get("ods");

        lm.setDirection(Constants.LM_DIR);
        rm.setDirection(Constants.RM_DIR);
    }
}
