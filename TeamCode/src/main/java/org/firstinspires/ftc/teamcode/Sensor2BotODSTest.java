package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Hunter Seachrist on 12/5/2017.
 */
@TeleOp(name = "SensorBot: Opical Distance Sensor Test", group = "bepis")
public class Sensor2BotODSTest extends Sensor2BotTemplate {

    private enum State {SETTING_DISTANCE, DRIVING, STOPPED}

    State state;

    public void init() {
        super.init();
        state = State.SETTING_DISTANCE;
    }

    public void loop() {

        if (ods == null)
            telemetry.addData("ERROR ERROR ERROR", "OPTICAL DISTANCE SENSOR IS NULL THIS IS VERY BAD REQUESTING TO ABORT REPEAT REQUESTING TO ABORT");
        else
            telemetry.addData("Light", ods.getLightDetected());

    }
}
