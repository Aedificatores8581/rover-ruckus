package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Hunter Seachrist on 12/12/2017.
 */


@TeleOp(name = "Sensor test", group = "bepis")
public class Sensor2BotIRSensorTest extends Sensor2BotTemplate{


    public void start(){

    }

    public void loop(){
        telemetry.addData("Strength",infraRedSensor.getStrength());
    }
}
