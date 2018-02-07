package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Conjured into existence by The Saminator on 12-09-2017.
 */
public class PingSensor{
    private DigitalChannel channel;
    private volatile long lastReading;
    private volatile long timePulsed;

    enum RunState {
        NULL("Doing Nothing"), SET_MODE_OUTPUT("Setting Mode to Output"), SET_MODE_INPUT("Setting Mode to Input"),
            BEFORE_WHILE("Before while(channel.getState());"), AFTER_WHILE("After while(channel.getState());"),
            LAST_READING_SET("Set Last Reading");

        String description;

        RunState(String s){
            description = s;
        }
        public String getDescription(){
            return description;
        }
    }

    private volatile RunState runState; // Used to track what part of "run" the Thread in startReading is in

    public static final double SPEED_OF_SOUND_MM_PER_NS = 0.000343;
    public static final double SPEED_OF_SOUND_CM_PER_NS = 0.0000343;
    public static final double SPEED_OF_SOUND_IN_PER_NS = 0.0000135039;

    public volatile boolean isRunningThread; // For exiting the loop in the method startReading
    public PingSensor(DigitalChannel channel) {
        this.channel = channel;
        lastReading = 0;
        timePulsed = 0;
        runState = RunState.NULL;

    }

    @Override public String toString(){
        return "Channel: " + channel + "\nLast Reading: " + lastReading + "\n";
    }

    public void startReading() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(isRunningThread) {
                    runState = RunState.SET_MODE_OUTPUT;
                    channel.setMode(DigitalChannel.Mode.OUTPUT);
                    channel.setState(true);
                    channel.setState(false);

                    runState = RunState.SET_MODE_INPUT;
                    channel.setMode(DigitalChannel.Mode.INPUT);
                    timePulsed = System.nanoTime();

                    runState = RunState.BEFORE_WHILE;
                    while (channel.getState()) ;

                    runState = RunState.AFTER_WHILE;

                    lastReading = System.nanoTime() - timePulsed;
                    runState = RunState.LAST_READING_SET;
                }
            }
        }).start();
    }

    public long getTimePulsed(){
        return this.timePulsed;
    }

    public String getRunState(){
        return runState.getDescription();
    }


    public boolean getChannelState(){
        return channel.getState();
    }

    public boolean isReadingFresh() {
        return lastReading != 0;
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
