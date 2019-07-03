package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Intake;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.MineralLiftState;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

@TeleOp (name = "OffSeason Tele Op")
public class RoverRuckusOffSeasonTeleOp extends WestBot15 {
    ExtensionState extensionState = ExtensionState.NON_RESETTING;
    double prevTime = 0;

    Gamepad prev1;
    ExtensionSafety extensionSafety;
    boolean canSwitchExtensionSafetyState;

    IntakeDoorState intakeDoorState;
    boolean canSwitchIntakeDoorState;
    double time = 0;
    boolean canSwitchTime = true;
    boolean canSwitchSlowdown = true, slowdown = true;
    boolean canSwitchMedium = true, Meduim = true;
    boolean isRetracted = false;
    double fitemetheo = 1;
    boolean soloDrive = true, soloDriveSlowdown = true;
    boolean mineralLiftSlowdown = true;
    boolean mineralLiftGamepad2Slowdown = true;
    private drivetrainspeed drivetrainspeed;


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

    public void start(){
        aextendo.isAutonomous = false;
        super.start();
        prevTime = UniversalFunctions.getTimeInSeconds();
        extensionState = ExtensionState.NON_RESETTING;
        drivetrainspeed = drivetrainspeed.medium;
    }
    public void loop() {
	updateGamepad1();
    updateGamepad2();

    //modifier section
    switch(drivetrainspeed) {
        case slow:
            drivetrain.maxSpeed = .3;
            drivetrain.turnMult = .7;
            break;
        case fast:
            drivetrain.maxSpeed = 1;
            drivetrain.turnMult = .5;
            break;
        case medium:
            drivetrain.maxSpeed = .6;
            drivetrain.turnMult = .6;
            break;
    }

    if (mineralLift.ServoAdjust == .01) mineralLift.ServoAdjust = .03 ;
	//Gamepad1 section

		//Driving
        drivetrain.leftPow = (gamepad1.right_trigger - gamepad1.left_trigger) + fitemetheo * leftStick1.x * drivetrain.turnMult;
        drivetrain.rightPow = (gamepad1.right_trigger - gamepad1.left_trigger) - fitemetheo * leftStick1.x * drivetrain.turnMult;
       // drivetrain.leftPow = (gamepad1.left_stick_y) + fitemetheo * leftStick1.x * drivetrain.turnMult;
       // drivetrain.rightPow = (gamepad1.left_stick_y) - fitemetheo * leftStick1.x * drivetrain.turnMult;
        drivetrain.setLeftPow();
        drivetrain.setRightPow();
			//Speed!
        if (gamepad1.left_stick_button && canSwitchSlowdown) {
            canSwitchSlowdown = false;
            if(drivetrainspeed == drivetrainspeed.medium) drivetrainspeed = drivetrainspeed.slow;
            else if(drivetrainspeed == drivetrainspeed.slow) drivetrainspeed = drivetrainspeed.fast;
            else if (drivetrainspeed == drivetrainspeed.fast) drivetrainspeed = drivetrainspeed.slow;
        } else if (!gamepad1.left_stick_button)
            canSwitchSlowdown = true;

        if (gamepad1.right_stick_button && canSwitchMedium) {
            canSwitchMedium = false;
            drivetrainspeed = drivetrainspeed.medium;
        } else if (!gamepad1.left_stick_button)
            canSwitchMedium = true;


		//Gamepad1 Intake and Mineral Container back position open for gamepad1 & gamepad2
		if (gamepad1.dpad_left) intaek.dispensor.setPosition(Intake.OPEN_DISPENSOR_POSITION);
        if (gamepad1.dpad_right) intaek.dispensor.setPosition(Intake.CLOSED_DISPENSOR_POSITION);
        if (gamepad1.dpad_up) intaek.articulator.setPosition(intaek.INTAKE_ARTICULATOR_UP_POSITION);
        if (gamepad1.dpad_down) intaek.articulateDown();

        if (gamepad1.left_bumper)
            intaek.setPower(1);
        else if (gamepad1.right_bumper && mineralLift.canSetPowerPositive)
            intaek.setPower(-1);
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
            lift.setPower(0);
        }
        else if (gamepad1.right_stick_y != 0 && drivetrainspeed == drivetrainspeed.slow && soloDrive)
            lift.setPower(-gamepad1.right_stick_y);
        else aextendo.aextendTM(rightStick1.y);

        if (!soloDrive) lift.setPower(-gamepad2.right_stick_y);

        //mineral lift automation
        if(gamepad1.a && mineralLiftSlowdown){
            if(mineralLift.isAutomationAllowed())
                mineralLift.allowAutomation(false);
            else if(!mineralLift.isAutomationAllowed())
                mineralLift.allowAutomation(true);
            mineralLiftSlowdown = false;
        }
        else if(!gamepad1.a)
            mineralLiftSlowdown = true;

        if(!mineralLift.isAutomationAllowed()) mineralLift.mineral_lift_stuck = true;
        else mineralLift.mineral_lift_stuck = false;

        //Lower
        if(gamepad1.b && mineralLift.isAutomationAllowed()){
            if (mineralLift.getMineralLiftState() == MineralLiftState.DONE_RAISING)
                mineralLift.setMineralLiftState(MineralLiftState.ARTICULATE_PIVOTS_DOWN);
        }
        //Raise
        if(gamepad1.y && mineralLift.isAutomationAllowed()){
            if (mineralLift.getMineralLiftState() == MineralLiftState.DONE_LOWERING)
                mineralLift.setMineralLiftState(MineralLiftState.EXTEND_LIFT);
        }
        //Automate!
        if(mineralLift.isAutomationAllowed()) mineralLift.automatedMineralLift();

	//Gamepad2
        //Two Drivers
        if (gamepad2.y && soloDriveSlowdown) {
            soloDrive = !soloDrive;
            soloDriveSlowdown = false;
        }
        else if (!gamepad2.y) soloDriveSlowdown = true;

		// Mineral Lift and Box Controls
        if (gamepad2.left_stick_button && mineralLiftGamepad2Slowdown) {
            mineralLift.mineral_lift_stuck = true;
            mineralLift.allowAutomation(false);
            mineralLiftGamepad2Slowdown = false;
        } else if (!gamepad2.left_stick_button)
            mineralLiftGamepad2Slowdown = true;

        if (!gamepad2.left_stick_button && !mineralLift.isAutomationAllowed()) {
            mineralLift.setLiftPower(leftStick2.y);
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
        telemetry.addData ("Drivetrain Speed", drivetrainspeed);
        telemetry.addData("Speed", drivetrain.maxSpeed);
        telemetry.addData("Drivetrain turning multiplier", drivetrain.turnMult);
        telemetry.addData("Mineral Lift State", mineralLift.getMineralLiftState());
        telemetry.addData("Mineral Lift Automation", mineralLift.isAutomationAllowed());
        telemetry.addData("Mineral Lift Servo Speed", mineralLift.ServoAdjust);
        telemetry.addData( "Solo Driving", soloDrive);
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

 enum drivetrainspeed {
    fast,
    medium,
    slow
}