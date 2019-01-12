package org.firstinspires.ftc.teamcode.Universal.Math.Expressions;

import org.opencv.core.Point;

public interface RealExpression2 {
    double[] f(double x);
    double derrivitave(Point point);
    double integral(Point point);
    boolean insideFunction(Point point);
}
