package org.firstinspires.ftc.teamcode.Components.Sensors.Tests;


import android.util.Log;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Autonomous(name = "Rev 2m Distance Test", group = "test")
public class Rev2MDistanceTest extends OpMode {
    Rev2mDistanceSensor sensor;

    @Override
    public void init() {
        sensor = hardwareMap.get(Rev2mDistanceSensor.class, "dist");
    }

    @Override
    public void loop() {
        telemetry.addLine(String.valueOf(sensor.getDistance(DistanceUnit.CM)));
    }
}
