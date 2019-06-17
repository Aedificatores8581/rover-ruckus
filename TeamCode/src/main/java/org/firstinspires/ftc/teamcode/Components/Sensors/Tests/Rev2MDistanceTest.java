package org.firstinspires.ftc.teamcode.Components.Sensors.Tests;


import android.util.Log;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

@Disabled
@Autonomous(name = "Rev 2m Distance Test", group = "test")
public class Rev2MDistanceTest extends OpMode {
    Rev2mDistanceSensor sensor;
    int i = 0;
    double startTime = 0;
    @Override
    public void init() {
        sensor = hardwareMap.get(Rev2mDistanceSensor.class, "dist");
    }
    @Override
    public void start(){
        super.start();
        startTime = UniversalFunctions.getTimeInSeconds();
    }
    @Override
    public void loop() {
        i++;
        telemetry.addLine(String.valueOf(sensor.getDistance(DistanceUnit.CM)));
        telemetry.addLine(String.valueOf(sensor.getDistance(DistanceUnit.INCH)));
        telemetry.addData("updateSpeed", i/(UniversalFunctions.getTimeInSeconds() - startTime) + "iterations per second");
    }
}
