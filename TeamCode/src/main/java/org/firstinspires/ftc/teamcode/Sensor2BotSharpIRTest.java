package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Hunter Seachrist on 12/5/2017.
 */
@TeleOp(name = "SensorBot: Sharp IR Test", group = "bepis")
public class Sensor2BotSharpIRTest extends Sensor2BotTemplate {

    public void loop() {
        telemetry.addData("Distance", ir.readDistanceCM());
    }

}
