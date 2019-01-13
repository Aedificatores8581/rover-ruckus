package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.AExtendotm;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Intake;
import org.firstinspires.ftc.teamcode.Components.Sensors.TouchSensor;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

@TeleOp (name = "Comp Tele Op")
public class RoverRuckusTeleOp extends WestBot15 {
    ExtensionState extensionState = ExtensionState.NON_RESETTING;
    double prevTime = 0;

    Gamepad prev1;

    ExtensionSafety extensionSafety;
    boolean canSwitchExtensionSafetyState;

    IntakeDoorState intakeDoorState;
    IntakeResetState intakeResetState = IntakeResetState.RETRACT;
    boolean canSwitchIntakeDoorState;

    public void init(){
        prev1 = new Gamepad();
        canSwitchExtensionSafetyState = true;

        try {
            prev1.copy(gamepad1);
        } catch (RobotCoreException e) {
            e.printStackTrace();
        }

        isAutonomous = false;
        usingIMU = false;

        extensionSafety = ExtensionSafety.DISABLED;
        intakeDoorState = IntakeDoorState.CLOSED;

        super.init();
        activateGamepad1();
        activateGamepad2();
    }

    public void start(){
        super.start();
        prevTime = UniversalFunctions.getTimeInSeconds();
    }

    public void loop(){
        drivetrain.maxSpeed = 0.7;
        drivetrain.turnMult = 1;

        updateGamepad1();
        updateGamepad2();

        // TODO: Wait, do we need this?
        if (!gamepad1.left_stick_button && aextendo.getExtensionLength() > 10) {
            drivetrain.turnMult = (1.0 - 2.0/3.0 * (aextendo.getExtensionLength() - 10) / (aextendo.MAX_EXTENSION_LENGTH - 10));
        }

        drivetrain.leftPow = gamepad1.right_trigger - gamepad1.left_trigger - leftStick1.x * drivetrain.turnMult;
        drivetrain.rightPow = gamepad1.right_trigger - gamepad1.left_trigger + leftStick1.x * drivetrain.turnMult;
        drivetrain.setLeftPow();
        drivetrain.setRightPow();
        lift.setPower(gamepad2.left_stick_y);

        if (gamepad1.a) {
            extensionState = ExtensionState.RESETTING;
            intakeResetState = IntakeResetState.RETRACT;
        }

        if(leftStick1.magnitude() > 0.2) {
            extensionState = ExtensionState.NON_RESETTING;
            mineralContainer.articulateDown();
            mineralContainer.closeCage();
        }

        if(gamepad2.left_trigger > 0.2){
            mineralContainer.openCage();
            mineralContainer.articulateUp();
        }
        aextendo.aextendTM(rightStick1.y);

        if (gamepad1.left_bumper) {
            intaek.setPower(1);
        } else if (gamepad1.right_bumper) {
            intaek.setPower(-1);
        } else {
            intaek.setPower(0);
        }
        intaek.dispensor.setPosition(Intake.CLOSED_DISPENSOR_POSITION);

        if (gamepad1.dpad_up) {
            intaek.articulateUp();
        }

        if (gamepad1.dpad_down) {
            intaek.articulateDown();
        }

        intaek.dispensor.setPosition(Intake.CLOSED_DISPENSOR_POSITION);

        telemetry.addData("extensionLength", aextendo.getExtensionLength());
        telemetry.addData("extension encoder val", aextendo.encoder.currentPosition);
        telemetry.addLine(lift.toString());

        prevTime = UniversalFunctions.getTimeInSeconds();

        switch (extensionSafety) {
            case ENABLED:
                drivetrain.turnMult = (1.0 - 2.0 / 3.0 * (aextendo.getExtensionLength() - 10) / (aextendo.MAX_EXTENSION_LENGTH - 10));

                if (gamepad1.y && canSwitchExtensionSafetyState) {
                    extensionSafety = ExtensionSafety.DISABLED;
                    canSwitchExtensionSafetyState = false;
                } else if (!gamepad1.y && aextendo.getExtensionLength() == 0) {
                    canSwitchExtensionSafetyState = true;
                }
                break;

            case DISABLED:
                if (gamepad1.y && canSwitchExtensionSafetyState) {
                    extensionSafety = ExtensionSafety.ENABLED;
                    canSwitchExtensionSafetyState = false;
                }  else if (!gamepad1.y) {
                    canSwitchExtensionSafetyState = true;
                }
                break;
        }

        try {
            prev1.copy(gamepad1);
        } catch (RobotCoreException e) {
            telemetry.addLine(e.getMessage());
        }
    }

    enum ExtensionState{
        RESETTING,
        NON_RESETTING
    }

    enum ExtensionSafety {
        ENABLED,
        DISABLED
    }

    enum IntakeDoorState {
        OPEN,
        CLOSED
    }

    enum IntakeResetState{
        RETRACT,
        AIM,
        OUTTAKE
    }
}
