package org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Components.Sensors.MotorEncoder;
import org.firstinspires.ftc.teamcode.Components.Sensors.TouchSensor;
import org.firstinspires.ftc.teamcode.Universal.UniversalConfig;

/**
 * Written by Theodore Lovinski 29/12/2018.
 */

public class MineralLift {
    private MineralContainer mineralContainer;
    private DcMotor leftMotor, rightMotor;
    private MotorEncoder leftEncoder, rightEncoder;
    private TouchSensor limitSwitch;

    private static final double MAX_SPEED = 1;
    // TODO: This value needs to be tuned. {tuning}
    private static final double DUMP_BUCKET_THRESHHOLD = 0;

    public void init(HardwareMap hardwareMap, boolean isAutonomous) {
        leftMotor = hardwareMap.dcMotor.get(UniversalConfig.MINERAL_LIFT_LEFT_MOTOR);
        rightMotor = hardwareMap.dcMotor.get(UniversalConfig.MINERAL_LIFT_RIGHT_MOTOR);

        leftEncoder = new MotorEncoder(leftMotor);
        rightEncoder = new MotorEncoder(rightMotor);

        mineralContainer.init(hardwareMap);
        limitSwitch.init(hardwareMap, UniversalConfig.MINERAL_LIFT_LIMIT_SWITCH);
        leftEncoder.initEncoder();
        rightEncoder.initEncoder();

        mineralContainer.closeCage();
        mineralContainer.articulateDown();
    }

    public void raise(double pow) {
        if (pow < 0 && !limitSwitch.isPressed() && Math.abs(pow) < MAX_SPEED) {
            leftMotor.setPower(pow);
            rightMotor.setPower(pow);
        } else if (pow > 0 && !limitSwitch.isPressed() && Math.abs(pow) < MAX_SPEED) {
            leftMotor.setPower(pow);
            rightMotor.setPower(pow);
        }

        if (leftEncoder.updateEncoder() > DUMP_BUCKET_THRESHHOLD) {
            mineralContainer.openCage();
        } else if (leftEncoder.updateEncoder() > DUMP_BUCKET_THRESHHOLD) {
            mineralContainer.closeCage();
        }
    }
}