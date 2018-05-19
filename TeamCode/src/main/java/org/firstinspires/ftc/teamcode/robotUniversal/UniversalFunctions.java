package org.firstinspires.ftc.teamcode.robotUniversal;

import org.opencv.core.Mat;

import java.util.Arrays;

/**
 * Created by Frank Portman on 4/1/2018.
 */
 //
public abstract class UniversalFunctions {
    public static double round(double d) {
        if (d < 0) {
            return Math.floor(d);
        }
        return Math.ceil(d);
    }
    public static double normalizeAngle(double angle, double newStartAngle) {
        angle -= newStartAngle;
        double a2 = Math.abs(angle) % 360;
        if (Math.abs(angle) != angle) {
            return 360 - a2;
        }
        return a2;
    }
    public static double normalizeAngle(double angle) {
        double a2 = Math.abs(angle) % 360;
        if (Math.abs(angle) != angle) {
            return 360 - a2;
        }
        return a2;
    }
    public static double normalizeAngle180(double angle, double newStartAngle) {
        double ang = normalizeAngle(angle, newStartAngle);
        if(ang > 180){
            ang = -180 + ang % 360;
        }
        return ang;
    }
    public static double normalizeAngle180(double angle) {
        double ang = normalizeAngle(angle);
        if(ang > 180){
            ang = -180 + ang % 360;
        }
        return ang;
    }





    public static double max(double... ds) {
        switch(ds.length){
            case 0: return 0.0;
            case 1: return ds[0];
            case 2: return Math.max(ds[0], ds[1]);
            default: return Math.max(ds[0],
                    max(Arrays.copyOfRange(ds, 1, ds.length)));
        }
    }

    public static double maxAbs(double... ds) {
        for (int i = 0; i < ds.length; ++i){
            ds[i] = Math.abs(ds[i]);
        }

        switch(ds.length){
            case 0: return 0.0;
            case 1: return ds[0];
            case 2: return Math.max(ds[0], ds[1]);
            default: return Math.max(ds[0],
                    max(Arrays.copyOfRange(ds, 1, ds.length)));
        }
    }

    public static double min(double... ds){
        switch(ds.length){
            case 0: return 0.0;
            case 1: return ds[0];
            case 2: return Math.min(ds[0], ds[1]);
            default: return Math.min(ds[0],
                    min(Arrays.copyOfRange(ds,1, ds.length)));
        }
    }

    public static double minAbs(double... ds){
        for (int i = 0; i < ds.length; ++i){
            ds[i] = Math.abs(ds[i]);
        }
        switch(ds.length){
            case 0: return 0.0;
            case 1: return ds[0];
            case 2: return Math.min(ds[0], ds[1]);
            default: return Math.min(ds[0],
                    min(Arrays.copyOfRange(ds,1, ds.length)));
        }
    }
}
