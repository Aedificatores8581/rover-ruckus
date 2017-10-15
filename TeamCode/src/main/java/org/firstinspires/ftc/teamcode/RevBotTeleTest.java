package org.firstinspires.ftc.teamcode;

//------------------------------------------------------------------------------
//
// PootisBotManual
//

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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
        if (gamepad1.dpad_up)
            motor.setPower(-0.2);
        else if (gamepad1.dpad_down)
            motor.setPower(0.2);

        crs1.setPower(gamepad1.left_stick_y);
        crs2.setPower(gamepad1.right_stick_y);
    }

}

 // PootisBotManual
