package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;

@TeleOp (name = "teleop2")
public class RoverRuckusTeleOp extends WestBot15 {
    private static final double MAX_EXTENSION = 29.34;

    ExtensionState extensionState = ExtensionState.NON_RESETTING;

    public void init() {
        isAutonomous = false;
        usingIMU = false;

        super.init();

        activateGamepad1();
        activateGamepad2();
    }

    public void start(){
        super.start();
    }

    public void loop(){
        updateGamepad1();
        updateGamepad2();

        // drivetrain.leftPow = gamepad1.right_trigger - gamepad1.left_trigger - leftStick1.x;
        //  drivetrain.rightPow = gamepad1.right_trigger - gamepad1.left_trigger + leftStick1.x;

        // This button is temporary.
        if (!gamepad2.right_bumper) {
            drivetrain.leftPow = leftStick1.y - Math.abs(1 - (MAX_EXTENSION / aextendo.getExtensionLength()) * 0.1);
            drivetrain.rightPow = rightStick1.y - Math.abs(1 - (MAX_EXTENSION / aextendo.getExtensionLength()) * 0.1);
        } else {
            drivetrain.leftPow = leftStick1.y;
            drivetrain.rightPow = rightStick1.y;
        }

        drivetrain.setLeftPow();
        drivetrain.setRightPow();

        aextendo.aextendTM(rightStick1.x);

        telemetry.addData("extensionLength", aextendo.getExtensionLength());

    }

    enum ExtensionState{
        RESETTING,
        NON_RESETTING
    }
}
