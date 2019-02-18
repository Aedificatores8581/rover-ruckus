package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Intake;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.MineralLiftState;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.NewMineralLift;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

@TeleOp (name = "Comp Tele Op")
public class RoverRuckusTeleOp extends WestBot15 {
    ExtensionState extensionState = ExtensionState.NON_RESETTING;
    double prevTime = 0;

    Gamepad prev1;
    double liftSpeed = 0.5;
    ExtensionSafety extensionSafety;
    boolean canSwitchExtensionSafetyState;

    IntakeDoorState intakeDoorState;
    boolean canSwitchIntakeDoorState;
    IntakeResetState intakeResetState = IntakeResetState.RETRACT;
    double time = 0;
    boolean canSwitchTime = true;
    boolean canSwitchSlowdown = true, slowdown = true;
    @Override
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

        super.init();
        extensionSafety = ExtensionSafety.DISABLED;
        intakeDoorState = IntakeDoorState.CLOSED;
        activateGamepad1();
        activateGamepad2();
    }
    public void start(){
        aextendo.isAutonomous = false;
        super.start();
        prevTime = UniversalFunctions.getTimeInSeconds();
    }
    public void loop() {
        if(gamepad1.left_stick_button && gamepad1.right_stick_button && canSwitchSlowdown){
            slowdown = !slowdown;
            canSwitchSlowdown = false;
        }
        else if(!(gamepad1.left_stick_button && gamepad1.right_stick_button))
            canSwitchSlowdown = true;

        drivetrain.maxSpeed = 0.7;
        updateGamepad1();
        updateGamepad2();
        drivetrain.turnMult = 1;

        if ( !gamepad1.left_stick_button && aextendo.getExtensionLength() > 10) {
            drivetrain.turnMult = (1.0 - 2.0 / 3.0 * (aextendo.getExtensionLength() - 10) / (aextendo.MAX_EXTENSION_LENGTH - 10));
        }

        double fitemetheo = 1;
        drivetrain.leftPow = (gamepad1.right_trigger - gamepad1.left_trigger) + fitemetheo * leftStick1.x * drivetrain.turnMult;
        drivetrain.rightPow = (gamepad1.right_trigger - gamepad1.left_trigger) - fitemetheo * leftStick1.x * drivetrain.turnMult;
        drivetrain.setLeftPow();
        drivetrain.setRightPow();
        if(!gamepad2.right_stick_button){
            lift.setPower(rightStick2.y);
        }

        if(gamepad2.dpad_up){
            mineralLift.articulatePivots(NewMineralLift.PIVOT_TELE_DOWN_POS);
        } else if(gamepad2.dpad_down){
            mineralLift.articulatePivots(NewMineralLift.PIVOT_TELE_UP_POS);
        }

        if (gamepad1.a) {
            extensionState = ExtensionState.RESETTING;
            intakeResetState = IntakeResetState.RETRACT;
        }
        if (leftStick1.magnitude() > 0.2) {
            extensionState = ExtensionState.NON_RESETTING;
            //amineralContainer.articulateDown();
            //mineralContainer.closeCage();
        }


        if (gamepad1.dpad_left) {
            intaek.dispensor.setPosition(Intake.OPEN_DISPENSOR_POSITION);
        }
        if (gamepad1.dpad_right) {
            intaek.dispensor.setPosition(Intake.CLOSED_DISPENSOR_POSITION);
        }

        aextendo.aextendTM(rightStick1.y);

        if(!gamepad2.left_stick_button){
            mineralLift.setLiftPower(liftSpeed * leftStick2.y);
        }

        if(gamepad2.dpad_up && !mineralLift.isMovingLift()) {
            mineralLift.automatedRaise();
        } else if(gamepad2.dpad_down && !mineralLift.isMovingLift()){
            mineralLift.automatedLower();
        }

        if(false) {
            if (gamepad2.dpad_right)
                liftSpeed += 0.3 * (UniversalFunctions.getTimeInSeconds() - prevTime);
            if (gamepad2.dpad_left)
                liftSpeed -= 0.3 * (UniversalFunctions.getTimeInSeconds() - prevTime);
            liftSpeed = UniversalFunctions.clamp(0, liftSpeed, 1);
        }
        else
            liftSpeed = 0.75;
        if (gamepad1.left_bumper) {
            intaek.setPower(-1);
            canSwitchTime = true;
            time = UniversalFunctions.getTimeInSeconds();
        }
        else if (gamepad1.right_bumper) {
            if(UniversalFunctions.getTimeInSeconds() - time < 0.08){
                intaek.setPower(1);
            }
            else{
                intaek.setPower(1);
            }
        }
        else {
            intaek.setPower(0);
            canSwitchTime = true;
            time = UniversalFunctions.getTimeInSeconds();
        }
        /*
        if(gamepad2.right_trigger > 0.2)
            lift2_0.articulate(-1);
        else if(gamepad2.right_bumper)
            lift2_0.articulate(1);*/


       if(gamepad1.dpad_up)
            intaek.articulateUp();
        if(gamepad1.dpad_down)
            intaek.articulateDown();


        /*if(gamepad1.right_trigger > UniversalConstants.Triggered.TRIGGER)
            mineralContainer.openCage();
        else
            mineralContainer.closeCage();*/

        telemetry.addData("slow turning when extended: ", slowdown);
        telemetry.addData("extensionLength", aextendo.getExtensionLength());
        telemetry.addData("extension encoder val", aextendo.encoder.currentPosition);
        telemetry.addData("topSensor", lift.topPressed());
        telemetry.addData("bottomSensor", lift.bottomPressed());
        telemetry.addLine(lift.toString());
        telemetry.addData("Lift Enc", mineralLift.getLiftEncoder());

        telemetry.addData("lift max speed", liftSpeed);
        prevTime = UniversalFunctions.getTimeInSeconds();

        // Determines Whether to slow down the intake
       /* switch (extensionSafety) {
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
        }*/
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
