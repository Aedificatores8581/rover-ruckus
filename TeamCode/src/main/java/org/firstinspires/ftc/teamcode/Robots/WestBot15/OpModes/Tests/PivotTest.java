package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Universal.UniversalConfig;

@Disabled
@TeleOp(name = "Pivot Tests")
public class PivotTest extends OpMode {
    Servo pivot[];

    double servoAmountToChangeServo;
    double initialPosition = 1.0;


    @Override
    public void init() {
        pivot = new Servo[2];
        pivot[0] = hardwareMap.servo.get(UniversalConfig.MINERAL_LIFT_PIVOT[0]);
        pivot[1] = hardwareMap.servo.get(UniversalConfig.MINERAL_LIFT_PIVOT[1]);

        pivot[0].setPosition(initialPosition);
        pivot[1].setPosition(initialPosition);

        servoAmountToChangeServo = 0.01;
    }
    // 1.0 init for auto
    // 0.855, position for teleop
    // 0.0027, init for dispensing
    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            pivot[0].setPosition(pivot[0].getPosition() + servoAmountToChangeServo);
            pivot[1].setPosition(pivot[1].getPosition() + servoAmountToChangeServo);
        } else if (gamepad1.dpad_down) {
            pivot[0].setPosition(pivot[0].getPosition() - servoAmountToChangeServo);
            pivot[1].setPosition(pivot[1].getPosition() - servoAmountToChangeServo);
        }

        telemetry.addData("Pivot 1", pivot[0].getPosition());
        telemetry.addData("Pivot 2", pivot[1].getPosition());
    }
}
