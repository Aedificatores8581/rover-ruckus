package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
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

    long waitTime = 2000L;
    long prevTime;
    double redColor = 0, blueColor = 0, jewelArmDownPosition = 0.25, jewelArmUpPosition = 0.71, jewelFlipperUp = 0.6, centerFinger = 0.5, speed = 0.15, adjustLeftSpeed = 0.075, adjustRightSpeed = -0.075;
    int encToDispenseLeft = 2205, encToDispenseRight = 1975, encToAdjustLeft = 2205, encToAdjustRight = 1975, encToMoveToLeft = 1370, encToMoveToCenter = 1730, encToMoveToRight = 2090;
    CryptoboxColumn column;

    // IMPORTANT: THIS OP-MODE WAITS ONE SECOND BEFORE STARTING. THIS MEANS THAT WE HAVE TWENTY-NINE SECONDS TO ACCOMPLISH TASKS, NOT THIRTY.
    public void start() {
        super.start();
        relicTrackables.activate();

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
                    default:
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
                        state = State.STATE_FACE_CRYPTOBOX;
                    else
                        state = State.STATE_CRYPTOBOX_CENTER_SLOT;
                }
                break;
            case STATE_CRYPTOBOX_CENTER_SLOT:
                if (checkEncoder(encToMoveToCenter)) {
                    if (column == CryptoboxColumn.CENTER)
                        state = State.STATE_FACE_CRYPTOBOX;
                    else
                        state = State.STATE_CRYPTOBOX_RIGHT_SLOT;
                }
                break;
            case STATE_CRYPTOBOX_RIGHT_SLOT:
                if (checkEncoder(encToMoveToRight)) {
                    state = State.STATE_FACE_CRYPTOBOX;
                }
                break;
            case STATE_FACE_CRYPTOBOX:
                setLeftPow(adjustLeftSpeed);
                setRightPow(adjustRightSpeed);
                if (checkLeftEncoder(encToAdjustLeft /* placeholder value */) || checkRightEncoder(encToAdjustRight /* placeholder value */)) {
                    state = State.STATE_DISPENSE_GLYPH;
                }
                break;
            case STATE_DISPENSE_GLYPH:
                if (checkLeftEncoder(encToDispenseLeft /* placeholder value */) || checkRightEncoder(encToDispenseRight /* placeholder value */)) {
                    // TODO: Dispense the glyph
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
        STATE_FACE_CRYPTOBOX, // Ends when motors are at position. Always -> STATE_DISPENSE_GLYPH
        STATE_DISPENSE_GLYPH, // Ends when glyph is dispensed. Always (unless we are collecting more glyphs) -> STATE_END
        // TODO: Collect more glyph and dispense them to the cryptobox?
        STATE_END // Ends when the universe dies.
    }

}
