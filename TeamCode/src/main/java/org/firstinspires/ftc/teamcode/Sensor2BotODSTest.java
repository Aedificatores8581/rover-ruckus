package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Hunter Seachrist on 12/5/2017.
 */
@TeleOp(name = "SensorBot: Opical Distance Sensor Test", group = "bepis")
public class Sensor2BotODSTest extends Sensor2BotTemplate{

    private enum State {SETTING_DISTANCE, DRIVING, STOPPED}
    State state;
    double lightAmount;


    public void init(){
        state = State.SETTING_DISTANCE;
    }

    public void loop(){
        lightAmount = ods.getRawLightDetected();
        telemetry.update();
        telemetry.addData("Light", lightAmount);

    }
}
