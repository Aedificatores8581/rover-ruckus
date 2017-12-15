package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DigitalChannel;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Conjured into existence by The Saminator on 12-09-2017.
 */
public class PingSensor {
    private DigitalChannel channel;
    private long lastReading;

    public static final double SPEED_OF_SOUND_MM_PER_NS = 0.000343;
    public static final double SPEED_OF_SOUND_CM_PER_NS = 0.0000343;
    public static final double SPEED_OF_SOUND_IN_PER_NS = 0.0000135039;

    public PingSensor(DigitalChannel channel) {
        this.channel = channel;
        lastReading = 0;
    }

    public void startReading() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                lastReading = 0;

                long timePulsed;

                channel.setMode(DigitalChannel.Mode.OUTPUT);
                channel.setState(true);
                channel.setState(false);

                channel.setMode(DigitalChannel.Mode.INPUT);
                timePulsed = System.nanoTime();
                while (channel.getState());

                lastReading = System.nanoTime() - timePulsed;
            }
        }).start();
    }

    public long getLastReadingNanoseconds() {
        return lastReading;
    }

    public double getLastReadingMillimeters() {
        return getLastReadingNanoseconds() * SPEED_OF_SOUND_MM_PER_NS;
    }

    public double getLastReadingCentimeters() {
        return getLastReadingNanoseconds() * SPEED_OF_SOUND_CM_PER_NS;
    }

    public double getLastReadingInches() {
        return getLastReadingNanoseconds() * SPEED_OF_SOUND_IN_PER_NS;
    }
}
