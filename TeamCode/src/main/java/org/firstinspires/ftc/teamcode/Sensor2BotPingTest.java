package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Hunter Seachrist on 12/5/2017.
 */
@TeleOp(name = "SensorBot: Ping Test", group = "bepis")

public class Sensor2BotPingTest extends Sensor2BotTemplate {
    private boolean prevA;
    private long[] lastTwentyReadings;
    private static final byte LAST_TWENTY_READINGS_LENGTH = 20;
    private byte index;

    public void start() {
        lastTwentyReadings = new long[LAST_TWENTY_READINGS_LENGTH];
        index = 0;
        telemetry.addLine("Calling startReading()");
        ping.startReading();
        telemetry.addLine("startReading() Called");
        ping.isRunningThread = true;
    }

    public void loop() {
        telemetry.addLine("Checking isReadingFresh()");
        if (ping.isReadingFresh()) {
            telemetry.addData("Nanoseconds from Thingy", ping.getLastReadingNanoseconds());
            if (index == LAST_TWENTY_READINGS_LENGTH - 1) {
                lastTwentyReadings[0] = 0;
                lastTwentyReadings = rotateValuesBack(lastTwentyReadings, LAST_TWENTY_READINGS_LENGTH);
                lastTwentyReadings[index] = ping.getLastReadingNanoseconds();
            } else {
                lastTwentyReadings[index] = ping.getLastReadingNanoseconds();
                ++index;

            }
            telemetry.addData("Average of last twenty",
                    (sum(lastTwentyReadings, LAST_TWENTY_READINGS_LENGTH)) / LAST_TWENTY_READINGS_LENGTH);

            telemetry.addData("Last twenty \"Last Readings\"", longArrayToString(lastTwentyReadings));
        }

        telemetry.addLine(ping.toString());
        telemetry.addData("Time Pulsed", ping.getTimePulsed());
        telemetry.addData("Reading Status in Thread", ping.getRunState());
        telemetry.addData("Ping Sensor State", ping.getChannelState());
    }

    private long[] rotateValuesBack(long param[], byte length) {
        for (byte i = 1; i < length; ++i) {
            param[i - 1] = param[i];
        }
        return param;
    }

    private long sum(long param[], byte length) {
        long sum = 0;
        for (byte i = 0; i < length; ++i) {
            sum += param[i];
        }

        return sum;
    }

    private String longArrayToString(long[] values) {
        StringBuilder sb = new StringBuilder("[");
        for (long val : values)
            sb.append(val).append(", ");
        return sb.append("]").toString();
    }

}

