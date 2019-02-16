package org.firstinspires.ftc.teamcode.Universal;

/**
 * Created by Frank Portman on 4/3/2018.
 */

public final class UniversalConstants {
    //Values required to register the gamepad's analog values
    public static final class Triggered {
        public static final double STICK = 0.2;
        public static final double TRIGGER = 0.2;
    }

    public static final int MS_STUCK_DETECT_INIT_DEFAULT = 500000;

    public static final class RoverRuckus{
        public static double robotAvoidanceThreshold = Math.hypot(9, 9);
    }

    public enum MarkerServoConstants {
        LEFT_CLOSED(.3),
        RIGHT_CLOSED(0.9),
        LEFT_OPEN(0.9),
        RIGHT_OPEN(0.0);

        double pos;

        MarkerServoConstants(double pos) {
            this.pos = pos;
        }

        public double getPos() {
            return pos;
        }
    }
}
