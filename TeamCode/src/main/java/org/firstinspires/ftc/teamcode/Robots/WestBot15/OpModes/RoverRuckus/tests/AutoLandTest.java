package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus.tests;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Lift;
import org.firstinspires.ftc.teamcode.Components.Sensors.TouchSensor;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;

public class AutoLandTest extends WestBot15 {
    TouchSensor topLiftSensor, bottomLiftSensor;

    @Override
    public void init() {
        topLiftSensor = new TouchSensor();
        topLiftSensor.init(hardwareMap, "tts");
        bottomLiftSensor = new TouchSensor();
        bottomLiftSensor.init(hardwareMap, "bts");

        lift = new Lift();
        lift.init(hardwareMap);
        lift.ratchetState = Lift.RatchetState.DOWN;
        lift.switchRatchetState();
    }

    @Override
    public void loop() {
    }
}
