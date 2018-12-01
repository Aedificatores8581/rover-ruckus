package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.Tests;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Components.Sensors.TouchSensor;

@Autonomous(name = "Limit Switch")
public class LimitSwitchTest extends OpMode {
    TouchSensor top, bottom;

    @Override
    public void init() {
        top.init(hardwareMap, "top");
        bottom.init(hardwareMap, "bot");
    }

    @Override
    public void loop() {
        telemetry.addData("Top", top.isPressed());
        telemetry.addData("Bot", bottom.isPressed());
    }
}
