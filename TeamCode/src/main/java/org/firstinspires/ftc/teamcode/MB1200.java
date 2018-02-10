package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DigitalChannel;

/**
 * Conjured into existence by The Saminator on 02-01-2018.
 */
public class MB1200 {
    public interface ReadingConsumer {
        void takeReading(double reading);
    }

    /**
     * Pin #2 is the data pin.
     */
    private DigitalChannel innerSensor;
    private Thread readThread;

    private volatile double reading;

    public MB1200(DigitalChannel is) {
        innerSensor = is;
        innerSensor.setMode(DigitalChannel.Mode.INPUT);
    }

    public void start() {
        readThread = new Thread() {
            @Override
            public void run() {
                boolean prevState = false;
                long lastReading = System.nanoTime();
                long prevReading = 0;

                while (!Thread.interrupted()) {
                    long readingNanos = lastReading - prevReading;

                    while (innerSensor.getState() == prevState) ;
                    reading = readingNanos / 58000.0;

                    prevReading = lastReading;
                    lastReading = System.nanoTime();
                }
            }
        };
        readThread.start();
    }

    public double getReading() {
        return reading;
    }

    public void stop() {
        readThread.interrupt();
    }
}
