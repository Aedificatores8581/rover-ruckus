package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.Drivetrain;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

/*
DO NOT CHANGE THIS FILE EVER
 */
@TeleOp(name = "sanity test")
public class MotorTest2 extends OpMode {
    DcMotor rightfront, rightback, leftfront, leftback;

    public void init(){
        rightfront = hardwareMap.dcMotor.get("rf");
        rightback = hardwareMap.dcMotor.get("ra");
        leftfront = hardwareMap.dcMotor.get("lf");
        leftback = hardwareMap.dcMotor.get("la");

        leftback.setDirection(DcMotorSimple.Direction.REVERSE);
        leftfront.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void loop() {
        double velocity = gamepad1.right_trigger - gamepad1.left_trigger;
        rightfront.setPower(velocity - gamepad1.right_stick_x);
        rightback.setPower(velocity - gamepad1.right_stick_x);
        leftback.setPower(velocity + gamepad1.right_stick_x);
        leftfront.setPower(velocity + gamepad1.right_stick_x);
        telemetry.addData("rightfront", rightfront.getPower());
        telemetry.addData("rightback", rightback.getPower());
        telemetry.addData("leftfront", leftfront.getPower());
        telemetry.addData("leftback", leftback.getPower());

    }
}
