package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.AExtendoState;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.AExtendotm;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Intake;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.MineralLiftState;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

@TeleOp (name = "OffSeason Tele Op")
public class RoverRuckusOffSeasonTeleOp extends WestBot15 {
    double prevTime = 0;
    //Gamepad prev1;
    boolean canSwitchSlowdown = true;
    boolean canSwitchMedium = true;
    double fitemetheo = 1;
    boolean soloDrive = true, soloDriveSlowdown = true;
    boolean mineralLiftSlowdown = true;
    boolean mineralLiftGamepad2Slowdown = true;
    boolean configing = true;
    private drivetrainspeed drivetrainspeed;


    @Override
    public void init() {
        //prev1 = new Gamepad();
        isAutonomous = false;
        usingIMU = false;
        drivetrain.maxSpeed = 1;
        drivetrain.turnMult = .5;
        super.init();
        activateGamepad1();
        activateGamepad2();
        prevTime = 0;
        mineralContainer.articulateFront(mineralContainer.FRONT_UP_POSITION);
        //waitForStart();
    }
    public void init_loop() {
        //while (configing);
        telemetry.addData("PIVOT_TELE_FORWARD_POS", mineralLift.PIVOT_TELE_FORWARD_POS);
        telemetry.addData("PIVOT_TELE_DOWN_POS", mineralLift.PIVOT_TELE_DOWN_POS);
        telemetry.addData("PIVOT_TELE_UP_POS", mineralLift.PIVOT_TELE_UP_POS);
        telemetry.update();
        updateGamepad1();
        updateGamepad2();
        mineralLift.setLiftPower(0);
        if (gamepad1.dpad_down) {
            mineralLift.PIVOT_TELE_UP_POS = mineralLift.PIVOT_TELE_UP_POS - .001;
            mineralLift.PIVOT_TELE_UP_POS = UniversalFunctions.clamp(0, mineralLift.PIVOT_TELE_UP_POS, 1);
        }
        if (gamepad1.dpad_up){
            mineralLift.PIVOT_TELE_UP_POS = mineralLift.PIVOT_TELE_UP_POS + .001;
            mineralLift.PIVOT_TELE_UP_POS = UniversalFunctions.clamp(0, mineralLift.PIVOT_TELE_UP_POS, 1);
        }
        if (gamepad1.dpad_left) {
            mineralLift.PIVOT_TELE_DOWN_POS = mineralLift.PIVOT_TELE_DOWN_POS - .001;
            mineralLift.PIVOT_TELE_DOWN_POS = UniversalFunctions.clamp(0, mineralLift.PIVOT_TELE_DOWN_POS, 1);
        }
        if (gamepad1.dpad_right) {
            mineralLift.PIVOT_TELE_DOWN_POS = mineralLift.PIVOT_TELE_DOWN_POS + .001;
            mineralLift.PIVOT_TELE_DOWN_POS = UniversalFunctions.clamp(0, mineralLift.PIVOT_TELE_DOWN_POS, 1);
        }
        if (gamepad1.x) {
            mineralLift.PIVOT_TELE_FORWARD_POS = mineralLift.PIVOT_TELE_FORWARD_POS - .001;
            mineralLift.PIVOT_TELE_FORWARD_POS = UniversalFunctions.clamp(0, mineralLift.PIVOT_TELE_FORWARD_POS, 1);
        }
        if (gamepad1.y) {
            mineralLift.PIVOT_TELE_FORWARD_POS = mineralLift.PIVOT_TELE_FORWARD_POS + .001;
            mineralLift.PIVOT_TELE_FORWARD_POS = UniversalFunctions.clamp(0, mineralLift.PIVOT_TELE_FORWARD_POS, 1);
        }
        if(gamepad1.y) configing = false;
    }
    public void start() {
        aextendo.isAutonomous = false;
        super.start();
        prevTime = UniversalFunctions.getTimeInSeconds();
        drivetrainspeed = drivetrainspeed.medium;
        if (mineralLift.ServoAdjust == .005) mineralLift.ServoAdjust = .03;

    }

    public void loop() {
        updateGamepad1();
        updateGamepad2();

        //modifier section
        switch (drivetrainspeed) {
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

        //Gamepad1 section

        //Driving
        drivetrain.leftPow = (gamepad1.left_trigger - gamepad1.right_trigger) + fitemetheo * leftStick1.x * drivetrain.turnMult;
        drivetrain.rightPow = (gamepad1.left_trigger - gamepad1.right_trigger) - fitemetheo * leftStick1.x * drivetrain.turnMult;
        // drivetrain.leftPow = (gamepad1.left_stick_y) + fitemetheo * leftStick1.x * drivetrain.turnMult;
        // drivetrain.rightPow = (gamepad1.left_stick_y) - fitemetheo * leftStick1.x * drivetrain.turnMult;
        drivetrain.setLeftPow();
        drivetrain.setRightPow();
        //Speed!
        if (gamepad1.left_stick_button && canSwitchSlowdown) {
            canSwitchSlowdown = false;
            if (drivetrainspeed == drivetrainspeed.medium) drivetrainspeed = drivetrainspeed.slow;
            else if (drivetrainspeed == drivetrainspeed.slow)
                drivetrainspeed = drivetrainspeed.fast;
            else if (drivetrainspeed == drivetrainspeed.fast)
                drivetrainspeed = drivetrainspeed.slow;
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
        if (gamepad1.dpad_up && !mineralLift.isAutomationAllowed()) mineralContainer.articulateFront(mineralContainer.FRONT_UP_POSITION );
        else if (gamepad1.dpad_up && mineralLift.isAutomationAllowed()) intaek.articulateUp();
        if (gamepad1.dpad_down && !mineralLift.isAutomationAllowed()) mineralContainer.articulateFront(mineralContainer.FRONT_DOWN_POSITION);
        else if (gamepad1.dpad_down && mineralLift.isAutomationAllowed()) intaek.articulateDown();

        if (gamepad1.left_bumper)
            intaek.setPower(1);
        else if (gamepad1.right_bumper && mineralLift.canSetPowerPositive)
            intaek.setPower(-1);
        else if (gamepad1.right_bumper && !mineralLift.canSetPowerPositive)
            mineralContainer.articulateBack(mineralContainer.BACK_OPEN_POSITION);
        else {
            mineralContainer.articulateBack(mineralContainer.BACK_CLOSED_POSITION);
            if(aextendo.getAExtendoState()== AExtendoState.DRIVER) intaek.setPower(0);
        }

        // Extension movement
        if (rightStick1.y == 0) {
            if(aextendo.getAExtendoState()== AExtendoState.DRIVER) aextendo.aextendTM(0);
            lift.setPower(0);
        } else if (gamepad1.right_stick_y != 0 && drivetrainspeed == drivetrainspeed.slow && soloDrive)
            lift.setPower(-gamepad1.right_stick_y);
        else if(aextendo.getAExtendoState()== AExtendoState.DRIVER) aextendo.aextendTM(rightStick1.y);

                //mineral lift automation
        if (gamepad1.b && mineralLiftSlowdown) {
            if (mineralLift.isAutomationAllowed()) {
                mineralLift.allowAutomation(false);
                aextendo.allowAutomation(false);
                aextendo.setAextendotmState(AExtendoState.DRIVER);
                mineralLift.setMineralLiftState(MineralLiftState.STUCK);
                mineralLiftSlowdown = false;
            } else if (!mineralLift.isAutomationAllowed()) {
                mineralLift.allowAutomation(true);
                aextendo.allowAutomation(true);
                mineralLiftSlowdown = false;
            }
            } else if (!gamepad1.b)
                mineralLiftSlowdown = true;

            if (!mineralLift.isAutomationAllowed()) mineralLift.mineral_lift_stuck = true;
            else mineralLift.mineral_lift_stuck = false;

            if(mineralLift.mineral_lift_stuck) mineralLift.setLiftPower(gamepad1.left_stick_y);
            //Lower
            if (gamepad1.a && mineralLift.isAutomationAllowed()) {
                if (mineralLift.getMineralLiftState() == MineralLiftState.DONE_RAISING)
                    mineralLift.setMineralLiftState(MineralLiftState.ARTICULATE_PIVOTS_DOWN);
            }
            else if (gamepad1.a && !mineralLift.isAutomationAllowed()) {
                mineralLift.articulateDown();
               // mineralLift.pivot1.setPosition(mineralLift.PIVOT_TELE_DOWN_POS);
                //mineralLift.pivot2.setPosition(mineralLift.PIVOT_TELE_DOWN_POS);
            }
            //Raise
            if (gamepad1.y && mineralLift.isAutomationAllowed()) {
                if (mineralLift.getMineralLiftState() == MineralLiftState.DONE_LOWERING)
                    mineralLift.setMineralLiftState(MineralLiftState.EXTEND_LIFT);
            }
            else  if (gamepad1.y && !mineralLift.isAutomationAllowed())
            {
                //mineralLift.pivot1.setPosition(mineralLift.PIVOT_TELE_UP_POS);
                //mineralLift.pivot2.setPosition(mineralLift.PIVOT_TELE_UP_POS);
                mineralLift.articulateUp();
            }

            //Automatic Transfer
            if (gamepad1.x && aextendo.isAutomationAllowed()) {
                if (aextendo.getAExtendoState() == AExtendoState.DRIVER)
                    aextendo.setAextendotmState(AExtendoState.START_TRANSFER);
            }
            //Automate!
            if (mineralLift.isAutomationAllowed()) mineralLift.automatedMineralLift();
            if (aextendo.isAutomationAllowed()) aextendo.automatedTransfer();

            if (aextendo.getAExtendoState()== AExtendoState.LIFTING_INTAKE)
                intaek.articulateUp();
            else if (aextendo.getAExtendoState()== AExtendoState.OPENING_DISPENSOR || aextendo.getAExtendoState()== AExtendoState.OPENING_DISPENSOR2)
                intaek.openDispensor();
            else if (aextendo.getAExtendoState()== AExtendoState.RUN_INTAKE || aextendo.getAExtendoState()== AExtendoState.RUN_INTAKE2 || aextendo.getAExtendoState()== AExtendoState.RUN_INTAKE3 )
                intaek.setPower(-1);
            else if (aextendo.getAExtendoState()== AExtendoState.WAIT || aextendo.getAExtendoState()== AExtendoState.WAIT2 )
                intaek.setPower(0);
            else if (aextendo.getAExtendoState()== AExtendoState.LOWER_INTAKE)
                intaek.articulateDown();
            else if (aextendo.getAExtendoState()== AExtendoState.CLOSE_DISPENSOR)
                intaek.closeDispensor();

            //Gamepad2
            //Two Drivers

//Telemetry
            //telemetry.addData("INTAKE_ARTICULATOR_POSITION", intaek.articulator.getPosition());
            //telemetry.addData("Mineral Lift Stuck", mineralLift.mineral_lift_stuck);
            telemetry.addData("Drivetrain Speed", drivetrainspeed);
            //telemetry.addData("Speed", drivetrain.maxSpeed);
            //telemetry.addData("Drivetrain turning multiplier", drivetrain.turnMult);
            telemetry.addData("Mineral Lift State", mineralLift.getMineralLiftState());
            telemetry.addData("Extension State", aextendo.getAExtendoState());
            telemetry.addData("Automation", mineralLift.isAutomationAllowed());
            telemetry.addData("Mineral Lift Servo Speed", mineralLift.ServoAdjust);
            //telemetry.addData("Solo Driving", soloDrive);
            prevTime = UniversalFunctions.getTimeInSeconds();
            telemetry.update();

            //error catching maybe

        }


    enum drivetrainspeed {
        fast,
        medium,
        slow
    }
}