package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Conjured into existence by The Saminator on 11-24-2017.
 */
public class GyroAngles {
    public static final AxesOrder ORDER = AxesOrder.ZYX;
    public static final AngleUnit UNIT = AngleUnit.DEGREES;

    private final double z, y, x;

    public GyroAngles(Orientation angles) {
        z = AngleUnit.DEGREES.fromUnit(UNIT, angles.firstAngle);
        y = AngleUnit.DEGREES.fromUnit(UNIT, angles.secondAngle);
        x = AngleUnit.DEGREES.fromUnit(UNIT, angles.thirdAngle);
    }

    public double getZ() {
        return z;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }
}
