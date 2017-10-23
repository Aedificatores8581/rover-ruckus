package org.firstinspires.ftc.teamcode;

//------------------------------------------------------------------------------
//
// Sterrett Arm Testing
//

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Sterrett
 */
@TeleOp(name = "ArmBot: Tele-Op", group = "the revolution never ends")

public class ArmTeleTest extends LinearOpMode {
    // Define class members
    DcMotor motor;
    Servo s1, s2, s3, s4;

    double s1position = 0;
    double s2position = 0;
    double s3position = 0;
    double s4position = 0;


    //--------------------------------------------------------------------------
    //
    //
    //
    //--------
    // Constructs the class.
    //
    // The system calls this member when the class is instantiated.
    //--------
    @Override
    public void runOpMode() {
        double INCREMENT = 0.005;     // amount to slew servo each CYCLE_MS cyclE
        double INCREMENT2 = .02;
        double MAX_POS = 1;     // Maximum rotational position
        double MIN_POS = 0;     // Minimum rotational position
        motor = hardwareMap.dcMotor.get("motor");
        s1 = hardwareMap.servo.get("s1");
        s2 = hardwareMap.servo.get("s2");
        s3 = hardwareMap.servo.get("s3");
        s4 = hardwareMap.servo.get("s4");

        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.dpad_up)
                motor.setPower(-0.5);
            if (gamepad1.dpad_down)
                motor.setPower(0.5);
            if (!(gamepad1.dpad_up ^ gamepad1.dpad_down))
                motor.setPower(0);
            if (gamepad1.left_stick_y < 0) {
                // Keep stepping up until we hit the max value.
                s1position += INCREMENT;
                if (s1position >= MAX_POS) {
                    s1position = MAX_POS;
                }
                s1.setPosition(s1position);
            }
            if (gamepad1.left_stick_y > 0) {
                s1position -= INCREMENT;
                if (s1position <= MIN_POS) {
                    s1position = MIN_POS;
                }
                s1.setPosition(s1position);
            }
            if (gamepad1.right_stick_y < 0) {
                // Keep stepping up until we hit the max value.
                s2position += INCREMENT;
                if (s2position >= MAX_POS) {
                    s2position = MAX_POS;
                }
                s2.setPosition(s2position);
            }
            if (gamepad1.right_stick_y > 0) {
                s2position -= INCREMENT;
                if (s2position <= MIN_POS) {
                    s2position = MIN_POS;
                }
                s2.setPosition(s2position);
            }
            if (gamepad1.x) {
                s3position += INCREMENT2;
                if (s3position >= MAX_POS) {
                    s3position = MAX_POS;
                }
                s3.setPosition(s3position);
            }
            if (gamepad1.y) {
                s3position -= INCREMENT2;
                if (s3position <= MIN_POS) {
                    s3position = MIN_POS;
                }
                s3.setPosition(s3position);
            }

            if (gamepad1.a) {
                s4position += INCREMENT2;
                if (s4position >= MAX_POS) {
                    s4position = MAX_POS;
                }
                s4.setPosition(s4position);
            }

            if (gamepad1.b) {
                s4position -= INCREMENT2;
                if (s4position <= MIN_POS) {
                    s4position = MIN_POS;
                }
                s4.setPosition(s4position);
            }

            sleep(30);
            idle();
            telemetry.addData("Status", "Running");
            telemetry.addData("Servo Position 1", "%5.2f", s1.getPosition());
            telemetry.addData("Servo Position 2", "%5.2f", s2.getPosition());
            telemetry.addData("Servo Position 3", "%5.2f", s3.getPosition());
            telemetry.addData("Servo Position 4", "%5.2f", s4.getPosition());
            telemetry.update();

        }
    }
}
    // Display the current value

