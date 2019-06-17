package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.Drivetrain;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

@Disabled
@TeleOp(name = "MotorTestSetPow: Config Test", group = "Test")
public class MotorConfigTest extends OpMode {

    DcMotor rightfront, rightback, leftfront, leftback;

    double rightPow, leftPow;

    @Override
    public void init() {
        rightPow = 0;
        leftPow = 0;

        rightfront = hardwareMap.dcMotor.get("rf");
        rightback = hardwareMap.dcMotor.get("ra");
        leftfront = hardwareMap.dcMotor.get("lf");
        leftback = hardwareMap.dcMotor.get("la");
    }

    @Override
    public void loop() {/*
        rightPow = gamepad1.right_stick_y;
        leftPow = gamepad1.left_stick_y;

        rightPow = UniversalFunctions.clamp(-1, rightPow, 1);
        rightfront.setPower(rightPow * .2);
        rightback.setPower(rightPow * .2);

        leftPow = UniversalFunctions.clamp(-1, rightPow, 1);
        leftfront.setPower(rightPow * .2);
        leftback.setPower(rightPow * .2);*/

        if (gamepad1.a) {
            rightfront.setPower(.6);
        } else {
            rightfront.setPower(0.0);
        }
        if (gamepad1.b) {
            rightback.setPower(.6);
        } else {
            rightback.setPower(0.0);
        }
        if (gamepad1.x) {
            leftfront.setPower(.6);
        } else {
            leftfront.setPower(0.0);
        }
        if (gamepad1.y) {
            leftback.setPower(.6);
        } else {
            leftback.setPower(0.0);
        }

        telemetry.addData("(A) right front", rightfront.getPower());
        telemetry.addData("(B) right back", rightback.getPower());
        telemetry.addData("(X) left  front", leftfront.getPower());
        telemetry.addData("(Y) left  back", leftback.getPower());
    }
}
