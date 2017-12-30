package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Conjured into existence by The Saminator on 12-05-2017.
 */

@TeleOp(name = "Sensor TeleOp", group = "feelz2thesequel")
public class Sensor2BotTeleop extends Sensor2BotTemplate {
    @Override
    public void start() {
    }

    @Override
    public void loop() {
        double relicHandPos = relicHand.getPosition();
        double relicFingersPos = relicFingers.getPosition();

        lm.setPower(gamepad1.left_stick_y * Constants.MOTOR_POWER);
        rm.setPower(gamepad1.right_stick_y * Constants.MOTOR_POWER);

        relicArm.setPower(gamepad2.right_stick_y);

        if (triggered(Math.abs(gamepad2.left_stick_y)))
            relicHandPos += gamepad2.left_stick_y * Constants.RELIC_HAND_DELTA_MULT;

        if (triggered(gamepad2.right_trigger))
            relicFingersPos += Constants.RELIC_FINGERS_DELTA;
        if (triggered(gamepad2.left_trigger))
            relicFingersPos -= Constants.RELIC_FINGERS_DELTA;

        relicHand.setPosition(relicHandPos);
        relicFingers.setPosition(relicFingersPos);
    }
}
