package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Hunter Seachrist on 12/5/2017.
 */
@TeleOp(name = "SensorBot: Ping Test", group = "bepis")
@Disabled
public class Sensor2BotPingTest extends Sensor2BotTemplate {
    private boolean prevA;

    public void loop() {
        //if (gamepad1.a && !prevA)
            //ping.startReading();

        //if (ping.isReadingFresh())
            //telemetry.addData("Centimeters from Thingy", ping.getLastReadingCentimeters());

        prevA = gamepad1.a;
    }

}
