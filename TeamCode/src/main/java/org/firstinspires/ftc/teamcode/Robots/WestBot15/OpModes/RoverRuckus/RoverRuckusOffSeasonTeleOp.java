package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Intake;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

@TeleOp (name = "OffSeason Tele Op")
public class RoverRuckusOffSeasonTeleOp extends WestBot15 {
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
        drivetrain.maxSpeed = 1;
        drivetrain.turnMult = .5;
        super.init();
        extensionSafety = ExtensionSafety.DISABLED;
        intakeDoorState = IntakeDoorState.CLOSED;
        activateGamepad1();
        activateGamepad2();
        prevTime = 0;
        mineralContainer.articulateFront(mineralContainer.FRONT_UP_POSITION);
    }
/* No idea why a loop move front articulator to a set position	
    public void init_loop(){
        mineralContainer.articulateFront(0.32);
    }
*/
    public void start(){
        aextendo.isAutonomous = false;
        super.start();
        prevTime = UniversalFunctions.getTimeInSeconds();
        extensionState = ExtensionState.NON_RESETTING;
    }
    public void loop() {
	updateGamepad1();
    updateGamepad2();
	//Gamepad1 section
		//Driving
	    double fitemetheo = 1; //what is this for?
        drivetrain.leftPow = (gamepad1.right_trigger - gamepad1.left_trigger) + fitemetheo * leftStick1.x * drivetrain.turnMult;
        drivetrain.rightPow = (gamepad1.right_trigger - gamepad1.left_trigger) - fitemetheo * leftStick1.x * drivetrain.turnMult;
        drivetrain.setLeftPow();
        drivetrain.setRightPow();
			//Speed!
        if (gamepad1.left_stick_button && canSwitchSlowdown) {
            canSwitchSlowdown = false;
            slowdown = !slowdown;
            if (slowdown){
                drivetrain.maxSpeed = .3;
                drivetrain.turnMult = .6;
            } else
            {
                drivetrain.maxSpeed = 1;
                drivetrain.turnMult = .5;
            }
        } else if (!gamepad1.left_stick_button)
            canSwitchSlowdown = true;

		//Gamepad1 Intake    
		if (gamepad1.dpad_left) {
            intaek.dispensor.setPosition(Intake.OPEN_DISPENSOR_POSITION);
        }
        if (gamepad1.dpad_right) {
            intaek.dispensor.setPosition(Intake.CLOSED_DISPENSOR_POSITION);
        }
        if (gamepad1.dpad_up) {
            intaek.articulator.setPosition(intaek.INTAKE_ARTICULATOR_MIDDLE_POSITION);
		}

        if (gamepad1.dpad_down){
            intaek.articulateDown();
        }

        if (gamepad1.left_bumper)
            intaek.setPower(1);
        else if (gamepad1.right_bumper && mineralLift.canSetPowerPositive)
            intaek.setPower(-1);
        //lift
        else if(gamepad1.right_bumper && !mineralLift.canSetPowerPositive)
            mineralContainer.articulateBack(mineralContainer.BACK_OPEN_POSITION);
        else if(gamepad2.right_bumper && !mineralLift.canSetPowerPositive)
            mineralContainer.articulateBack(mineralContainer.BACK_OPEN_POSITION);
        else {
            mineralContainer.articulateBack(mineralContainer.BACK_CLOSED_POSITION);
            intaek.setPower(0);
        }

        // Extension movement
        if (rightStick1.y == 0) {
            aextendo.aextendTM(0);
        }
        else if (gamepad1.right_stick_y != 0 && slowdown) {
            aextendo.aextendTM(UniversalFunctions.clamp(-1, gamepad1.right_stick_y, 0));
            lift.setPower(-gamepad1.right_stick_y);
        }
        if (gamepad1.right_stick_y == 0) {
            lift.setPower(-gamepad2.right_stick_y);
        }
        else aextendo.aextendTM(rightStick1.y);

        //mineral lift automation
        if(gamepad1.a && canSwitchSlowdown){
            if(mineralLift.isAutomationAllowed())
                mineralLift.allowAutomation(false);
            else if(!mineralLift.isAutomationAllowed())
                mineralLift.allowAutomation(true);
            canSwitchSlowdown = false;
        }
        else if(!gamepad1.a)
            canSwitchSlowdown = true;

        if(gamepad1.b && canSwitchSlowdown && !mineralLift.isMovingLift()){
            mineralLift.automatedMineralLift();
            canSwitchSlowdown = false;
        }
        else if(!gamepad1.b)
            canSwitchSlowdown = true;
	//Gamepad2
		//Hang Lift Controls


		// Mineral Lift and Box Controls
        if (gamepad2.left_stick_button && canSwitchSlowdown) {
            mineralLift.mineral_lift_stuck = !mineralLift.mineral_lift_stuck;
            mineralLift.allowAutomation(false);
            canSwitchSlowdown = false;
        } else if (!gamepad2.left_stick_button)
            canSwitchSlowdown = true;

        if (!gamepad2.left_stick_button) {
            mineralLift.setLiftPower(leftStick2.y);
        }

        if(gamepad2.y){
            mineralLift.pivot1.setPosition(mineralLift.PIVOT_TELE_FORWARD_POS);
            mineralLift.pivot2.setPosition(mineralLift.PIVOT_TELE_FORWARD_POS);
            mineralContainer.articulateFront(mineralContainer.FRONT_DOWN_POSITION);
        }
        if (gamepad2.dpad_up) {
                mineralContainer.articulateFront(mineralContainer.FRONT_UP_POSITION);
            } else if (gamepad2.dpad_down) {
                mineralContainer.articulateFront(mineralContainer.FRONT_DOWN_POSITION);
            }

        //Intake controls
		if(gamepad2.a){
            intaek.articulateUp();
        }
        else if(gamepad2.b){
            intaek.articulateDown();
        }

        if(gamepad2.dpad_left){
            intaek.closeDispensor();
        }
        if(gamepad2.dpad_right){
            intaek.openDispensor();
        }
		

//Telemetry
        telemetry.addData("INTAKE_ARTICULATOR_POSITION", intaek.articulator.getPosition());
        telemetry.addData("Mineral Lift Stuck", mineralLift.mineral_lift_stuck);
        telemetry.addData("Slowdown - False = Fast - True = Slow", slowdown);
        telemetry.addData("Speed", drivetrain.maxSpeed);
        telemetry.addData("Drivetrain turning multiplier", drivetrain.turnMult);
        telemetry.addData("Mineral Lift State", mineralLift.getMineralLiftState());
        telemetry.addData("Mineral Lift Automation", mineralLift.isAutomationAllowed());
        telemetry.addData("Controller Delay", canSwitchSlowdown);
        prevTime = UniversalFunctions.getTimeInSeconds();
        telemetry.update();

     //error catching maybe
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
