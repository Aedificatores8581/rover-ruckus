package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by The Saminator on 10-01-2017.
 */
@TeleOp(name = "Drivetrain Tele-Op Test", group = "SPECIALMAN")
public class DriveBotTeleop extends DriveBotTemplate {
    private boolean prevA;
    private Timer async;

    private boolean lifting;

    @Override
    public void init() {
        super.init();
        prevA = false;
    }

    protected void highDelivery() {
        lifting = true;
        async.schedule(new TimerTask() {
            @Override
            public void run() {
                setLift2Pow(0.5);
            }
        }, 0);
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
