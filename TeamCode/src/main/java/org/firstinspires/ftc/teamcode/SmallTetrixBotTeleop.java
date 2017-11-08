package org.firstinspires.ftc.teamcode;

import java.lang.Object.*;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import java.math.*;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;

import java.util.Locale;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * I made this from scratch.
 *
 * @author Pootis Man
 */
@TeleOp(name = "Teleop from Akron Minimaker Fair", group = "bepis")

public class SmallTetrixBotTeleop extends TestBotTemplate   // TestBot Template is used due to it only having the left and right
                                                            // Motors included
                                                            // TODO: Rename TestBotTemplate
{

    //--------------------------------------------------------------------------
    //
    //
    //
    //--------
    // Constructs the class.
    //
    // The system calls this member when the class is instantiated.
    //--------


    double leftPow = 0.0;
    double rightPow = 0.0;
    public void start() {
    }

    @Override public void loop () {
        if (gamepad1.left_stick_y < -0.5){
            leftPow = 1.0;
        } else if (gamepad1.left_stick_y > 0.5){
            leftPow = -1.0;
        }else{
            leftPow = 0.0;
        }

        if (gamepad1.right_stick_y < -0.5){
            rightPow = 1.0;
        } else if (gamepad1.right_stick_y> 0.5){
            rightPow = -1.0;
        }else{
            rightPow = 0.0;
        }

        setLeftPow(leftPow);
        setRightPow(rightPow);
    }
}





