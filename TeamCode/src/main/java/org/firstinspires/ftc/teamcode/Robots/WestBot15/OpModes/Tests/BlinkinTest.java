package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Components.Blinkin.BlinkinLEDControl;
import org.firstinspires.ftc.teamcode.Components.Blinkin.BlinkinPresets;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;

import static org.firstinspires.ftc.teamcode.Components.Blinkin.BlinkinLEDControl.MAX_PWM_ADDRESS;
import static org.firstinspires.ftc.teamcode.Components.Blinkin.BlinkinLEDControl.MIN_PWM_ADDRESS;

/**
 *
 * Scrivened by Theodore Lovinski 08-12-2018.
 *
 * Currently untested.
 *
 */

@TeleOp(name = "Blinkin Test", group = "tests")
public class BlinkinTest extends WestBot15 {
    BlinkinLEDControl blinkinLEDControl;
    BlinkinPresets blinkinPresets;
    private static double selected_pattern = 0.4;

    @Override
    public void init() {
        super.init();
        blinkinLEDControl.setSolidColor(blinkinPresets.VIOLET);
        blinkinLEDControl.switchDisplayState();
    }

    public void start() { super.start(); }

    @Override
    public void loop() {
        if (selected_pattern < MIN_PWM_ADDRESS && selected_pattern < MAX_PWM_ADDRESS) {
            if (gamepad1.left_bumper) {
                selected_pattern -= 0.02;
            } else if (gamepad1.right_bumper) {
                selected_pattern += 0.02;
            }
        }

        if (gamepad1.dpad_up) {
            blinkinLEDControl.setSolidColor(blinkinPresets.VIOLET);
        } else if (gamepad1.dpad_left) {
            blinkinLEDControl.setSolidColor(blinkinPresets.RED);
        } else if (gamepad1.dpad_down) {
            // Here we do things Karellian style.
            blinkinLEDControl.setSolidColor(blinkinPresets.BLUE);
        } else if (gamepad1.dpad_right) {
            blinkinLEDControl.setSolidColor(blinkinPresets.GREEN);
        }

        blinkinLEDControl.setPattern(selected_pattern);
    }
}
