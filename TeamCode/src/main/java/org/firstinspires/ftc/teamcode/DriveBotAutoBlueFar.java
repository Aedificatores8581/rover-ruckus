package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Autonomous(name = "Autonomous Blue Near", group = "competition bepis")
public class DriveBotAutoBlueFar extends DriveBotTemplate {

    State state;

    private int cameraMonitorViewId;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;
    private VuforiaLocalizer vuforia;
    private RelicRecoveryVuMark vuMark;
    double redColor = 0, blueColor = 0, armPosition = 0, centerFinger = 0, speed = 0, adjustLeftSpeed, adjustRightSpeed;
    int forLeftEncoder= 0, forRightEncoder = 0, backLeftEncoder, backRightEncoder, leftForwEnc, rightForwEnc;;
    String column;
    NormalizedColorSensor colorSensor;
    NormalizedRGBA colors;

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

        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VuforiaLicenseKey.LICENSE_KEY; // VuforiaLicenseKey is ignored by git
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "cs");
    }

    @Override
    public void loop() {
        colors = colorSensor.getNormalizedColors();
        double redRatio = colors.red / (colors.red + colors.blue + colors.green);
        double blueRatio = colors.blue / (colors.red + colors.blue + colors.green);
        switch (state) {
            case STATE_SCAN_KEY:
                vuMark = RelicRecoveryVuMark.from(relicTemplate);
                switch (vuMark) {
                    case LEFT:
                        //state = ROBOT_ACTIVITY_STATE.moving;
                        //encoderAmount = 8000;
                        column = "Left";
                        break;
                    case CENTER:

                        column = "Center";
                        break;
                    case RIGHT:

                        column = "Right";
                        break;
                    default:
                        break;
                }
                if (vuMark != RelicRecoveryVuMark.UNKNOWN)
                    state = State.STATE_LOWER_JEWEL_ARM;
                break;
            case STATE_LOWER_JEWEL_ARM:
                jewelArm.setPosition(armPosition);
                break;
            case STATE_SCAN_JEWEL:
                NormalizedRGBA colors = colorSensor.getNormalizedColors();
                if (redRatio >= redColor && redRatio > blueRatio)
                    state = State.STATE_HIT_RIGHT_JEWEL;
                    //this could be state = State.STATE_HIT_LEFT_JEWEL; depending on what side the color sensor is facing
                else if (blueRatio >= blueColor && redRatio < blueRatio)
                    state = State.STATE_HIT_LEFT_JEWEL;
                //this could be state = State.STATE_HIT_RIGHT_JEWEL; depending on what side the color sensor is facing
                break;
            case STATE_HIT_LEFT_JEWEL:
                jewelFlipper.setPosition(1.0);
                //this could be jewelFlipper.setPosition(0); depending on the side of the arm the servo is mounted
                state = State.STATE_RESET_JEWEL_HITTER;
                break;
            case STATE_HIT_RIGHT_JEWEL:
                jewelFlipper.setPosition(0);
                //this could be jewelFlipper.setPosition(0); depending on the side of the arm the servo is mounted
                state = State.STATE_RESET_JEWEL_HITTER;
                break;
            case STATE_RESET_JEWEL_HITTER:
                jewelFlipper.setPosition(centerFinger);
                jewelArm.setPosition(0);
                state = State.STATE_DRIVE_TO_CRYPTOBOX;
                break;
            case STATE_DRIVE_TO_CRYPTOBOX:
                setLeftPow(speed);
                setRightPow(speed);
                if(checkLeftEncoder(forLeftEncoder) == true || checkRightEncoder(forRightEncoder) == true) {
                    setLeftPow(speed);
                    setRightPow(-speed);
                }
                //if the gyro sensor senses that a 90 degree turn has been made{
                    setLeftPow(speed);
                    setRightPow(speed);
                //use the distance sensor to read one shelf
                    state = state.STATE_CRYPTOBOX_LEFT_SLOT;


                break;
            case STATE_CRYPTOBOX_LEFT_SLOT:
                if(column == "Left")
                    state = State.STATE_DISPENSE_GLYPH;
                else
                    state = State.STATE_CRYPTOBOX_CENTER_SLOT;
                break;
            case STATE_CRYPTOBOX_CENTER_SLOT:
                if(column == "Center")
                    state = State.STATE_DISPENSE_GLYPH;
                else
                    state = State.STATE_CRYPTOBOX_RIGHT_SLOT;
                break;
            case STATE_CRYPTOBOX_RIGHT_SLOT:
                if(column == "Right")
                    state = State.STATE_DISPENSE_GLYPH;
                break;
            case STATE_DISPENSE_GLYPH:
                setLeftPow(adjustLeftSpeed);
                setRightPow(adjustRightSpeed);
                if (checkLeftEncoder(backLeftEncoder) == true || checkRightEncoder((backRightEncoder)) == true ) {
                    setLeftPow(speed);
                    setRightPow(speed);
                    //(if the gyroscope senses that a 90 degree turn has been made){
                    setLeftPow(speed);
                    setRightPow(speed);
                    if(checkLeftEncoder(leftForwEnc) || checkRightEncoder(rightForwEnc)){
                        //dispense the glyph
                        state = State.STATE_END;
                    }
                    //}
                }
                break;
            // TODO: Implement collection of additional glyphs?
            case STATE_END:
                telemetry.addData("Finished", "Very Yes");
                break;
        }

        telemetry.addData("State", state.name());

        super.loop();
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
        STATE_DISPENSE_GLYPH, // Ends when glyph is dispensed. Always (unless we are collecting more glyphs) -> STATE_END
        // TODO: Collect more glyph and dispense them to the cryptobox?
        STATE_END // Ends when the universe dies.
    }

}
