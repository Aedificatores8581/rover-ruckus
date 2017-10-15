package org.firstinspires.ftc.teamcode;

//------------------------------------------------------------------------------
//
// PootisBotManual
//

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * I made this from scratch.
 *
 * @author Pootis Man
 */
@TeleOp(name = "TestBot: Tele-Op", group = "the revolution never ends")

public class RevBotTeleTest extends RevBotTemplate
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
    @Override
    public void init() {
        super.init();

    }


    public RevBotTeleTest() {
        //
        // Initialize base classes.
        //
        // All via self-construction.
        //
        // Initialize class members.
        //
        // All via self-construction.



    }

    @Override
    public void start() {
    }

    //--------------------------------------------------------------------------
    //
    // loop
    //
    //-------
    // Initializes the class.
    //
    // The system calls this member repeatedly while the OpMode is running.
    //--------

    @Override public void loop () {
        double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
        int    CYCLE_MS    =   50;     // period of each cycle
        double MAX_POS     =  1.0;     // Maximum rotational position
        double MIN_POS     =  0.0;     // Minimum rotational position

        // Define class members
        Servo   servo;
        double  s1position = (MAX_POS - MIN_POS) / 2; // Start at halfway position
        double  s2position = (MAX_POS - MIN_POS) / 2;

        boolean rampUp = true;
        if (gamepad1.dpad_up)
            motor.setPower(-0.5);
        if (gamepad1.dpad_down)
            motor.setPower(0.5);
        if (!(gamepad1.dpad_up ^ gamepad1.dpad_down))
            motor.setPower(0);
        if (gamepad1.left_stick_y > 0) {
            // Keep stepping up until we hit the max value.
            s1position += INCREMENT ;
            if (s1position >= MAX_POS ) {
                s1position = MAX_POS;
                rampUp = !rampUp;   // Switch ramp direction
            }
        }
        else if(gamepad1.left_stick_y < 0) {
            s1position -= INCREMENT ;
            if (s1position <= MIN_POS ) {
                s1position = MIN_POS;
                rampUp = !rampUp;  // Switch ramp direction
        }
        else {
            // Keep stepping down until we hit the min value.

            }
        }

        // Display the current value
        telemetry.addData("Servo Position", "%5.2f", s1position);
        telemetry.addData(">", "Press Stop to end test." );
        telemetry.update();

        // Set the servo to the new position and pause;
        s1.setPosition(s1position);
      //  sleep(CYCLE_MS);
       // idle();
       // crs1.setPower(gamepad1.left_stick_y);
      //  crs2.setPower(gamepad1.right_stick_y);
    }

}

 // PootisBotManual
