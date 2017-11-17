package org.firstinspires.ftc.teamcode;

/**
 * Created by The Saminator on 11-12-2017.
 */
public class DriveBotAutoBlueFar extends DriveBotTemplate {

    State state;

    @Override
    public void init() {
        super.init();
        state = State.STATE_SCAN_KEY;
    }

    @Override
    public void loop() {
        switch (state) {
            case STATE_SCAN_KEY:
                break;
            case STATE_LOWER_JEWEL_ARM:
                break;
            case STATE_SCAN_JEWEL:
                break;
            case STATE_HIT_LEFT_JEWEL:
                break;
            case STATE_HIT_RIGHT_JEWEL:
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
        STATE_SCAN_JEWEL, // Ends when jewel color is read. Right jewel == blue -> STATE_HIT_LEFT_JEWEL. Right jewel == red -> STATE_HIT_RIGHT_JEWEL
        STATE_HIT_LEFT_JEWEL, // Ends when touch sensor is hit. Always -> STATE_DRIVE_TO_CRYPTOBOX
        STATE_HIT_RIGHT_JEWEL, // Ends when touch sensor is hit. Always -> STATE_DRIVE_TO_CRYPTOBOX
        STATE_DRIVE_TO_CRYPTOBOX, // Ends when short-range distance sensor reads cryptobox divider. Always -> STATE_CRYPTOBOX_LEFT_SLOT
        STATE_CRYPTOBOX_LEFT_SLOT, // Ends when short-range distance sensor reads cryptobox divider. Key == left -> STATE_DISPENSE_GLYPH. Key == center or right -> STATE_CRYPTOBOX_CENTER_SLOT
        STATE_CRYPTOBOX_CENTER_SLOT, // Ends when short-range distance sensor reads cryptobox divider. Key == center -> STATE_DISPENSE_GLYPH. Key == right -> STATE_CRYPTOBOX_RIGHT_SLOT
        STATE_CRYPTOBOX_RIGHT_SLOT, // Ends when short-range distance sensor reads cryptobox divider. Always -> STATE_DISPENSE_GLYPH.
        STATE_DISPENSE_GLYPH, // Ends when glyph is dispensed. Always (unless we are collecting more glyphs) -> STATE_END
        // TODO: Collect more glyph and dispense them to the cryptobox?
        STATE_END // Ends when the universe dies.
    }

}
