package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Hunter Seachrist on 12/5/2017.
 */
@TeleOp(name = "SensorBot: Ping Test", group = "bepis")
@Disabled
public class Sensor2BotPingTest extends Sensor2BotTemplate {
    private boolean prevA;

    public void start(){
        telemetry.addLine("Calling startReading()");
        ping.startReading();
        telemetry.addLine("startReading() Called");
    }

    int count;
    public void loop() {
        telemetry.addLine("Checking isReadingFresh()");
        if (ping.isReadingFresh()) {
            telemetry.addData("Nanoseconds from Thingy", ping.getLastReadingNanoseconds());
        }

        telemetry.addLine(ping.toString());
        telemetry.addData("Time Pulsed", ping.getTimePulsed());
        telemetry.addData("Reading Status in Thread", ping.getRunState());
        telemetry.addData("Count",count);
        telemetry.addData("Ping Sensor State", ping.getChannelState());

        count++;
    }

}
