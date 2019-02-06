package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class OpticalEncoderTest extends OpMode {
    DcMotor motor;

    @Override
    public void init() {
        motor = hardwareMap.dcMotor.get("int");
    }

    @Override
    public void loop() {
        motor.setPower(gamepad1.left_stick_y);
        telemetry.addLine("Enc: " + motor.getCurrentPosition());
    }
}
