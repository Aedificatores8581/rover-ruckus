package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Conjured into existence by The Saminator on 10-01-2017.
 */
@TeleOp(name = "DriveBot Test Tele-Op", group = "this is a test")
public class DriveBotTestTeleop extends DriveBotTestTemplate {
    private Gamepad prev1;
    private Gamepad prev2;

    private double speedMult;

    private boolean lifting;
    private boolean armExtended;

    @Override
    public void init() { // Configuration for this is in the Google Drive
        super.init();
        prev1 = new Gamepad();
        prev2 = new Gamepad();
        speedMult = 0.7;

        armExtended = false;
    }

    protected void toggleSpeed() {
        if (speedMult > 0.5)
            speedMult = 0.3;
        else
            speedMult = 0.7;
    }

    @Override
    public void loop() {
        setLeftPow(gamepad1.left_stick_y * speedMult);
        setRightPow(gamepad1.right_stick_y * speedMult);

        relicArm.setPower(gamepad2.left_stick_y);

        if (gamepad1.a && !prev1.a)
            scream();

        if (gamepad1.left_bumper && !prev1.left_bumper)
            toggleSpeed();

        if (gamepad1.dpad_down && !prev1.dpad_down)
            jewelArm.setPosition(jewelArm.getPosition() + 0.2);

        if (gamepad1.dpad_up && !prev1.dpad_up)
            jewelArm.setPosition(jewelArm.getPosition() - 0.2);

        if (gamepad1.dpad_left && !prev1.dpad_left)
            jewelFlipper.setPosition(0.2);
        else if (gamepad1.dpad_right && !prev1.dpad_right)
            jewelFlipper.setPosition(0.8);
        else
            jewelFlipper.setPosition(0.5);

        if (gamepad2.a && !gamepad1.a) {
            if (relicHand.getPosition() > 0.5)
                relicHand.setPosition(0.25);
            else
                relicHand.setPosition(0.75);
        }

        if (gamepad2.left_bumper && !prev2.left_bumper)
            relicFingers.setPower(0.35);
        else if (gamepad2.right_bumper && !prev2.right_bumper)
            relicFingers.setPower(-0.35);
        else
            relicFingers.setPower(0.0);

        telemetry.addData("Arm extended", armExtended);

        telemetry.addData("Left front encoder", leftFore.getCurrentPosition());
        telemetry.addData("Left back encoder", leftRear.getCurrentPosition());
        telemetry.addData("Right front encoder", rightFore.getCurrentPosition());
        telemetry.addData("Right back encoder", rightRear.getCurrentPosition());

        telemetry.addData("Jewel Arm Pos.", jewelArm.getPosition());
        telemetry.addData("Jewel Flip. Pos.", jewelFlipper.getPosition());
        telemetry.addData("Relic Hand Pos.", relicHand.getPosition());

        telemetry.addData("Color Sensor RGB", "[" + color.red() + "," + color.green() + "," + color.blue() + "]");

        try {
            prev1.copy(gamepad1);
            prev2.copy(gamepad2);
        } catch (RobotCoreException e) {
            telemetry.addData("Exception", e);
        }

        super.loop();
    }
}
