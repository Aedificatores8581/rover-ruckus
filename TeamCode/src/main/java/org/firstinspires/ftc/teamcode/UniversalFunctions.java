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
    protected static double normalizeAngle(double angle) {
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
    protected static double normalizeAngle180(double angle) {
        double ang = normalizeAngle(angle);
        if(ang > 180){
            ang = -180 + ang % 360;
        }
        return ang;
    }
    













    /*
    *
    * MAXIMUM FUNCTIONS
    *
    */
    protected static double max(double a, double b, double c){
        return Math.max(Math.max(a, b), c);
    }
    protected static double max(double a, double b, double c, double d){
        return Math.max(max(a, b, c), d);
    }
    protected static double max(double a, double b, double c, double d, double e){
        return Math.max(max(a, b, c, d), e);
    }
    protected static double max(double a, double b, double c, double d, double e, double f){
        return Math.max(max(a, b, c, d, e), f);
    }
    protected static double max(double a, double b, double c, double d, double e, double f, double g){
        return Math.max(max(a, b, c, d, e, f), g);
    }
    protected static double max(double a, double b, double c, double d, double e, double f, double g, double h){
        return Math.max(max(a, b, c, d, e, f, g), h);
    }
    protected static double maxAbs(double a, double b, double c){
        return Math.max(Math.max(Math.abs(a), Math.abs(b)), Math.abs(c));
    }
    protected static double maxAbs(double a, double b, double c, double d){
        return Math.max(maxAbs(a, b, c), Math.abs(d));
    }
    protected static double maxAbs(double a, double b, double c, double d, double e){
        return Math.max(maxAbs(a, b, c, d), Math.abs(e));
    }
    protected static double maxAbs(double a, double b, double c, double d, double e, double f){
        return Math.max(maxAbs(a, b, c, d, e), Math.abs(f));
    }
    protected static double maxAbs(double a, double b, double c, double d, double e, double f, double g){
        return Math.max(maxAbs(a, b, c, d, e, f), Math.abs(g));
    }
    protected static double maxAbs(double a, double b, double c, double d, double e, double f, double g, double h){
        return Math.max(maxAbs(a, b, c, d, e, f, g), Math.abs(h));
    }

    /*
    *
    * MINIMUM FUNCTIONS
    *
    */
    protected static double min(double a, double b, double c){
        return Math.min(Math.min(a, b), c);
    }
    protected static double min(double a, double b, double c, double d){
        return Math.min(min(a, b, c), d);
    }
    protected static double min(double a, double b, double c, double d, double e){
        return Math.min(min(a, b, c, d), e);
    }
    protected static double min(double a, double b, double c, double d, double e, double f){
        return Math.min(min(a, b, c, d, e), f);
    }
    protected static double min(double a, double b, double c, double d, double e, double f, double g){
        return Math.min(min(a, b, c, d, e, f), g);
    }
    protected static double min(double a, double b, double c, double d, double e, double f, double g, double h){
        return Math.min(min(a, b, c, d, e, f, g), h);
    }
    protected static double minAbs(double a, double b, double c){
        return Math.min(Math.min(Math.abs(a), Math.abs(b)), Math.abs(c));
    }
    protected static double minAbs(double a, double b, double c, double d){
        return Math.min(maxAbs(a, b, c), Math.abs(d));
    }
    protected static double minAbs(double a, double b, double c, double d, double e){
        return Math.min(maxAbs(a, b, c, d), Math.abs(e));
    }
    protected static double minAbs(double a, double b, double c, double d, double e, double f){
        return Math.min(maxAbs(a, b, c, d, e), Math.abs(f));
    }
    protected static double minAbs(double a, double b, double c, double d, double e, double f, double g){
        return Math.min(maxAbs(a, b, c, d, e, f), Math.abs(g));
    }
    protected static double minAbs(double a, double b, double c, double d, double e, double f, double g, double h){
        return Math.min(maxAbs(a, b, c, d, e, f, g), Math.abs(h));
    }
}
