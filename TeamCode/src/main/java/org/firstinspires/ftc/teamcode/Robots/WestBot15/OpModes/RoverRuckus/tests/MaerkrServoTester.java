package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
/**
 * OpMode to test Different Servo sides
 */
// .3 closed left
// 1.0 closed right
@Autonomous(name = "Marker Test")
public class MaerkrServoTester extends OpMode {

    Servo maerkrLeft, maerkrRight;

    double leftPos, rightPos;

    @Override
    public void init() {
        maerkrLeft  = hardwareMap.servo.get("lmrkr");
        maerkrLeft.setPosition(0.0);

        maerkrRight = hardwareMap.servo.get("rmrkr");
        maerkrRight.setPosition(0.0);

    }
// Maerkr Left Close: .1
// Maerkr Right Close: .1
    @Override
    public void loop() {
        leftPos = 0.9;
        rightPos = 0.9;

        maerkrRight.setPosition(rightPos);
        maerkrLeft.setPosition(leftPos);

        /*
        maerkrRight.setPosition(maerkrLeft.getPosition() + 0.001*gamepad1.left_stick_y);
        if (gamepad1.a && getRuntime() > 0.25) {
            resetStartTime();
            maerkrLeft.setPosition(maerkrLeft.getPosition() > .5 ? 0.0 : 1.0);
        }
        if (gamepad1.b && getRuntime() > 0.25) {
            resetStartTime();
            maerkrRight.setPosition(maerkrRight.getPosition() > .5 ? 0.0 : 1.0);
        }*/
    }
}