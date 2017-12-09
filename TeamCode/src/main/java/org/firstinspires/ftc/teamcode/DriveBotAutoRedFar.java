package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Disabled
@Autonomous(name = "Autonomous RedFar", group = "competition bepis")
public class DriveBotAutoRedFar extends DriveBotTestTemplate {

    State state;

    private int cameraMonitorViewId;


    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;
    private VuforiaLocalizer vuforia;
    private RelicRecoveryVuMark vuMark;
    double redColor = 0.55, blueColor = 0.4, redRatio = 0, blueRatio = 0, armPosition = 0, centerFinger = 0.6, speed = 0.15, adjustLeftSpeed = 0.06, adjustRightSpeed = 0.06, angle = 0;
    int encToDispense = 250, encToDismount = 1130, encToArriveAtCryptobox = 175, encToMoveToNextColumn = 0;
    long waiting = 0, waitTime = 1500;

    CryptoboxColumn column;

    GyroAngles gyroAngles;

    // IMPORTANT: THIS OP-MODE WAITS ONE SECOND BEFORE STARTING. THIS MEANS THAT WE HAVE TWENTY-NINE SECONDS TO ACCOMPLISH TASKS, NOT THIRTY.
    public void start() {
        relicTrackables.activate();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            telemetry.addData("Exception", e);
        }
    }

    @Override
    public void init() {
        super.init();
        state = State.STATE_SCAN_KEY;
        jewelArm.setPosition(0.71);
        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VuforiaLicenseKey.LICENSE_KEY; // VuforiaLicenseKey is ignored by git
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

    }
    //0.71 = up position

    @Override
    public void loop() {
        colors = color.getNormalizedColors();
        double redRatio = colors.red / (colors.red + colors.blue + colors.green);
        double blueRatio = colors.blue / (colors.red + colors.blue + colors.green);
        switch (state) {
            case STATE_SCAN_KEY:
                vuMark = RelicRecoveryVuMark.from(relicTemplate);
                switch (vuMark) {
                    case LEFT:
                        //state = ROBOT_ACTIVITY_STATE.moving;
                        //encoderAmount = 8000;
                        column = CryptoboxColumn.RIGHT;
                        break;
                    case CENTER:

                        column = CryptoboxColumn.MID;
                        break;
                    case RIGHT:

                        column = CryptoboxColumn.LEFT;
                        break;
                    default:
                        break;
                }
                if (vuMark != RelicRecoveryVuMark.UNKNOWN)
                    state = State.STATE_CENTER_FINGER;
                break;
            case STATE_CENTER_FINGER:
                jewelFlipper.setPosition(0.6);
                if (waiting == 0)
                    waiting = System.currentTimeMillis();
                if (System.currentTimeMillis() - waiting >= waitTime) {
                    waiting = 0;
                    state = State.STATE_LOWER_JEWEL_ARM;
                }
                break;
            case STATE_LOWER_JEWEL_ARM:
                jewelArm.setPosition(0.25);
                if (waiting == 0)
                    waiting = System.currentTimeMillis();
                if (System.currentTimeMillis() - waiting >= waitTime) {
                    waiting = 0;
                    state = State.STATE_SCAN_JEWEL;
                }
                break;
            case STATE_SCAN_JEWEL:
                if (redRatio >= redColor && redRatio > blueRatio)
                    state = State.STATE_HIT_LEFT_JEWEL;
                else if (blueRatio >= blueColor && redRatio < blueRatio)
                    state = State.STATE_HIT_RIGHT_JEWEL;

                break;
            case STATE_HIT_LEFT_JEWEL:
                jewelFlipper.setPosition(0.05);
                if (waiting == 0)
                    waiting = System.currentTimeMillis();
                if (System.currentTimeMillis() - waiting >= waitTime) {
                    waiting = 0;
                    state = State.STATE_RESET_JEWEL_HITTER;
                }
                break;
            case STATE_HIT_RIGHT_JEWEL:
                jewelFlipper.setPosition(0.95);
                if (waiting == 0)
                    waiting = System.currentTimeMillis();
                if (System.currentTimeMillis() - waiting >= waitTime) {
                    waiting = 0;
                    state = State.STATE_RESET_JEWEL_HITTER;
                }
                break;
            case STATE_RESET_JEWEL_HITTER:
                jewelArm.setPosition(0.71);
                if (waiting == 0)
                    waiting = System.currentTimeMillis();
                if (System.currentTimeMillis() - waiting >= waitTime) {
                    waiting = 0;
                    leftFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rightFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rightFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    leftFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    state = State.STATE_DRIVE_TO_CRYPTOBOX;
                }
                break;
            case STATE_DRIVE_TO_CRYPTOBOX:
                setLeftPow(speed);
                setRightPow(speed);
                state = State.STATE_CHECK_TURN;
                break;
            case STATE_CHECK_TURN:
                if (checkEncoder(encToDismount))
                    state = State.STATE_READ_GYRO;
                break;
            case STATE_READ_GYRO:
                gyroAngles = new GyroAngles(angles);
                state = State.STATE_TURN;
                break;
            case STATE_TURN:
                setLeftPow(-adjustLeftSpeed);
                setRightPow(adjustRightSpeed);
                if (gyroAngles.getZ() - (new GyroAngles(angles).getZ()) <= -30) {
                    state = state.STATE_CRYPTOBOX_RIGHT_SLOT;
                    leftFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rightFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rightFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    leftFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }
                break;

            case STATE_CRYPTOBOX_RIGHT_SLOT:
                setLeftPow(speed);
                setRightPow(speed);
                if (checkEncoder(encToArriveAtCryptobox)) {
                    if (column == CryptoboxColumn.RIGHT)
                        state = State.STATE_DISPENSE_GLYPH;
                    else
                        state = State.STATE_CRYPTOBOX_CENTER_SLOT;
                }
                break;
            case STATE_CRYPTOBOX_CENTER_SLOT:
                leftFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                if (checkEncoder(encToMoveToNextColumn)) {
                    if (column == CryptoboxColumn.MID)
                        state = State.STATE_DISPENSE_GLYPH;
                    else
                        state = State.STATE_CRYPTOBOX_RIGHT_SLOT;
                }
                break;
            case STATE_CRYPTOBOX_LEFT_SLOT:
                leftFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                if (checkEncoder(encToMoveToNextColumn))
                    state = State.STATE_DISPENSE_GLYPH;
                break;
            case STATE_DISPENSE_GLYPH:
                setLeftPow(adjustLeftSpeed);
                setRightPow(adjustRightSpeed);
                leftFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                setLeftPow(-speed);
                setRightPow(speed);
                gyroAngles = new GyroAngles(angles);
                if (gyroAngles.getZ() - (new GyroAngles(angles).getZ()) <= 30) {
                    setLeftPow(-speed);
                    setRightPow(-speed);


                    if (checkEncoder(encToDispense /* placeholder value*/) || checkLeftEncoder(encToDispense /* placeholder value*/)) {
                        //glyphOutput.setPosition(0.33);
                        if (waiting == 0)
                            waiting = System.currentTimeMillis();
                        if (System.currentTimeMillis() - waiting >= waitTime) {
                            waiting = 0;
                        }
                        //glyphOutput.setPosition(0);

                        state = State.STATE_END;
                    }
                    //1130 for getting off of the platform
                    //after first turn, -175 for right column
                }
                break;
            // TODO: Implement collection of additional glyphs?
            case STATE_END:
                telemetry.addData("Finished", "Very Yes");
                break;
        }

        telemetry.addData("State", state.name());

        telemetry.addData("Jewel Arm Pos.", jewelArm.getPosition());
        telemetry.addData("Jewel Flip. Pos.", jewelFlipper.getPosition());
        telemetry.addData("Color Sensor RGB", "[red " + redRatio + ", blue " + blueRatio + "]");
        telemetry.addData("leftFore position ", leftFore.getCurrentPosition());
        telemetry.addData("rightFore position ", rightFore.getCurrentPosition());
        telemetry.addData("Angles: ", imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES));
        telemetry.addData("Total LF Encoder", leftFore.getCurrentPosition());
        telemetry.addData("Total LR Encoder", leftRear.getCurrentPosition());
        telemetry.addData("Total RF Encoder", rightFore.getCurrentPosition());
        telemetry.addData("Total RR Encoder", rightRear.getCurrentPosition());

    }

    enum State {
        STATE_SCAN_KEY, // Ends when we get a successful scan. Always -> STATE_LOWER_JEWEL_ARM
        STATE_CENTER_FINGER,
        STATE_LOWER_JEWEL_ARM, // Ends when jewel arm is at certain position. Always -> STATE_SCAN_JEWEL
        STATE_SCAN_JEWEL, // Ends when right jewel color is read. Right jewel == blue -> STATE_HIT_LEFT_JEWEL. Right jewel == red -> STATE_HIT_RIGHT_JEWEL
        STATE_HIT_LEFT_JEWEL, // Ends when servo is at position. Always -> STATE_RESET_JEWEL_HITTER
        STATE_HIT_RIGHT_JEWEL, // Ends when servo is at position. Always -> STATE_RESET_JEWEL_HITTER
        STATE_RESET_JEWEL_HITTER, // Ends when servo is at position. Always -> STATE_DRIVE_TO_CRYPTOBOX
        STATE_DRIVE_TO_CRYPTOBOX, // Ends when short-range distance sensor reads cryptobox divider. Always -> STATE_CRYPTOBOX_RIGHT_SLOT
        STATE_CHECK_TURN,
        STATE_READ_GYRO,
        STATE_TURN,
        STATE_CRYPTOBOX_LEFT_SLOT, // Ends when short-range distance sensor reads cryptobox divider. Key == left -> STATE_DISPENSE_GLYPH. Key == center or right -> STATE_CRYPTOBOX_CENTER_SLOT
        STATE_CRYPTOBOX_CENTER_SLOT, // Ends when short-range distance sensor reads cryptobox divider. Key == center -> STATE_DISPENSE_GLYPH. Key == right -> STATE_CRYPTOBOX_LEFT_SLOT
        STATE_CRYPTOBOX_RIGHT_SLOT, // Ends when short-range distance sensor reads cryptobox divider. Always -> STATE_DISPENSE_GLYPH.
        STATE_DISPENSE_GLYPH, // Ends when glyph is dispensed. Always (unless we are collecting more glyphs) -> STATE_END
        // TODO: Collect more glyph and dispense them to the cryptobox?
        STATE_END // Ends when the universe dies.
    }


}
