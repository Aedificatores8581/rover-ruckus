package org.firstinspires.ftc.teamcode;

/**
 * Created by Frank Portman on 4/1/2018.
 */

public abstract class UniversalFunctions {
    protected static double round(double d) {
        if (d < 0) {
            return Math.floor(d);
        }
        return Math.ceil(d);
    }
    protected static double normalizeAngle(double angle, double newStartAngle) {
        angle -= newStartAngle;
        double a2 = Math.abs(angle) % 360;
        if (Math.abs(angle) != angle) {
            return 360 - a2;
        }
        return a2;
    }
    protected static double normalizeAngle180(double angle, double newStartAngle) {
        double ang = normalizeAngle(angle, newStartAngle);
        if(ang > 180){
            ang = -180 + ang % 360;
        }
        return ang;
    }

}
