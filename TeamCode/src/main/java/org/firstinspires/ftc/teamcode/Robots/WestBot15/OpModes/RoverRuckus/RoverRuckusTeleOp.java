package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;

@TeleOp (name = "teleop2")
public class RoverRuckusTeleOp extends WestBot15 {
    ExtensionState extensionState = ExtensionState.NON_RESETTING;
    public void init(){
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
        drivetrain.maxSpeed = 0.7;
        updateGamepad1();
        updateGamepad2();
        drivetrain.turnMult = 1;
        if(!gamepad1.right_stick_button)
            drivetrain.turnMult = (1.0-0.75*aextendo.getExtensionLength()/29.0);
        drivetrain.leftPow = gamepad1.right_trigger - gamepad1.left_trigger - leftStick1.x * drivetrain.turnMult;
        drivetrain.rightPow = gamepad1.right_trigger - gamepad1.left_trigger + leftStick1.x * drivetrain.turnMult;
        drivetrain.setLeftPow();
        drivetrain.setRightPow();
        aextendo.aextendTM(rightStick1.y);
        /*
        if(gamepad1.a)
            extensionState = ExtensionState.RESETTING;
        switch (extensionState) {
            case NON_RESETTING:
                aextendo.aextendTM(rightStick1.y);
                if (gamepad1.left_bumper)
                    aextendo.articulateUp();
                else
                    aextendo.articulateDown();
                break;
            case RESETTING:
                aextendo.aextendTM(-1);
                aextendo.articulateUp();
                if (aextendo.isRetracted())
                    extensionState = ExtensionState.NON_RESETTING;
        }
        if(leftStick2.magnitude() > UniversalConstants.Triggered.STICK)
            lift2_0.lift(leftStick2.y);

        else if(gamepad2.dpad_up) {
            lift2_0.lift(1);
            mineralContainer.articulateUp();
        }
        else if(gamepad2.dpad_down) {
            lift2_0.lift(-1);
            mineralContainer.articulateDown();
        }
        if(gamepad1.left_trigger > UniversalConstants.Triggered.TRIGGER)
            intaek.dispense();
        if(gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER)
            mineralContainer.openCage();
        else
            mineralContainer.closeCage();*/
        telemetry.addData("extensionLength", aextendo.getExtensionLength());
        telemetry.addData("extension encoder val", aextendo.encoder.currentPosition);

    }
    enum ExtensionState{
        RESETTING,
        NON_RESETTING
    }
}
