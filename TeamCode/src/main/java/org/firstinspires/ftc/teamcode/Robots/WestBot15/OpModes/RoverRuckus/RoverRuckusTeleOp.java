package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Intake;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

@Disabled
@TeleOp (name = "Comp Tele Op")
public class RoverRuckusTeleOp extends WestBot15 {
    ExtensionState extensionState = ExtensionState.NON_RESETTING;
    double prevTime = 0;

    Gamepad prev1;
    double liftSpeed = 0.75;
    ExtensionSafety extensionSafety;
    boolean canSwitchExtensionSafetyState;

    IntakeDoorState intakeDoorState;
    boolean canSwitchIntakeDoorState;
    double time = 0;
    boolean canSwitchTime = true;
    boolean canSwitchSlowdown = true, slowdown = true;
    boolean isRetracted = false;
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
        prevTime = 0;
        mineralContainer.articulateFront(mineralContainer.FRONT_UP_POSITION);
    }
    public void init_loop(){
        mineralContainer.articulateFront(0.32);
    }
    public void start(){
        aextendo.isAutonomous = false;
        super.start();
        prevTime = UniversalFunctions.getTimeInSeconds();
        extensionState = ExtensionState.NON_RESETTING;
    }
    public void loop() {
        if (gamepad1.left_stick_button && gamepad1.right_stick_button && canSwitchSlowdown) {
            slowdown = !slowdown;
            canSwitchSlowdown = false;
        } else if (!(gamepad1.left_stick_button && gamepad1.right_stick_button))
            canSwitchSlowdown = true;

        drivetrain.maxSpeed = 0.7;
        updateGamepad1();
        updateGamepad2();
        drivetrain.turnMult = 1;
        if (!gamepad1.left_stick_button && aextendo.getExtensionLength() > 10) {
            drivetrain.turnMult = (1.0 - 1.0 / 2.0 * (aextendo.getExtensionLength() - 10) / (aextendo.MAX_EXTENSION_LENGTH - 10));
        }
        if(aextendo.getExtensionLength() > 4){
            intaek.dispensor.setPosition(Intake.CLOSED_DISPENSOR_POSITION);
        }
        double fitemetheo = 1;
        drivetrain.leftPow = (gamepad1.right_trigger - gamepad1.left_trigger) + fitemetheo * leftStick1.x * drivetrain.turnMult;
        drivetrain.rightPow = (gamepad1.right_trigger - gamepad1.left_trigger) - fitemetheo * leftStick1.x * drivetrain.turnMult;
        drivetrain.setLeftPow();
        drivetrain.setRightPow();
        if (!gamepad2.right_stick_button) {
            lift.setPower(rightStick2.y);
        }
        if(rightStick1.magnitude() > UniversalConstants.Triggered.TRIGGER){
            isRetracted = false;
            switch(extensionState){
                case RESETTING:
                    intaek.articulateDown();
                    extensionState = ExtensionState.NON_RESETTING;
                    break;
                case NON_RESETTING:
                    aextendo.aextendTM(rightStick1.y);
                    break;
            }
        }
        else{
            switch(extensionState){
                case RESETTING:
                    isRetracted = true;
                    aextendo.aextendTM(-1);
                    if(aextendo.backSwitch.isPressed() || Math.abs(aextendo.extendo.getPower()) < 0.1) {
                        intaek.articulateDown();
                        intaek.dispensor.setPosition(Intake.OPEN_DISPENSOR_POSITION);
                        extensionState = ExtensionState.NON_RESETTING;
                    }
                    else{
                        intaek.articulator.setPosition(intaek.INTAKE_ARTICULATOR_MIDDLE_POSITION);
                    }
                    break;
                case NON_RESETTING:
                    if(isRetracted)
                        intaek.articulateDown();
                    aextendo.aextendTM(0);
                    if (gamepad1.a) {
                        extensionState = ExtensionState.RESETTING;
                    }
                    break;
            }
        }

        if (gamepad1.dpad_left) {
            intaek.dispensor.setPosition(Intake.OPEN_DISPENSOR_POSITION);
        }
        if (gamepad1.dpad_right) {
            intaek.dispensor.setPosition(Intake.CLOSED_DISPENSOR_POSITION);
        }
        if(gamepad2.left_bumper){
            intaek.articulateUp();
        }
        if (!gamepad2.left_stick_button) {
            mineralLift.setLiftPower(liftSpeed * leftStick2.y);
        }
        if(gamepad2.y){
            mineralLift.pivot1.setPosition(mineralLift.PIVOT_TELE_FORWARD_POS);
            mineralLift.pivot2.setPosition(mineralLift.PIVOT_TELE_FORWARD_POS);
        }
        if(!mineralLift.canSetPowerPositive)
            mineralContainer.articulateFront(mineralContainer.FRONT_DOWN_POSITION);
        else {
            if (gamepad2.dpad_up) {
                mineralContainer.articulateFront(mineralContainer.FRONT_UP_POSITION);
            } else if (gamepad2.dpad_down) {
                mineralContainer.articulateFront(mineralContainer.FRONT_DOWN_POSITION);
            }
        }
        if(gamepad2.a){
            intaek.dispensor.setPosition(Intake.OPEN_DISPENSOR_POSITION);
        }
        else if(gamepad2.b){
            intaek.dispensor.setPosition(Intake.CLOSED_DISPENSOR_POSITION);
        }
        liftSpeed = 0.95;

        if (gamepad1.left_bumper) {
            intaek.setPower(1);
            canSwitchTime = true;
            time = UniversalFunctions.getTimeInSeconds();
        } else if (gamepad1.right_bumper) {
            intaek.setPower(-1);
        } else {
            intaek.setPower(0);
            canSwitchTime = true;
            time = UniversalFunctions.getTimeInSeconds();
        }
        if (gamepad1.dpad_up)
            intaek.articulator.setPosition(intaek.INTAKE_ARTICULATOR_MIDDLE_POSITION);
        if (gamepad1.dpad_down)
            intaek.articulateDown();
        if(gamepad2.right_bumper && !mineralLift.canSetPowerPositive)
            mineralContainer.articulateBack(mineralContainer.BACK_OPEN_POSITION);
        else
            mineralContainer.articulateBack(mineralContainer.BACK_CLOSED_POSITION);
        if(gamepad1.dpad_left){
            intaek.closeDispensor();
        }
        if(gamepad1.dpad_right){
            intaek.openDispensor();
        }
        intaek.INTAKE_ARTICULATOR_MIDDLE_POSITION = UniversalFunctions.clamp(Intake.OPEN_DISPENSOR_POSITION, intaek.INTAKE_ARTICULATOR_MIDDLE_POSITION, Intake.CLOSED_DISPENSOR_POSITION);
        telemetry.addData("slow turning when extended: ", slowdown);
        telemetry.addData("extensionLength", aextendo.getExtensionLength());
        telemetry.addData("extension encoder val", aextendo.encoder.currentPosition);
        telemetry.addData("INTAKE_ARTICULATOR_MIDDLE_POSITION", intaek.INTAKE_ARTICULATOR_MIDDLE_POSITION);
        telemetry.addLine(lift.toString());
        telemetry.addData("extension state", extensionState);
        telemetry.addData("backSwitch", aextendo.backSwitch.isPressed());
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
}
