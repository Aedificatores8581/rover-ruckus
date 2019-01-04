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
        updateGamepad1();
        updateGamepad2();
        drivetrain.turnMult = 1;

        if(!gamepad1.left_stick_button&&aextendo.getExtensionLength() > 10 ) {
            drivetrain.turnMult = (1.0 - 2.0/3.0 * (aextendo.getExtensionLength()-10) / (aextendo.MAX_EXTENSION_LENGTH-10));
        }

        drivetrain.leftPow = gamepad1.right_trigger - gamepad1.left_trigger - rightStick1.x * drivetrain.turnMult;
        drivetrain.rightPow = gamepad1.right_trigger - gamepad1.left_trigger + rightStick1.x * drivetrain.turnMult;
        drivetrain.setLeftPow();
        drivetrain.setRightPow();
        lift.setPower(gamepad2.left_stick_y);

        if (gamepad1.x) {
            extensionState = ExtensionState.RESETTING;
        }

        switch (extensionState) {
            case NON_RESETTING:
                aextendo.aextendTM(leftStick1.y);
                if (gamepad1.left_bumper) {
                    intaek.articulateUp();
                } else {
                    intaek.articulateDown();
                }
                break;
            case RESETTING:
                aextendo.aextendTM(-1);
                intaek.articulateUp();
                if (aextendo.isRetracted())
                    extensionState = ExtensionState.NON_RESETTING;
        }


        /*if(leftStick2.magnitude() > UniversalConstants.Triggered.STICK) {
            lift2_0.lift(leftStick2.y);
        }
        else if(gamepad2.dpad_up) {
            lift2_0.lift(1);
            mineralContainer.articulateUp();
        }
        else if(gamepad2.dpad_down) {
            lift2_0.lift(-1);
            mineralContainer.articulateDown();
        }*/


        if (gamepad1.dpad_up) intaek.dispense();

        if (gamepad1.right_bumper) {
            intaek.setPower(intaek.maxSpeed);
        } else {
            intaek.setPower(0.0);
        }

        /*if(gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER)
            mineralContainer.openCage();
        else
            mineralContainer.closeCage();*/

        telemetry.addData("extensionLength", aextendo.getExtensionLength());
        telemetry.addData("extension encoder val", aextendo.encoder.currentPosition);
        telemetry.addLine(lift.toString());
        prevTime = UniversalFunctions.getTimeInSeconds();

        // Determines Whether to slow down the intake
        switch (extensionSafety) {
            case ENABLED:
                drivetrain.turnMult = (1.0 - 2.0/3.0 * (aextendo.getExtensionLength()-10) / (aextendo.MAX_EXTENSION_LENGTH-10));

                if (gamepad1.y && canSwitchExtensionSafetyState) {
                    extensionSafety = ExtensionSafety.DISABLED;
                    canSwitchExtensionSafetyState = false;
                } else if (!gamepad1.y) {
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

        switch (intakeDoorState) {
            case OPEN:
                intaek.dispensor.setPosition(Intake.OPEN_DISPENSOR_POSITION);

                if (gamepad1.dpad_down && canSwitchIntakeDoorState) {
                    intakeDoorState = IntakeDoorState.CLOSED;
                    canSwitchIntakeDoorState = false;
                } else if (!gamepad1.dpad_down) {
                    canSwitchIntakeDoorState = true;
                }
                break;
            case CLOSED:
                intaek.dispensor.setPosition(Intake.CLOSED_DISPENSOR_POSITION);
                if (gamepad1.dpad_up && canSwitchIntakeDoorState) {
                    intakeDoorState = IntakeDoorState.OPEN;
                    canSwitchIntakeDoorState = false;
                }  else if (!gamepad1.dpad_up) {
                    canSwitchIntakeDoorState = true;
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
}
