package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Conjured into existence by The Saminator on 02-01-2018.
 */
@Autonomous(name = "MB1200 Test", group = "these are not the opmodes you are looking for move along now")
public class MB1200Test extends OpMode {
    private MB1200 sensor;
    private boolean prevA = false;

    @Override
    public void init() {
        sensor = new MB1200(hardwareMap.digitalChannel.get("ds"));
        prevA = false;
    }

    @Override
    public void start() {
        super.start();
        sensor.start();
    }

    @Override
    public void loop() {
        if (gamepad1.a && !prevA)
            telemetry.addData("Sensor reading", sensor.getReading());

        prevA = gamepad1.a;
    }

    @Override
    public void stop() {
        super.stop();
        sensor.stop();
    }
}
