package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogSensor;

/**
 * Created by Hunter Seachrist on 2/9/2018.
 */

public class AmpSensorTest extends OpMode {
    AnalogSensor ampSensor;

    public void init(){
        ampSensor = hardwareMap.get(AnalogSensor.class, "amp");

    }


    public void loop(){
        telemetry.addData("Amp Sensor Voltage", ampSensor.readRawVoltage());
    }
}
