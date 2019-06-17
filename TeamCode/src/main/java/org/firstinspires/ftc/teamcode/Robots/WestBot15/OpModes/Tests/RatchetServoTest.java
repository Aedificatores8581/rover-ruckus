package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;

@Disabled
@TeleOp(name = "Ratchet Servo Test", group = "tests")
public class RatchetServoTest extends OpMode {

    Servo sideRatchetServo, topRatchetServo;
    // Side
    // Closed: 0.1
    // Open: 0.3

    // Top
    // Closed .92
    // Open: .7
    @Override
    public void init() {
        topRatchetServo = hardwareMap.servo.get("trat");
        sideRatchetServo = hardwareMap.servo.get("srat");
        sideRatchetServo.setPosition(0.4);
        topRatchetServo.setPosition(0.7);
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {
        sideRatchetServo.setPosition(sideRatchetServo.getPosition() + (gamepad1.left_stick_y * 0.005));

        topRatchetServo.setPosition(topRatchetServo.getPosition() + (gamepad1.right_stick_y * 0.005));

        telemetry.addData("Side (left stick)", sideRatchetServo.getPosition());
        telemetry.addData("Top (right stick)", topRatchetServo.getPosition());
    }
}
