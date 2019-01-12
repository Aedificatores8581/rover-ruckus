package org.firstinspires.ftc.teamcode.Universal.Math.Expressions;

import org.opencv.core.Point;

interface RealNonLinearExpression2 {
    double[] f(double x);
    double[] derivative(Point point);
    double integral(Point point);
    boolean insideFunction(Point point);
}
