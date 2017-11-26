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

    long waitTime = 2000L;
    long prevTime;
    double redColor = 0, blueColor = 0, jewelArmDownPosition = 0.25, jewelArmUpPosition = 0.71, jewelFlipperUp = 0.6, centerFinger = 0.5, speed = 0.15, adjustSpeed = 0.06;
    int encToDispense = 840, encToDispenseLeft, encToDispenseRight, encToMoveToLeft = 1370, encToChangeColumn = 360, encToMoveToCenter, encToMoveToRight;
    int leftEncAfterTurn, rightEncAfterTurn;
    double glyphHold = 0, glyphDrop = 0.33;
    double targetAngle = 75;

    CryptoboxColumn column;
    GyroAngles gyroAngles;

    // IMPORTANT: THIS OP-MODE WAITS ONE SECOND BEFORE STARTING. THIS MEANS THAT WE HAVE TWENTY-NINE SECONDS TO ACCOMPLISH TASKS, NOT THIRTY.
    public void start() {
        super.start();
        relicTrackables.activate();
        encToMoveToCenter = encToMoveToLeft + encToChangeColumn;
        encToMoveToRight = encToMoveToLeft + (encToChangeColumn * 2);

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
    public void init() {
        super.init();
        state = State.STATE_LOWER_JEWEL_ARM;

        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VuforiaLicenseKey.LICENSE_KEY; // VuforiaLicenseKey is ignored by git
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

        prevTime = 0;

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

        if (gamepad1.x && !prev1.x)
            encToDispense += 5;

        if (gamepad1.y && !prev1.y)
            encToDispense -= 5;

        telemetry.addData("Driving Speed (DPad up/down)", speed);
        telemetry.addData("Turning Speed (DPad right/left)", adjustSpeed);
        telemetry.addData("Target Angle Degrees (Left/right triggers)", targetAngle);
        telemetry.addData("Distance to Nearest Cryptobox (A/B)", encToMoveToLeft);
        telemetry.addData("Distance to Next Cryptobox Column (X/Y)", encToChangeColumn);
        telemetry.addData("Distance to Dispense Glyph (Left/right bumpers)", encToDispense);

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
        switch (state) {
            case STATE_LOWER_JEWEL_ARM:
                jewelFlipper.setPosition(jewelFlipperUp);
                jewelArm.setPosition(jewelArmDownPosition);
                glyphOutput.setPosition(glyphHold);
                if (prevTime == 0)
                    prevTime = System.currentTimeMillis();
                if (System.currentTimeMillis() - prevTime >= waitTime)
                    state = State.STATE_SCAN_JEWEL;
                break;
            case STATE_SCAN_JEWEL:
                prevTime = 0;
                if (redRatio > blueRatio)
                    state = State.STATE_HIT_RIGHT_JEWEL;
                else if (redRatio < blueRatio)
                    state = State.STATE_HIT_LEFT_JEWEL;
                break;
            case STATE_HIT_LEFT_JEWEL:
                jewelFlipper.setPosition(0.95);
                //this could be jewelFlipper.setPosition(0); depending on the side of the arm the servo is mounted
                if (prevTime == 0)
                    prevTime = System.currentTimeMillis();
                if (System.currentTimeMillis() - prevTime >= waitTime)
                    state = State.STATE_RESET_JEWEL_HITTER;
                break;
            case STATE_HIT_RIGHT_JEWEL:
                jewelFlipper.setPosition(0.05);
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
                switch (vuMark) {
                    case LEFT:
                        column = CryptoboxColumn.LEFT;
                        break;
                    case CENTER:
                        column = CryptoboxColumn.CENTER;
                        break;
                    case RIGHT:
                        column = CryptoboxColumn.RIGHT;
                        break;
                }
                if (vuMark != RelicRecoveryVuMark.UNKNOWN)
                    state = State.STATE_DRIVE_TO_CRYPTOBOX;
                break;
            case STATE_DRIVE_TO_CRYPTOBOX:
                setLeftPow(speed);
                setRightPow(speed);
                //use the distance sensor to read one shelf
                state = state.STATE_CRYPTOBOX_LEFT_SLOT;
                break;
            case STATE_CRYPTOBOX_LEFT_SLOT:
                if (checkEncoder(encToMoveToLeft)) {
                    if (column == CryptoboxColumn.LEFT)
                        state = State.STATE_RECORD_FACING;
                    else
                        state = State.STATE_CRYPTOBOX_CENTER_SLOT;
                }
                break;
            case STATE_CRYPTOBOX_CENTER_SLOT:
                if (checkEncoder(encToMoveToCenter)) {
                    if (column == CryptoboxColumn.CENTER)
                        state = State.STATE_RECORD_FACING;
                    else
                        state = State.STATE_CRYPTOBOX_RIGHT_SLOT;
                }
                break;
            case STATE_CRYPTOBOX_RIGHT_SLOT:
                if (checkEncoder(encToMoveToRight)) {
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
                    leftEncAfterTurn = leftFore.getCurrentPosition();
                    rightEncAfterTurn = rightFore.getCurrentPosition();
                    setLeftPow(speed);
                    setRightPow(speed);
                    state = State.STATE_DISPENSE_GLYPH;
                }
                break;
            case STATE_DISPENSE_GLYPH:
                if (checkLeftEncoder(encToDispenseLeft) || checkRightEncoder(encToDispenseRight)) {
                    glyphOutput.setPosition(glyphDrop);
                    state = State.STATE_END;
                }
                break;
            // TODO: Implement collection of additional glyphs?
            case STATE_END:
                setLeftPow(0.0);
                setRightPow(0.0);
                telemetry.addData("Finished", "Very Yes");
                break;
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
        STATE_DRIVE_TO_CRYPTOBOX, // Ends when short-range distance sensor reads cryptobox divider. Always -> STATE_CRYPTOBOX_LEFT_SLOT
        STATE_CRYPTOBOX_LEFT_SLOT, // Ends when short-range distance sensor reads cryptobox divider. Key == left -> STATE_DISPENSE_GLYPH. Key == center or right -> STATE_CRYPTOBOX_CENTER_SLOT
        STATE_CRYPTOBOX_CENTER_SLOT, // Ends when short-range distance sensor reads cryptobox divider. Key == center -> STATE_DISPENSE_GLYPH. Key == right -> STATE_CRYPTOBOX_RIGHT_SLOT
        STATE_CRYPTOBOX_RIGHT_SLOT, // Ends when short-range distance sensor reads cryptobox divider. Always -> STATE_DISPENSE_GLYPH.
        STATE_RECORD_FACING, // Ends when current orientation is recorded. Always -> STATE_FACE_CRYPTOBOX
        STATE_FACE_CRYPTOBOX, // Ends when gyro is at angle. Always -> STATE_DISPENSE_GLYPH
        STATE_DISPENSE_GLYPH, // Ends when glyph is dispensed. Always (unless we are collecting more glyphs) -> STATE_END
        // TODO: Collect more glyph and dispense them to the cryptobox?
        STATE_END // Ends when the universe dies.
    }

}
