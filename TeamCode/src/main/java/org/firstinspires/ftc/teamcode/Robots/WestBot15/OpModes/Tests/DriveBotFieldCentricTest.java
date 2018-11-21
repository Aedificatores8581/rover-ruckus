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
@TeleOp(name = "WestBotFieldCentricTest", group = "")
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
        drivetrain.direction = TankDT.Direction.FOR;
    }

    @Override
    public void start(){
        super.start();
    }

    @Override
    public void loop() {
        updateGamepad1();
        setRobotAngle();
        double angleBetween = UniversalFunctions.normalizeAngleRadians(leftStick1.angle(), robotAngle.angle());
        if (leftStick1.magnitude() < UniversalConstants.Triggered.STICK) {
            drivetrain.leftFore.setPower(0);
            drivetrain.leftRear.setPower(0);
            drivetrain.rightFore.setPower(0);
            drivetrain.rightRear.setPower(0);
        } else {
            switch (drivetrain.direction) {
                case FOR:
                    if (Math.sin(angleBetween) < 0 && drivetrain.turn) {
                        drivetrain.direction = Drivetrain.Direction.BACK;
                        drivetrain.directionMult *= -1;
                        drivetrain.turn = false;
                    } else if (Math.sin(angleBetween) >= 0)
                        drivetrain.turn = true;
                    break;

                case BACK:
                    if (Math.sin(angleBetween) > 0 && drivetrain.turn) {
                        drivetrain.direction = Drivetrain.Direction.FOR;
                        drivetrain.turn = false;
                        drivetrain.directionMult *= -1;
                    } else if (Math.sin(angleBetween) <= 0)
                        drivetrain.turn = true;
                    break;
            }

            double cos = Math.cos(angleBetween);
            double turnMult = Math.abs(cos) + 1;
            double leftPow = drivetrain.directionMult * (leftStick1.magnitude() - turnMult * cos);
            double rightPow = drivetrain.directionMult * (leftStick1.magnitude() + turnMult * cos);
            drivetrain.leftFore.setPower(leftPow);
            drivetrain.leftRear.setPower(leftPow);
            drivetrain.rightFore.setPower(rightPow);
            drivetrain.rightRear.setPower(rightPow);
        }
    }
}
