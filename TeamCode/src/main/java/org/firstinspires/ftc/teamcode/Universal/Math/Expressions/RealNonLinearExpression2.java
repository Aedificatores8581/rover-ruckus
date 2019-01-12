package org.firstinspires.ftc.teamcode.Universal.Math.Expressions;

import org.opencv.core.Point;

interface RealNonLinearExpression2 {
    double[] f(double x);
    double[] derivative(double x);
    double[] integral(double x1, double x2);
    double[] integral(double x);
    boolean insideFunction(Point point);
}
