package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DigitalChannel;

/**
 * Conjured into existence by The Saminator on 02-01-2018.
 */
public class MB1200 {
    public interface ReadingConsumer {
        void takeReading(double reading);
    }

    private DigitalChannel innerSensor;

    public MB1200(DigitalChannel is) {
        innerSensor = is;
    }

    public void read(final ReadingConsumer consumer) {
        new Thread() {
            @Override
            public void run() {
                innerSensor.setMode(DigitalChannel.Mode.OUTPUT);
                innerSensor.setState(true);

                long start = System.nanoTime();
                innerSensor.setMode(DigitalChannel.Mode.INPUT);
                while (!innerSensor.getState()) ;

                long readingNanos = System.nanoTime() - start;
                double readingCentis = readingNanos / 58000.0;
                consumer.takeReading(readingCentis);

                innerSensor.setMode(DigitalChannel.Mode.OUTPUT);
                innerSensor.setState(false);
            }
        }.start();
    }
}
