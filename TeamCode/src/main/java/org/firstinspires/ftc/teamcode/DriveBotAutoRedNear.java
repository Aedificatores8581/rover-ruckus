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
 * ..
 */
@Autonomous(name = "Autonomous Red Near", group = "competition bepis")

public class DriveBotAutoRedNear extends DriveBotTestTemplate {

    State state;

    private int cameraMonitorViewId;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;
    private VuforiaLocalizer vuforia;
    private RelicRecoveryVuMark vuMark;

    Gamepad prev1;

    long waitTime = 500L;
    long prevTime;
    double redColor = 0, blueColor = 0, jewelArmDownPosition = 0.7, jewelArmUpPosition = 0.25, jewelFlipperUp = 0.6, centerFinger = 0.66, speed = 0.15, adjustSpeed = 0.06, dispensePosition, retractDispensePosition;
    int timeToDispense, encToDispense = 480, encToRamGlyph = 500, encToBackUp = 100, encToBackUpAgain = 200, encToMoveToLeft = 450, encToChangeColumn = 320, encToMoveToCenter, encToMoveToRight;
    double glyphHold = 0.03, glyphDrop = 0.33;
    double targetAngle = 30;
    double ramLeftMod, ramRightMod, ramAngle = AutonomousDefaults.RAM_MOTOR_RATIO;
    CryptoboxColumn column;
    GyroAngles gyroAngles;
    boolean dispenseGlyph, retractDispenser;


    // IMPORTANT: THIS OP-MODE WAITS ONE SECOND BEFORE STARTING. THIS MEANS THAT WE HAVE TWENTY-NINE SECONDS TO ACCOMPLISH TASKS, NOT THIRTY.
    public void start() {
        super.start();
        relicTrackables.activate();
        encToMoveToCenter = encToMoveToLeft + encToChangeColumn;
        encToMoveToRight = encToMoveToLeft + (encToChangeColumn * 2);
        if (ramAngle > 1.0) {
            ramRightMod = 1.0;
            ramLeftMod = 1.0 / ramAngle;
        } else {
            ramRightMod = ramAngle;
            ramLeftMod = 1.0;
        }

        try {
            Thread.sleep(1000);

            leftFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        } catch (InterruptedException e) {
            telemetry.addData("Exception", e);
        }
    }

    @Override
    protected boolean isAutonomous() {
        return true;
    }

    @Override
    public void init() {
        this.msStuckDetectInit = 10000;
        super.init();
        
        rIntake.setPosition(0.3);

        lIntake.setPosition(0.3);

        state = State.STATE_LOWER_JEWEL_ARM;

        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VuforiaLicenseKey.LICENSE_KEY; // VuforiaLicenseKey is ignored by git
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
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

        if (gamepad1.a && !prev1.a)
            encToMoveToLeft += 10;

        if (gamepad1.b && !prev1.b)
            encToMoveToLeft -= 10;

        if (gamepad1.x && !prev1.x)
            encToChangeColumn += 10;

        if (gamepad1.y && !prev1.y)
            encToChangeColumn -= 10;

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
        telemetry.addData("Distance to Nearest Cryptobox (A/B)", encToMoveToLeft);
        telemetry.addData("Distance to Next Cryptobox Column (X/Y)", encToChangeColumn);
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
        rIntake.setPosition(0.6);

        lIntake.setPosition(0.4);

        NormalizedRGBA colors = color.getNormalizedColors();
        double redRatio = colors.red / (colors.red + colors.green + colors.blue);
        double blueRatio = colors.blue / (colors.red + colors.green + colors.blue);
        switch (state) {
            case STATE_LOWER_JEWEL_ARM:
                jewelFlipper.setPosition(centerFinger);
                jewelArm.setPosition(jewelArmDownPosition);
                if (prevTime == 0)
                    prevTime = System.currentTimeMillis();
                if (System.currentTimeMillis() - prevTime >= waitTime)
                    state = State.STATE_SCAN_JEWEL;
                break;
            case STATE_SCAN_JEWEL:
                prevTime = 0;
                if (redRatio > blueRatio)
                    state = State.STATE_HIT_LEFT_JEWEL;
                else if (redRatio < blueRatio)
                    state = State.STATE_HIT_RIGHT_JEWEL;
                break;
            case STATE_HIT_LEFT_JEWEL:
                jewelFlipper.setPosition(0.05);
                //this could be jewelFlipper.setPosition(0); depending on the side of the arm the servo is mounted
                if (prevTime == 0)
                    prevTime = System.currentTimeMillis();
                if (System.currentTimeMillis() - prevTime >= waitTime)
                    state = State.STATE_RESET_JEWEL_HITTER;
                break;
            case STATE_HIT_RIGHT_JEWEL:
                jewelFlipper.setPosition(0.95);
                //this could be jewelFlipper.setPosition(0); depending on the side of the arm the servo is mounted
                if (prevTime == 0)
                    prevTime = System.currentTimeMillis();
                if (System.currentTimeMillis() - prevTime >= waitTime)
                    state = State.STATE_RESET_JEWEL_HITTER;
                break;
            case STATE_RESET_JEWEL_HITTER:
                prevTime = 0;
                jewelFlipper.setPosition(centerFinger);
                jewelArm.setPosition(jewelArmUpPosition);
                state = State.STATE_SCAN_KEY;
                break;
            case STATE_SCAN_KEY:
                vuMark = RelicRecoveryVuMark.from(relicTemplate);
                switch (vuMark) { // Blue is weird.
                    case LEFT:
                        column = CryptoboxColumn.RIGHT;
                        break;
                    case CENTER:
                        column = CryptoboxColumn.MID;
                        break;
                    case RIGHT:
                        column = CryptoboxColumn.LEFT;
                        break;
                }
                if (vuMark != RelicRecoveryVuMark.UNKNOWN)
                    state = State.STATE_DRIVE_TO_CRYPTOBOX;
                break;
            case STATE_DRIVE_TO_CRYPTOBOX:
                setLeftPow(speed);
                setRightPow(speed);
                //use the distance sensor to read one shelf
                state = state.STATE_CRYPTOBOX_RIGHT_SLOT;
                break;
            case STATE_CRYPTOBOX_RIGHT_SLOT:
                if (true/*if one column is sensed*/) {
                    if (column == CryptoboxColumn.RIGHT)
                        state = State.STATE_RECORD_FACING;
                    else
                        state = State.STATE_CRYPTOBOX_CENTER_SLOT;
                }
                break;
            case STATE_CRYPTOBOX_CENTER_SLOT:
                if (true/*if a second column is sensed*/) {
                    if (column == CryptoboxColumn.MID)
                        state = State.STATE_RECORD_FACING;
                    else
                        state = State.STATE_CRYPTOBOX_LEFT_SLOT;
                }
                break;
            case STATE_CRYPTOBOX_LEFT_SLOT:
                if (true/*if a third column is sensed*/) {
                    state = State.STATE_RECORD_FACING;
                }
                break;
            case STATE_RECORD_FACING:
                gyroAngles = new GyroAngles(angles);
                state = State.STATE_FACE_CRYPTOBOX;
                break;
            case STATE_FACE_CRYPTOBOX:
                setLeftPow(adjustSpeed);
                setRightPow(-adjustSpeed);
                if (gyroAngles.getZ() - (new GyroAngles(angles).getZ()) <= -targetAngle) {
                    //resetEncoders();
                    state = State.STATE_REINIT_MOTORS;
                }
                break;
            case STATE_REINIT_MOTORS:
                //reinitMotors(-speed, -speed);
                state = State.STATE_DISPENSE_GLYPH;
                break;
            case STATE_DISPENSE_GLYPH:
                if (checkEncoder(encToDispense)) {
                    dispenseGlyph = true;
                    setLeftPow(speed);
                    setRightPow(speed);
                    state = State.STATE_BACK_UP_TO_RAM_GLYPH;
                }
                break;
            case STATE_BACK_UP_TO_RAM_GLYPH:
                if (checkEncodersReverse(encToBackUp)) {
                    setLeftPow(-speed * ramLeftMod);
                    setRightPow(-speed * ramRightMod);
                    state = State.STATE_RAM_GLYPH_INTO_BOX;
                }
                break;
            case STATE_RAM_GLYPH_INTO_BOX:
                if (checkEncoder(encToRamGlyph)) {
                    setLeftPow(speed * ramLeftMod);
                    setRightPow(speed * ramRightMod);
                    state = State.STATE_BACK_AWAY_FROM_RAMMED_GLYPH;
                }
                break;
            case STATE_BACK_AWAY_FROM_RAMMED_GLYPH:
                if (checkEncodersReverse(encToBackUpAgain)) {
                    setLeftPow(0);
                    setRightPow(0);
                    state = State.STATE_END;
                }
                break;
            case STATE_END:
                telemetry.addData("Finished", "Very Yes");
                break;
        }


        /*if (dispenseGlyph) {
            glyphDispense.setPosition(dispensePosition);
            if (prevTime == 0)
                prevTime = System.currentTimeMillis();
            if (System.currentTimeMillis() - prevTime >= waitTime)
                retractDispenser = true;

            if (retractDispenser) {

                glyphDispense.setPosition(retractDispensePosition);
                if (prevTime == 0)
                    prevTime = System.currentTimeMillis();
                if (System.currentTimeMillis() - prevTime >= waitTime)
                    dispenseGlyph = false;

            }

        }*/

        telemetry.addData("State", state.name());
        telemetry.addData("Red Ratio", redRatio);
        telemetry.addData("Blue Ratio", blueRatio);
        telemetry.addData("Red Color", colors.red);
        telemetry.addData("Blue Color", colors.blue);

        telemetry.addData("Total LF Encoder", leftFore.getCurrentPosition());
        telemetry.addData("Total LR Encoder", leftRear.getCurrentPosition());
        telemetry.addData("Total RF Encoder", rightFore.getCurrentPosition());
        telemetry.addData("Total RR Encoder", rightRear.getCurrentPosition());

        if (column != null)
            telemetry.addData("Column", column.name());
    }

    enum State {
        STATE_SCAN_KEY, // Ends when we get a successful scan. Always -> STATE_LOWER_JEWEL_ARM
        STATE_LOWER_JEWEL_ARM, // Ends when jewel arm is at certain position. Always -> STATE_SCAN_JEWEL
        STATE_SCAN_JEWEL, // Ends when right jewel color is read. Right jewel == blue -> STATE_HIT_LEFT_JEWEL. Right jewel == red -> STATE_HIT_RIGHT_JEWEL
        STATE_HIT_LEFT_JEWEL, // Ends when servo is at position. Always -> STATE_RESET_JEWEL_HITTER
        STATE_HIT_RIGHT_JEWEL, // Ends when servo is at position. Always -> STATE_RESET_JEWEL_HITTER
        STATE_RESET_JEWEL_HITTER, // Ends when servo is at position. Always -> STATE_DRIVE_TO_CRYPTOBOX
        STATE_DRIVE_TO_CRYPTOBOX, // Ends when short-range distance sensor reads cryptobox divider. Always -> STATE_CRYPTOBOX_RIGHT_SLOT
        STATE_CRYPTOBOX_RIGHT_SLOT, // Ends when short-range distance sensor reads cryptobox divider. Key == left -> STATE_DISPENSE_GLYPH. Key == center or right -> STATE_CRYPTOBOX_CENTER_SLOT
        STATE_CRYPTOBOX_CENTER_SLOT, // Ends when short-range distance sensor reads cryptobox divider. Key == center -> STATE_DISPENSE_GLYPH. Key == right -> STATE_CRYPTOBOX_LEFT_SLOT
        STATE_CRYPTOBOX_LEFT_SLOT, // Ends when short-range distance sensor reads cryptobox divider. Always -> STATE_RECORD_FACING.
        STATE_RECORD_FACING, // Ends when current orientation is recorded. Always -> STATE_FACE_CRYPTOBOX
        STATE_FACE_CRYPTOBOX, // Ends when gyro is at angle. Always -> STATE_REINIT_MOTORS
        STATE_REINIT_MOTORS, // Ends when the motors' mode is RUN_USING_ENCODER. Always -> STATE_DISPENSE_GLYPH
        STATE_DISPENSE_GLYPH, // Ends when glyph is dispensed. Always -> STATE_BACK_UP_TO_RAM_GLYPH
        STATE_BACK_UP_TO_RAM_GLYPH, // Ends when motors are at position. Always -> STATE_RAM_GLYPH_INTO_BOX
        STATE_RAM_GLYPH_INTO_BOX, // Ends when motors are at position. Always -> STATE_BACK_AWAY_FROM_RAMMED_GLYPH
        STATE_BACK_AWAY_FROM_RAMMED_GLYPH, // Ends when motors are at position. Always -> STATE_END
        STATE_END // Ends when the universe dies. Always -> STATE_RESURRECT_UNIVERSE
        // STATE_RESURRECT_UNIVERSE // uncomment when we have the technology to reverse entropy.
    }

}
