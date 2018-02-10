package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;

/**
 * Conjured into existence by The Saminator on 02-09-2018.
 */
@Autonomous
@Disabled
public class MagnetSensor extends OpMode {

    private DigitalChannel magnet;

    @Override
    public void init() {
        magnet = hardwareMap.digitalChannel.get("ms");
        magnet.setMode(DigitalChannel.Mode.INPUT);
    }

    @Override
    public void loop() {
        telemetry.addData("Magnet present", !magnet.getState());
    }
}
