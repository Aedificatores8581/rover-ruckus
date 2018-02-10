package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DigitalChannel;
/**
 * Created by Hunter Seachrist on 2/9/2018.
 */

@TeleOp(name = "SensorBot: Amp Sensor", group = "Sensor Test")
public class AmpSensorTest extends OpMode {
    AnalogInput ampSensor;
    Servo testServo;
    double servoPosition;

    protected DigitalChannel magFront, magBack;


    public void init(){
        ampSensor = hardwareMap.get(AnalogInput.class, "amp");
        testServo = hardwareMap.servo.get("servo");
        magFront = hardwareMap.digitalChannel.get("mf");
        magBack = hardwareMap.digitalChannel.get("mb");
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
        telemetry.addData("back.getState:  ", magBack.getState());
        telemetry.addData("back.getMode:   ", magBack.getMode());
        telemetry.addData("front.getState:  ", magFront.getState());
        telemetry.addData("front.getMode:   ", magFront.getMode());


    }
}
