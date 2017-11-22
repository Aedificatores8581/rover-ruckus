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

    private double jewelArmServoValue, jewelFlipperServoValue, relicHandServoValue, relicFingersServoValue;

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

    @Override
    public void start() {
        jewelArmServoValue = 0.71;
        jewelFlipperServoValue = 0.05;
        relicHandServoValue = 0.29;
        relicFingersServoValue = 0.9;
    }

    protected void toggleSpeed() {
        if (speedMult > 0.5)
            speedMult = 0.3;
        else
            speedMult = 0.7;
    }

    protected void clampJewelArmServo() {
        if (jewelArmServoValue > 0.71) // Maximum position
            jewelArmServoValue = 0.71;
        if (jewelArmServoValue < 0.25) // Minimum position
            jewelArmServoValue = 0.25;
    }

    protected void clampJewelFlipperServo() {
        if (jewelFlipperServoValue > 0.95)
            jewelFlipperServoValue = 0.95;
        if (jewelFlipperServoValue < 0.05)
            jewelFlipperServoValue = 0.05;
    }

    protected void clampRelicHandServo() {
        if (relicHandServoValue > 0.50) // Maximum position
            relicHandServoValue = 0.50;
        if (relicHandServoValue < 0.29) // Minimum position
            relicHandServoValue = 0.29;
    }

    protected void clampRelicFingersServo() {
        if (relicFingersServoValue > 0.9) // Maximum position
            relicFingersServoValue = 0.9;
        if (relicFingersServoValue < 0.58) // Minimum position
            relicFingersServoValue = 0.58;
    }

    protected void refreshServos() {
        jewelArm.setPosition(jewelArmServoValue);
        jewelFlipper.setPosition(jewelFlipperServoValue);
        relicHand.setPosition(relicHandServoValue);
        relicFingers.setPosition(relicFingersServoValue);
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

        if (gamepad1.dpad_down) {
            jewelArmServoValue += 0.01;
            clampJewelArmServo();
        }

        if (gamepad1.dpad_up) {
            jewelArmServoValue -= 0.01;
            clampJewelArmServo();
        }

        if (gamepad1.dpad_left) {
            jewelFlipperServoValue += 0.01;
            clampJewelArmServo();
        }

        if (gamepad1.dpad_right) {
            jewelFlipperServoValue -= 0.01;
            clampJewelFlipperServo();
        }

        relicHandServoValue += gamepad2.right_stick_y * 0.02;
        clampRelicHandServo();

        if (gamepad2.left_bumper) {
            relicFingersServoValue -= 0.01;
            clampRelicFingersServo();
        }

        if (gamepad2.right_bumper) {
            relicFingersServoValue += 0.01;
            clampRelicFingersServo();
        }

        telemetry.addData("Arm extended", armExtended);

        telemetry.addData("Left front power", leftFore.getPower());
        telemetry.addData("Left back power", leftRear.getPower());
        telemetry.addData("Right front power", rightFore.getPower());
        telemetry.addData("Right back power", rightRear.getPower());

        telemetry.addData("Left front encoder", leftFore.getCurrentPosition());
        telemetry.addData("Left back encoder", leftRear.getCurrentPosition());
        telemetry.addData("Right front encoder", rightFore.getCurrentPosition());
        telemetry.addData("Right back encoder", rightRear.getCurrentPosition());

        telemetry.addData("Jewel Arm Pos.", jewelArm.getPosition());
        telemetry.addData("Jewel Arm Set Value", jewelArmServoValue);

        telemetry.addData("Jewel Flip. Pos.", jewelFlipper.getPosition());
        telemetry.addData("Jewel Flip. Set Value", jewelFlipperServoValue);

        telemetry.addData("Relic Hand Pos.", relicHand.getPosition());
        telemetry.addData("Relic Hand Set Value", relicHandServoValue);

        telemetry.addData("Relic Fingers Pos.", relicFingers.getPosition());
        telemetry.addData("Relic Fingers Set Value", relicFingersServoValue);

        telemetry.addData("Color Sensor RGB", "[" + colors.red + "," + colors.green + "," + colors.blue + "]");


        try {
            prev1.copy(gamepad1);
            prev2.copy(gamepad2);
        } catch (RobotCoreException e) {
            telemetry.addData("Exception", e);
        }

        refreshServos();
    }
}
