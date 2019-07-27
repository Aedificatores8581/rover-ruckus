package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Intake;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.MineralLift;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.MineralLiftState;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

@TeleOp (name = "RR2 Outreach")
public class RR2OutreachTeleOp extends WestBot15 {
    double prevTime = 0;

    boolean TakeControl = false, TakeControlSlowdown = true;
    double fitemetheo = 1;
    boolean mineralLiftSlowdown = true;

    @Override
    public void init(){
        isAutonomous = false;
        usingIMU = false;
        drivetrain.maxSpeed = .3;
        drivetrain.turnMult = .8;
        super.init();
        activateGamepad1();
        activateGamepad2();
        mineralContainer.articulateFront(mineralContainer.FRONT_UP_POSITION);
    }

    public void start(){
        aextendo.isAutonomous = false;
        super.start();
        prevTime = UniversalFunctions.getTimeInSeconds();
        if (mineralLift.ServoAdjust == .03) mineralLift.ServoAdjust = .005;
    }

    public void loop() {
        updateGamepad1();
        updateGamepad2();
        //modifier section

        //Gamepad1 section
        //Driving
        if (!TakeControl) {
            drivetrain.leftPow = (gamepad1.left_stick_y) + fitemetheo * leftStick1.x * drivetrain.turnMult;
            drivetrain.rightPow = (gamepad1.left_stick_y) - fitemetheo * leftStick1.x * drivetrain.turnMult;
        }
        else {
            drivetrain.leftPow = (gamepad2.right_trigger - gamepad2.left_trigger) + fitemetheo * leftStick2.x * drivetrain.turnMult;
            drivetrain.rightPow = (gamepad2.right_trigger - gamepad2.left_trigger) - fitemetheo * leftStick2.x * drivetrain.turnMult;
        }
        drivetrain.setLeftPow();
        drivetrain.setRightPow();
        //Gamepad1 Intake and Mineral Container back position open for gamepad1 & gamepad2
        if (gamepad1.dpad_left) intaek.dispensor.setPosition(Intake.OPEN_DISPENSOR_POSITION);
        if (gamepad1.dpad_right) intaek.dispensor.setPosition(Intake.CLOSED_DISPENSOR_POSITION);

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

        //mineral lift automation
        if(gamepad2.a && mineralLiftSlowdown){
            if(mineralLift.isAutomationAllowed())
                mineralLift.allowAutomation(false);
            else if(!mineralLift.isAutomationAllowed())
                mineralLift.allowAutomation(true);
            mineralLiftSlowdown = false;
        }
        else if(!gamepad2.a)
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
        if(gamepad2.b && TakeControlSlowdown){
            TakeControl = !TakeControl;
            TakeControlSlowdown = false;
        }
        else if(!gamepad2.b)
            TakeControlSlowdown = true;

        if (gamepad2.x) {
            drivetrain.maxSpeed = 0;
            drivetrain.turnMult = 0;
        }
        else {
            drivetrain.maxSpeed = .3;
            drivetrain.turnMult = .8;
        }
        // Mineral Lift and Box Controls
        if (!gamepad2.left_stick_button && !mineralLift.isAutomationAllowed()) {
            mineralLift.setLiftPower(leftStick2.y);
        }
//Telemetry
        telemetry.addData("Speed", drivetrain.maxSpeed);
        telemetry.addData("Drivetrain turning multiplier", drivetrain.turnMult);
        telemetry.addData("Mineral Lift State", mineralLift.getMineralLiftState());
        telemetry.addData("Mineral Lift Automation", mineralLift.isAutomationAllowed());
        telemetry.addData("Take Control", TakeControl);
        telemetry.update();
    }

}
