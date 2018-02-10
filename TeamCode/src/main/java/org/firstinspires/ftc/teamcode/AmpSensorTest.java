package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Hunter Seachrist on 2/9/2018.
 */

@TeleOp(name = "SensorBot: Amp Sensor", group = "Sensor Test")
public class AmpSensorTest extends OpMode {
    AnalogInput ampSensor;
    Servo testServo;
    double servoPosition;

    public void init(){
        ampSensor = hardwareMap.get(AnalogInput.class, "amp");
        testServo = hardwareMap.servo.get("servo");
    }


    public void loop(){
        testServo.setPosition(servoPosition);

        telemetry.addLine("Amp Sensor Voltage: " + ampSensor.getVoltage());
        if (gamepad1.left_bumper) {
            servoPosition += 0.02;
        }
        else if (gamepad1.right_bumper) {
            servoPosition -= 0.02;
        }

    }
}
