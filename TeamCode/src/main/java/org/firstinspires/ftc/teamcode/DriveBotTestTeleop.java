package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

/**
 * Conjured into existence by The Saminator on 10-01-2017.
 */
@TeleOp(name = "DriveBot Test Tele-Op", group = "this is a test")
public class DriveBotTestTeleop extends DriveBotTestTemplate {
    private Gamepad prev1;
    private Gamepad prev2;

    private double speedMult;
    private byte armPos = 1;
    private double jewelArmServoValue, jewelFlipperServoValue, relicHandServoValue, relicFingersServoValue, glyphServoValue;
    private boolean lifting, valueChange;
    private boolean armExtended;
    long waiting = 0, waitTime = 500;

    @Override
    protected boolean isAutonomous() {
        return true;
    }

    @Override
    public void init() { // Configuration for this is in the Google Drive
        super.init();
        prev1 = new Gamepad();
        prev2 = new Gamepad();
        speedMult = 0.175;
        armExtended = false;
    }

    @Override
    public void start() {
        jewelArmServoValue = 0.71;
        jewelFlipperServoValue = 0.05;
        speedMult = 0.175;
    }

    protected void toggleSpeed() {
        if (speedMult == 0.7)
            speedMult = 0.175;
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

        if (relicHandServoValue > 0.23) // Maximum position
            relicHandServoValue = 0.23;
        if (relicHandServoValue < 0.139) // Minimum position
            relicHandServoValue = 0.139;
        //0.188 = 0
        //0.23 = 270
        /*
        arm position of servos
        relic hand 270 degrees = 0.25
        relic hand 0 degrees = 0.2
        relic hand start position = 0.165
         */
    }

    protected void clampRelicFingersServo() {
        if (relicFingersServoValue > 0.9) // Maximum position
            relicFingersServoValue = 0.9;
        if (relicFingersServoValue < 0.4) // Minimum position
            relicFingersServoValue = 0.4;
    }
    
    protected void clampGlyphDispenserServo() {
        if (glyphServoValue > 0.33) // Maximum position
            glyphServoValue = 0.33;
    }
    
    protected void refreshServos() {
        jewelArm.setPosition(jewelArmServoValue);
        jewelFlipper.setPosition(jewelFlipperServoValue);
        relicHand.setPosition(relicHandServoValue);
        relicFingers.setPosition(relicFingersServoValue);
        glyphOutput.setPosition(glyphServoValue);
    }

    @Override
    public void loop() {
        setLeftPow(gamepad1.left_stick_y * speedMult);
        setRightPow(gamepad1.right_stick_y * speedMult);

        glyphIntakeLeft.setPower((gamepad1.right_trigger - gamepad1.left_trigger) * 0.75);
        glyphIntakeRight.setPower((gamepad1.right_trigger - gamepad1.left_trigger) * -0.75);

        refreshServos();

        relicArm.setPower(gamepad2.left_stick_y);

        if (gamepad1.right_trigger == 1 && gamepad1.left_trigger == 1){
            leftFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER );
        }

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

        if (gamepad2.b) {
            relicHandServoValue = 0.188;
        }
        
        if (gamepad2.a) {
            relicHandServoValue = 0.23;
            if (waiting == 0)
                waiting = System.currentTimeMillis();
            if (System.currentTimeMillis() - waiting >= waitTime) {
                waiting = 0;
                relicFingersServoValue = 0.9;
            }
        }
        /*
        if (gamepad1.b) {
            double angleValue = new GyroAngles(angles).getZ() - angleAtStart;
            if (angleValue > new GyroAngles(angles).getZ()) {
                setLeftPow(-0.1);
                setRightPow(0.1);
                if (angleValue <= 45) {
                    setLeftPow(0);
                    setRightPow(0);
                }
            } else if (angleValue < new GyroAngles(angles).getZ()) {
                setLeftPow(0.1);
                setRightPow(-0.1);
                if (angleValue >= 45) {
                    setLeftPow(0);
                    setRightPow(0);
                }
            }
        }
        //850 encoder ticks to get off of the platform (600)
        //320 for right column
        //260 to enter
        //600 to turn
        if(gamepad1.x) {
            double angleValue = new GyroAngles(angles).getZ() - angleAtStart;
            if (angleValue > new GyroAngles(angles).getZ()) {
                setLeftPow(-0.1);
                setRightPow(0.1);
                if (angleValue <= 135) {
                    setLeftPow(0);
                    setRightPow(0);
                }
            } else if (angleValue < new GyroAngles(angles).getZ()) {
                setLeftPow(0.1);
                setRightPow(-0.1);
                if (angleValue >= 135) {
                    setLeftPow(0);
                    setRightPow(0);
                }
            }
        }
        */
        if(gamepad2.a == false){
            valueChange = true;
        }
        if(gamepad2.b == false){
            valueChange = true;
        }
        if(gamepad2.a){
            while(valueChange = true) {
                if (armPos > 1)
                    armPos--;
                valueChange = false;
            }
        }
        if(gamepad2.b) {
            while (valueChange = true) {
                if (armPos < 3)
                    armPos++;
                valueChange = false;
            }
        }

        if(armPos == 1)
            relicHandServoValue = 0.5;
        if(armPos == 2)
            relicHandServoValue = 0.36;
        if(armPos == 3)
            relicHandServoValue = 0.0;

        //relicHandServoValue += gamepad2.right_stick_y * 0.005;
        //clampRelicHandServo();
        //down = 0.36
        //0.3 up
        //0.5
        if (gamepad2.left_bumper) {
            relicFingersServoValue -= 0.02;
            clampRelicFingersServo();
        }

        if (gamepad2.right_bumper) {
            relicFingersServoValue += 0.02;
            clampRelicFingersServo();
        }

        if (gamepad2.y) {
            glyphServoValue = 0.33;
            clampGlyphDispenserServo();
        }
        if (gamepad2.x) {
            glyphServoValue = 0.0;
            clampGlyphDispenserServo();
        }

        telemetry.addData("Arm Extended", armExtended);

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
        telemetry.addData("Glyph Dispenser: ", glyphOutput.getPosition());
        NormalizedRGBA colors = color.getNormalizedColors();
        telemetry.addData("Color Sensor RGB", "[" + colors.red + "," + colors.green + "," + colors.blue + "]");
 

        try {
            prev1.copy(gamepad1);
            prev2.copy(gamepad2);
        } catch (RobotCoreException e) {
            telemetry.addData("Exception", e);
        }

    }
}
