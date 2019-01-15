package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.Drivetrain;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

/**
 * Created by Frank Portman on 6/17/2018
 */
@TeleOp(name = "Field Centric Test", group = "")
public class DriveBotFieldCentricTest extends WestBot15 {
    boolean switchControlState    = false,
            canSwitchControlState = false,
            switchTurnState       = false,
            canSwitchTurnState    = false;

    @Override
    public void init(){
        usingIMU = true;
        super.init();
        activateGamepad1();
        drivetrain.controlState = TankDT.ControlState.TANK;
        drivetrain.turnState = TankDT.FCTurnState.FAST;
        drivetrain.direction = TankDT.Direction.BACK;
        drivetrain.directionMult = 1;
    }

    @Override
    public void start(){
        super.start();
        setStartAngle();
    }

    @Override
    public void loop() {
        drivetrain.updateLocation();
        drivetrain.maxSpeed = 0.5;
        setRobotAngle();
        updateGamepad1();
        if(leftStick1.magnitude() > 0.2) {
            drivetrain.newFieldCentric(leftStick1, robotAngle, 1);
        }
        else
            drivetrain.stop();
        telemetry.addData("position", drivetrain.position);
        telemetry.addData("gyro angle", robotAngle.angle());
        telemetry.addData("stick", leftStick1);
        telemetry.addData("leftPow", drivetrain.leftPow);
        telemetry.addData("rightPow", drivetrain.rightPow);
    }
}
