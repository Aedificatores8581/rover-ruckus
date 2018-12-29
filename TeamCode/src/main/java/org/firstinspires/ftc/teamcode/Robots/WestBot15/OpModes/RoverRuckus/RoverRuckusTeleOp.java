package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;

@TeleOp (name = "teleop2")
public class RoverRuckusTeleOp extends WestBot15 {
    private double minLength;

    ExtensionState extensionState = ExtensionState.NON_RESETTING;

    public void init() {
        isAutonomous = false;
        usingIMU = false;

        super.init();

        if (aextendo.isRetracted()) {
            minLength = aextendo.getExtensionLength();
        }

        activateGamepad1();
        activateGamepad2();
    }

    public void start(){
        super.start();
    }

    public void loop(){
        updateGamepad1();
        updateGamepad2();

        drivetrain.leftPow = gamepad1.right_trigger - gamepad1.left_trigger - leftStick1.x;
        drivetrain.rightPow = gamepad1.right_trigger - gamepad1.left_trigger + leftStick1.x;

        if (aextendo.isRetracted()) {
            drivetrain.leftPow *= drivetrain.maxSpeed;
            drivetrain.rightPow *= drivetrain.maxSpeed;
        } else {
            drivetrain.leftPow *= aextendo.getExtensionLength() / 10;
        }

        drivetrain.setLeftPow();
        drivetrain.setRightPow();

        aextendo.aextendTM(rightStick1.y);

        telemetry.addData("extensionLength", aextendo.getExtensionLength());

    }

    enum ExtensionState{
        RESETTING,
        NON_RESETTING
    }
}
