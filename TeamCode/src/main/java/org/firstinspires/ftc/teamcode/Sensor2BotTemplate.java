package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Const;

/**
 * Conjured into existence by The Saminator on 12-05-2017.
 */
public abstract class Sensor2BotTemplate extends OpMode {
    protected DigitalChannel touch;
    protected DcMotor lm, rm, wp1, wp2;
    protected OpticalDistanceSensor ods;
    protected CRServo vm;

    public static class Constants {
        public static final double MOTOR_POWER = 0.25;
        public static final DcMotor.Direction LM_DIR = DcMotorSimple.Direction.FORWARD;
        public static final DcMotor.Direction RM_DIR = DcMotorSimple.Direction.REVERSE;
        public static final DcMotor.Direction WP1_DIR = DcMotorSimple.Direction.FORWARD;
        public static final DcMotor.Direction WP2_DIR = DcMotorSimple.Direction.FORWARD;
        public static final DcMotor.Direction VM_DIR = DcMotorSimple.Direction.FORWARD;
    }

    @Override
    public void init() {
        touch = hardwareMap.digitalChannel.get("touch");
        lm = hardwareMap.dcMotor.get("lm");
        rm = hardwareMap.dcMotor.get("rm");
        wp1 = hardwareMap.dcMotor.get("wp1");
        wp2 = hardwareMap.dcMotor.get("wp2");
        vm = hardwareMap.crservo.get("vm");
        ods = hardwareMap.get(OpticalDistanceSensor.class, "ods");

        touch.setMode(DigitalChannel.Mode.INPUT);
        lm.setDirection(Constants.LM_DIR);
        rm.setDirection(Constants.RM_DIR);
        wp1.setDirection(Constants.WP1_DIR);
        wp2.setDirection(Constants.WP2_DIR);
        vm.setDirection(Constants.VM_DIR);
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
