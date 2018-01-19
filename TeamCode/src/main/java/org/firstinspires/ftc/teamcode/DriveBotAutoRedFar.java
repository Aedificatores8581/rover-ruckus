package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/*
 * Conjured into existence by The Saminator on 11-12-2017.
 */
@Autonomous(name = "Autonomous Red Far", group = "competition bepis")

public class DriveBotAutoRedFar extends DriveBotTestTemplate {

    State state;

    private int cameraMonitorViewId;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;
    private VuforiaLocalizer vuforia;
    private RelicRecoveryVuMark vuMark;

    boolean initServos;

    Gamepad prev1;

    long waitTime = 2000L;
    long prevTime, prevTime2 = 0, totalTime = 0;
    double redColor = 0, blueColor = 0, jewelArmDownPosition = 0.74, jewelArmUpPosition = 0.25, centerFinger = 0.48, speed = -0.15, adjustSpeed = 0.06, dispensePosition = 1.0, retractDispensePosition = 0.0;

    //210 to move forward to left
    //325 to move to mid
    //400 to move to right
    //350 to place glyph

    int timeToDispense, encToDispense = 1375, encToRamGlyph = 1000, encToBackUp = 400, encToBackUpAgain = 360;
    double glyphHold = 0.03, glyphDrop = 0.33;
    double targetAngle = -194;
    double ramLeftMod = 1.0, ramRightMod = 1.0, ramAngle = AutonomousDefaults.RAM_MOTOR_RATIO;

    int encToAlignLeft = 275, encToAlignCenter = 150, encToAlignRight = 45;

    double degrees90 = 85;
    double degreesSmall = 30, degreesRestOfSmall = 120;

    CryptoboxColumn column;
    GyroAngles gyroAngles;
    boolean dispenseGlyph, retractDispenser;
    boolean checkKey, keyChecked;

    // IMPORTANT: THIS OP-MODE WAITS ONE SECOND BEFORE STARTING. THIS MEANS THAT WE HAVE TWENTY-NINE SECONDS TO ACCOMPLISH TASKS, NOT THIRTY.
    public void start() {
        super.start();
        relicTrackables.activate();
        //encToMoveToCenter = encToMoveToLeft + encToChangeColumn;
        //encToMoveToRight = encToMoveToLeft + (encToChangeColumn * 2);
        if (ramAngle > 1.0) {
            ramRightMod = 1.0;
            ramLeftMod = 1.0 / ramAngle;
        } else {
            ramRightMod = ramAngle;
            ramLeftMod = 1.0;
        }

        checkKey = false;
        keyChecked = false;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            telemetry.addData("Exception", e);
        }

        leftFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rIntake.setPosition(0.3);

        lIntake.setPosition(0.7);

        relicHand.setPosition(0.5);

        initServos = false;
    }

    @Override
    protected boolean isAutonomous() {
        return true;
    }

    @Override
    public void init() {
        this.msStuckDetectInit = 10000;
        super.init();

        state = State.STATE_LOWER_JEWEL_ARM;

        rIntake.setPosition(0.3);

        lIntake.setPosition(0.7);

        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VuforiaLicenseKey.LICENSE_KEY; // VuforiaLicenseKey is ignored by git
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

        prevTime = 0;

        dispenseGlyph = false;
        retractDispenser = false;

        leftFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        prev1 = new Gamepad();
        vuMark = RelicRecoveryVuMark.from(relicTemplate);

    }

    @Override
    public void init_loop() {
        if (gamepad1.dpad_up && !prev1.dpad_up)
            speed += 0.01;

        if (gamepad1.dpad_down && !prev1.dpad_down)
            speed -= 0.01;

        if (gamepad1.dpad_right && !prev1.dpad_right)
            adjustSpeed += 0.005;

        if (gamepad1.dpad_left && !prev1.dpad_left)
            adjustSpeed -= 0.005;

        if (triggered(gamepad1.right_trigger) && !triggered(prev1.right_trigger))
            targetAngle += 1;

        if (triggered(gamepad1.left_trigger) && !triggered(prev1.left_trigger))
            targetAngle -= 1;

        if (gamepad1.right_bumper && !prev1.right_bumper)
            encToDispense += 5;

        if (gamepad1.left_bumper && !prev1.left_bumper)
            encToDispense -= 5;

        if (triggered(gamepad1.left_stick_y) && !triggered(prev1.left_stick_y))
            encToBackUp += 5;

        if (triggered(-gamepad1.left_stick_y) && !triggered(-prev1.left_stick_y))
            encToBackUp -= 5;

        if (triggered(gamepad1.left_stick_x) && !triggered(prev1.left_stick_x))
            encToRamGlyph += 5;

        if (triggered(-gamepad1.left_stick_x) && !triggered(-prev1.left_stick_x))
            encToRamGlyph -= 5;

        if (triggered(gamepad1.right_stick_y) && !triggered(prev1.right_stick_y))
            encToBackUpAgain += 5;

        if (triggered(-gamepad1.right_stick_y) && !triggered(-prev1.right_stick_y))
            encToBackUpAgain -= 5;

        if (triggered(gamepad1.right_stick_x) && !triggered(prev1.right_stick_x))
            ramAngle += 0.05;

        if (triggered(-gamepad1.right_stick_x) && !triggered(-prev1.right_stick_x))
            ramAngle -= 0.05;

        telemetry.addData("Driving Speed (DPad up/down)", speed);
        telemetry.addData("Turning Speed (DPad right/left)", adjustSpeed);
        telemetry.addData("Target Angle Degrees (Right/left triggers)", targetAngle);
        telemetry.addData("Distance to Dispense Glyph (Right/left bumpers)", encToDispense);
        telemetry.addData("Distance to Back Up First (Left stick up/down)", encToBackUp);
        telemetry.addData("Distance to Ram Glyph (Left stick right/left)", encToRamGlyph);
        telemetry.addData("Distance to Back Up Final (Right stick up/down)", encToBackUpAgain);
        telemetry.addData("Angle to Ram Glyph Second (Right Speed Mult) (Right stick right/left)", ramAngle);

        try {
            prev1.copy(gamepad1);
        } catch (RobotCoreException e) {
            telemetry.addData("Exception", e);
        }
    }

    @Override
    public void loop() {
        NormalizedRGBA colors = color.getNormalizedColors();
        double redRatio = colors.red / (colors.red + colors.green + colors.blue);
        double blueRatio = colors.blue / (colors.red + colors.green + colors.blue);

        if (!initServos) {
            initServos = true;

            rIntake.setPosition(0.7);

            lIntake.setPosition(0.3);

            relicHand.setPosition(0.5);
        }

        switch (state) {
            case STATE_LOWER_JEWEL_ARM:
                checkKey = true;
                jewelFlipper.setPosition(centerFinger);
                jewelArm.setPosition(jewelArmDownPosition);
                if (prevTime == 0)
                    prevTime = System.currentTimeMillis();
                if (System.currentTimeMillis() - prevTime >= waitTime)
                    state = State.STATE_SCAN_JEWEL;
                break;
            case STATE_SCAN_JEWEL:
                prevTime = 0;
                glyphOutput.setPosition(/*Constants.GLYPH_DISPENSE_LEVEL*/ 0.3);
                if (redRatio > Constants.RED_THRESHOLD)
                    state = State.STATE_HIT_LEFT_JEWEL;
                else if (blueRatio >= Constants.BLUE_THRESHOLD)
                    state = State.STATE_HIT_RIGHT_JEWEL;
                else if (System.currentTimeMillis() - totalTime >= 5000)
                    state = State.STATE_RESET_JEWEL_HITTER;

                break;
            case STATE_HIT_LEFT_JEWEL:
                jewelFlipper.setPosition(0.05);
                if (prevTime == 0)
                    prevTime = System.currentTimeMillis();
                if (System.currentTimeMillis() - prevTime >= waitTime)
                    state = State.STATE_RESET_JEWEL_HITTER;
                break;
            case STATE_HIT_RIGHT_JEWEL:
                jewelFlipper.setPosition(1.0);
                if (prevTime == 0)
                    prevTime = System.currentTimeMillis();
                if (System.currentTimeMillis() - prevTime >= waitTime)
                    state = State.STATE_RESET_JEWEL_HITTER;
                break;
            case STATE_RESET_JEWEL_HITTER:
                prevTime = 0;
                jewelArm.setPosition(jewelArmUpPosition);
                state = State.STATE_SCAN_KEY;
                break;
            case STATE_SCAN_KEY:
                if (!keyChecked)
                    column = CryptoboxColumn.MID;
                state = State.STATE_GYRO_ANGLES;
                break;
            case STATE_GYRO_ANGLES:
                gyroAngles = new GyroAngles(angles);
                switch (column) {
                    case LEFT:
                        setLeftPow(-adjustSpeed);
                        setRightPow(adjustSpeed);
                        state = State.STATE_L_TURN_90;
                        break;
                    case MID:
                        setLeftPow(speed);
                        setRightPow(speed);
                        state = State.STATE_C_APPROACH_CRYPTOBOX;
                        break;
                    case RIGHT:
                        setLeftPow(speed);
                        setRightPow(speed);
                        state = State.STATE_R_APPROACH_CRYPTOBOX;
                        break;
                }
                break;
            //<editor-fold desc="Left column">
            case STATE_L_TURN_90:
                if (gyroAngles.getZ() - (new GyroAngles(angles).getZ()) >= degrees90) {
                    gyroAngles = new GyroAngles(angles);
                    resetEncoders();
                    reinitMotors(speed, speed);
                    state = State.STATE_L_ALIGN_TO_CRYPTOBOX;
                }
                break;
            case STATE_L_ALIGN_TO_CRYPTOBOX:
                if (checkEncoders(encToAlignLeft)) {
                    setLeftPow(-adjustSpeed);
                    setRightPow(adjustSpeed);
                    state = State.STATE_L_TURN_90_BACK;
                }
                break;
            case STATE_L_TURN_90_BACK:
                if (gyroAngles.getZ() - (new GyroAngles(angles).getZ()) >= degrees90) {
                    gyroAngles = new GyroAngles(angles);
                    resetEncoders();
                    reinitMotors(-speed, -speed);
                    state = State.STATE_L_APPROACH_CRYPTOBOX;
                }
                break;
            case STATE_L_APPROACH_CRYPTOBOX:
                if (checkEncoders(encToDispense)) {
                    resetEncoders();
                    state = State.STATE_REINIT_MOTORS;
                }
                break;
            //</editor-fold>
            //<editor-fold desc="Center column">
            case STATE_C_APPROACH_CRYPTOBOX:
                if (checkEncoders(encToDispense / 2)) {
                    setLeftPow(-adjustSpeed);
                    setRightPow(adjustSpeed);
                    state = State.STATE_C_TURN_90;
                }
                break;
            case STATE_C_TURN_90:
                if (gyroAngles.getZ() - (new GyroAngles(angles).getZ()) >= degrees90) {
                    gyroAngles = new GyroAngles(angles);
                    resetEncoders();
                    reinitMotors(speed, speed);
                    state = State.STATE_C_ALIGN_TO_CRYPTOBOX;
                }
                break;
            case STATE_C_ALIGN_TO_CRYPTOBOX:
                if (checkEncoders(encToAlignCenter)) {
                    setLeftPow(-adjustSpeed);
                    setRightPow(adjustSpeed);
                    state = State.STATE_C_TURN_90_BACK;
                }
                break;
            case STATE_C_TURN_90_BACK:
                if (gyroAngles.getZ() - (new GyroAngles(angles).getZ()) >= degrees90) {
                    gyroAngles = new GyroAngles(angles);
                    resetEncoders();
                    reinitMotors(-speed, -speed);
                    state = State.STATE_C_APPROACH_CRYPTOBOX;
                }
                break;
            case STATE_C_MEET_CRYPTOBOX:
                if (checkEncoders(encToDispense / 2)) {
                    resetEncoders();
                    state = State.STATE_REINIT_MOTORS;
                }
                break;
            //</editor-fold>
            //<editor-fold desc="Right column">
            case STATE_R_APPROACH_CRYPTOBOX:
                if (checkEncoders(encToDispense / 2)) {
                    setLeftPow(-adjustSpeed);
                    setRightPow(adjustSpeed);
                    state = State.STATE_R_TURN_A_BIT;
                }
                break;
            case STATE_R_TURN_A_BIT:
                if (gyroAngles.getZ() - (new GyroAngles(angles).getZ()) >= degreesSmall) {
                    gyroAngles = new GyroAngles(angles);
                    resetEncoders();
                    reinitMotors(speed, speed);
                    state = State.STATE_R_ALIGN_TO_CRYPTOBOX;
                }
                break;
            case STATE_R_ALIGN_TO_CRYPTOBOX:
                if (checkEncoders(encToAlignRight)) {
                    setLeftPow(-adjustSpeed);
                    setRightPow(adjustSpeed);
                    state = State.STATE_R_TURN_BACK;
                }
                break;
            case STATE_R_TURN_BACK:
                if (gyroAngles.getZ() - (new GyroAngles(angles).getZ()) >= degreesRestOfSmall) {
                    resetEncoders();
                    reinitMotors(-speed, -speed);
                    state = State.STATE_R_APPROACH_CRYPTOBOX;
                }
                break;
            case STATE_R_MEET_CRYPTOBOX:
                if (checkEncoders(encToDispense / 2)) {
                    resetEncoders();
                    state = State.STATE_REINIT_MOTORS;
                }
                break;
            //</editor-fold>
            case STATE_REINIT_MOTORS:
                reinitMotors(speed, speed);
                state = State.STATE_DISPENSE_GLYPH;
                break;
            case STATE_DISPENSE_GLYPH:
                if (checkEncoders(encToDispense)) {
                    dispenseGlyph = true;
                    resetEncoders();
                    reinitMotors(-speed, -speed);
                    state = State.STATE_BACK_UP_TO_RAM_GLYPH;
                }
                break;
            case STATE_BACK_UP_TO_RAM_GLYPH:
                if (prevTime == 0)
                    prevTime = System.currentTimeMillis();
                if (System.currentTimeMillis() - prevTime >= 750) {
                    if (checkEncodersReverse(encToBackUp)) {
                        resetEncoders();
                        reinitMotors(speed * ramLeftMod, speed * ramRightMod);
                        state = State.STATE_RAM_GLYPH_INTO_BOX;
                    }
                }
                break;
            case STATE_RAM_GLYPH_INTO_BOX:
                if (checkEncoders(encToRamGlyph)) {
                    resetEncoders();
                    reinitMotors(-speed * ramLeftMod, -speed * ramRightMod);
                    state = State.STATE_BACK_AWAY_FROM_RAMMED_GLYPH;
                }
                break;
            case STATE_BACK_AWAY_FROM_RAMMED_GLYPH:
                if (prevTime == 0)
                    prevTime = System.currentTimeMillis();
                if (checkEncoders(encToBackUpAgain)) {
                    setLeftPow(0);
                    setRightPow(0);
                    state = State.STATE_END;
                }
                break;
            case STATE_END:
                telemetry.addData("Finished", "Very Yes");
                break;
        }

        if (checkKey) {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            switch (vuMark) {
                case LEFT:
                    column = CryptoboxColumn.LEFT;
                    break;
                case CENTER:
                    column = CryptoboxColumn.MID;
                    break;
                case RIGHT:
                    column = CryptoboxColumn.RIGHT;
                    break;
            }
            if (vuMark != RelicRecoveryVuMark.UNKNOWN)
                keyChecked = true;
        }

        if (dispenseGlyph) {
            if (retractDispenser) {
                glyphOutput.setPosition(retractDispensePosition);
                if (prevTime == 0)
                    prevTime = System.currentTimeMillis();
                if (System.currentTimeMillis() - prevTime >= waitTime)
                    dispenseGlyph = false;
            } else
                glyphOutput.setPosition(dispensePosition);

            if (prevTime == 0)
                prevTime = System.currentTimeMillis();
            if (System.currentTimeMillis() - prevTime >= waitTime)
                retractDispenser = true;
        }

        telemetry.addData("State", state.name());
        telemetry.addData("Red Ratio", redRatio);
        telemetry.addData("Blue Ratio", blueRatio);
        telemetry.addData("Red Color", colors.red);
        telemetry.addData("Blue Color", colors.blue);

        telemetry.addData("Total LF Encoder", leftFore.getCurrentPosition());
        telemetry.addData("Total LR Encoder", leftRear.getCurrentPosition());
        telemetry.addData("Total RF Encoder", rightFore.getCurrentPosition());
        telemetry.addData("Total RR Encoder", rightRear.getCurrentPosition());

        //telemetry.addData("Angle", new GyroAngles(angles).getZ()); // IMPORTANT: DO NOT UNCOMMENT THIS CAUSES A NULL POINTER EXCEPTION!

        telemetry.addData("Jewel Arm Position", jewelArm.getPosition());
        telemetry.addData("Jewel Flipper Position", jewelFlipper.getPosition());
        telemetry.addData("Relic Hand Position", relicHand.getPosition());
        telemetry.addData("Relic Fingers Position", relicFingers.getPosition());
        telemetry.addData("Glyph Output Position", glyphOutput.getPosition());
        telemetry.addData("Right Intake Position", rIntake.getPosition());
        telemetry.addData("Left Intake Position", lIntake.getPosition());

        if (column != null)
            telemetry.addData("Column", column.name());
    }

    // These states MUST have comments describing what they do, when they end, and what the next state is.
    enum State {
        STATE_LOWER_JEWEL_ARM, // Ends when jewel arm is at certain position. Always -> STATE_SCAN_JEWEL
        STATE_SCAN_JEWEL, // Ends when right jewel color is read. Right jewel == blue -> STATE_HIT_LEFT_JEWEL. Right jewel == red -> STATE_HIT_RIGHT_JEWEL
        STATE_HIT_LEFT_JEWEL, // Ends when servo is at position. Always -> STATE_RESET_JEWEL_HITTER
        STATE_HIT_RIGHT_JEWEL, // Ends when servo is at position. Always -> STATE_RESET_JEWEL_HITTER
        STATE_RESET_JEWEL_HITTER, // Ends when servo is at position. Always -> STATE_SCAN_KEY
        STATE_SCAN_KEY, // Ends when, if the Vuforia was not successful yet, the column is assumed to be center. Always -> STATE_GYRO_ANGLES
        STATE_GYRO_ANGLES, // Ends when gyroAngles variable is set to the current angles. Key { left -> STATE_L_TURN_90; center -> STATE_C_APPROACH_CRYPTOBOX; right -> STATE_R_APPROACH_CRYPTOBOX }

        /* Old stuff
        STATE_FIRST_TURN, // Ends when motor powers are set. Always -> STATE_CHECK_SLOT
        STATE_CRYPTOBOX_RIGHT_SLOT, // Ends when short-range distance sensor reads cryptobox divider. Key == left -> STATE_DISPENSE_GLYPH. Key == center or right -> STATE_CRYPTOBOX_CENTER_SLOT
        STATE_CRYPTOBOX_CENTER_SLOT, // Ends when short-range distance sensor reads cryptobox divider. Key == center -> STATE_DISPENSE_GLYPH. Key == right -> STATE_CRYPTOBOX_LEFT_SLOT
        STATE_CRYPTOBOX_LEFT_SLOT, // Ends when short-range distance sensor reads cryptobox divider. Always -> STATE_RECORD_FACING.
        STATE_RECORD_FACING, // Ends when current orientation is recorded. Always -> STATE_FACE_CRYPTOBOX
        STATE_FACE_CRYPTOBOX, // Ends when gyro is at angle. Always -> STATE_REINIT_MOTORS*/

        // Left column
        STATE_L_TURN_90, // Ends when the robot has turned 90 degrees. Always -> STATE_L_ALIGN_TO_CRYPTOBOX
        STATE_L_ALIGN_TO_CRYPTOBOX, // Ends when the robot has moved a certain distance. Always -> STATE_L_TURN_90_BACK,
        STATE_L_TURN_90_BACK, // Ends when the robot has turned 90 degrees back. Always -> STATE_L_ALIGN_TO_CRYPTOBOX
        STATE_L_APPROACH_CRYPTOBOX, // Ends when the robot has moved a certain distance. Always -> STATE_REINIT_MOTORS

        // Center column
        STATE_C_APPROACH_CRYPTOBOX, // Ends when the robot has moved a certain distance. Always -> STATE_C_TURN_90
        STATE_C_TURN_90, // Ends when the robot has turned 90 degrees. Always -> STATE_C_ALIGN_TO_CRYPTOBOX
        STATE_C_ALIGN_TO_CRYPTOBOX, // Ends when the robot has moved a certain distance. Always -> STATE_C_TURN_90_BACK,
        STATE_C_TURN_90_BACK, // Ends when the robot has turned 90 degrees back. Always -> STATE_C_ALIGN_TO_CRYPTOBOX
        STATE_C_MEET_CRYPTOBOX, // Ends when the robot has moved a certain distance. Always -> STATE_REINIT_MOTORS

        // Right column
        STATE_R_APPROACH_CRYPTOBOX, // Ends when the robot has moved a certain distance. Always -> STATE_R_TURN_A_BIT
        STATE_R_TURN_A_BIT, // Ends when the robot has turned 45 degrees. Always -> STATE_R_ALIGN_TO_CRYPTOBOX
        STATE_R_ALIGN_TO_CRYPTOBOX, // Ends when the robot has moved a certain small distance. Always -> STATE_R_TURN_BACK
        STATE_R_TURN_BACK, // Ends when the robot has turned 45 degrees. Always -> STATE_R_MEET_CRYPTOBOX
        STATE_R_MEET_CRYPTOBOX, // Ends when the robot has moved a certain distance. Always -> STATE_REINIT_MOTORS

        STATE_REINIT_MOTORS, // Ends when the motors' mode is RUN_USING_ENCODER. Always -> STATE_DISPENSE_GLYPH
        STATE_DISPENSE_GLYPH, // Ends when glyph is dispensed. Always -> STATE_WAIT_FOR_GLYPH_DISPENSED
        STATE_BACK_UP_TO_RAM_GLYPH, // Ends when motors are at position. Always -> STATE_RAM_GLYPH_INTO_BOX
        STATE_RAM_GLYPH_INTO_BOX, // Ends when motors are at position. Always -> STATE_BACK_AWAY_FROM_RAMMED_GLYPH
        STATE_BACK_AWAY_FROM_RAMMED_GLYPH, // Ends when motors are at position. Always -> STATE_END
        STATE_END // Ends when the universe dies. Always -> STATE_RESURRECT_UNIVERSE
        // STATE_RESURRECT_UNIVERSE // uncomment when we have the technology to reverse entropy.
    }

}
