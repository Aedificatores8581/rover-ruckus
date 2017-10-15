package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by The Saminator on 10-01-2017.
 */
@TeleOp(name = "Drivetrain Tele-Op Test", group = "SPECIALMAN")
public class DriveBotTeleop extends DriveBotTemplate {
    private boolean prevA;

    @Override
    public void init() {
        super.init();
        prevA = false;
    }

    @Override
    public void loop() {
        setLeftPow(gamepad1.left_stick_y * 0.5);
        setRightPow(gamepad1.right_stick_y * 0.5);

        if (gamepad1.a && !prevA)
            scream();

        prevA = gamepad1.a;
    }
}
