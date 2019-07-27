package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
/**
 * OpMode to test Different Servo sides
 */
// .3 closed left
// 1.0 closed right

@Disabled
@Autonomous(name = "Marker Test")
public class MaerkrServoTester extends OpMode {

    Servo maerkrLeft, maerkrRight;

    double leftPos, rightPos;

    @Override
    public void init() {
        maerkrLeft  = hardwareMap.servo.get("mrkr");
        maerkrLeft.setPosition(0.4);

    }
// Maerkr Left Close: .1
// Maerkr Right Close: .1
    @Override
    public void loop() {
        leftPos = 0.9;
        rightPos = 0.9;

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
