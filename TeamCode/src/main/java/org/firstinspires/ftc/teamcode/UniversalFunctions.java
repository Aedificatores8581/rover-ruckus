package org.firstinspires.ftc.teamcode;

/**
 * Created by Frank Portman on 4/1/2018.
 */

public abstract class UniversalFunctions {
    public static double round(double d) {
        if (d < 0) {
            return Math.floor(d);
        }
        return Math.ceil(d);
    }
    public static double normalizeAngle(double angle, double newStartAngle){
        angle -= newStartAngle;
        double a2 = Math.abs(angle) %  360;
        if(Math.abs(angle) != angle){
            return 360 - a2;
        }
        return a2;
    }

}
