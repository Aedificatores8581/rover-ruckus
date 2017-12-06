package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Conjured into existence by The Saminator on 12-05-2017.
 */

@Autonomous(name = "Sensor Touch Demo", group = "feelz2thesequel")
public class Sensor2BotTouchDemo extends Sensor2BotTemplate {
    boolean runMotors;

    @Override
    public void start() {
        runMotors = true;
    }

    @Override
    public void loop() {
        if (runMotors)
            go();
        else
            stop();

        if (touch.isPressed())
            runMotors = false;
    }
}
