package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by The Saminator on 11-12-2017.
 */
public class DriveBotAutoBlueNear extends DriveBotTemplate {

    State state;

    private int cameraMonitorViewId;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;
    private VuforiaLocalizer vuforia;
    private RelicRecoveryVuMark vuMark;

    NormalizedColorSensor colorSensor;

    // IMPORTANT: THIS OP-MODE WAITS ONE SECOND BEFORE STARTING. THIS MEANS THAT WE HAVE TWENTY-NINE SECONDS TO ACCOMPLISH TASKS, NOT THIRTY.
    public void start() {
        relicTrackables.activate();

        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            telemetry.addData("EXCEPTION", e);
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
        switch (state) {
            case STATE_SCAN_KEY:
                vuMark = RelicRecoveryVuMark.from(relicTemplate);
                if (vuMark != RelicRecoveryVuMark.UNKNOWN)
                    state = State.STATE_LOWER_JEWEL_ARM;
                break;
            case STATE_LOWER_JEWEL_ARM:
                jewelArm.setPosition(0.39);
                break;
            case STATE_SCAN_JEWEL:
                NormalizedRGBA colors = colorSensor.getNormalizedColors();
                if (colors.red > colors.blue)
                    state = State.STATE_HIT_RIGHT_JEWEL;
                else
                    state = State.STATE_HIT_LEFT_JEWEL;
                break;
            case STATE_HIT_LEFT_JEWEL:
                jewelFlipper.setPosition(1.0);
                state = State.STATE_RESET_JEWEL_HITTER;
                break;
            case STATE_HIT_RIGHT_JEWEL:
                jewelFlipper.setPosition(-1.0);
                state = State.STATE_RESET_JEWEL_HITTER;
                break;
            case STATE_RESET_JEWEL_HITTER:
                jewelFlipper.setPosition(0);
                jewelArm.setPosition(0);
                state = State.STATE_DRIVE_TO_CRYPTOBOX;
                break;
            case STATE_DRIVE_TO_CRYPTOBOX:
                break;
            case STATE_CRYPTOBOX_LEFT_SLOT:
                break;
            case STATE_CRYPTOBOX_CENTER_SLOT:
                break;
            case STATE_CRYPTOBOX_RIGHT_SLOT:
                break;
            case STATE_DISPENSE_GLYPH:
                break;
            // TODO: Implement collection of additional glyphs?
            case STATE_END:
                break;
        }
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
