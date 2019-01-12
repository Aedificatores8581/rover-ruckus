package org.firstinspires.ftc.teamcode.Universal.Math.Expressions;

import org.opencv.core.Point;

public abstract class RealNonLinearFunction2 implements RealNonLinearExpression2 {

    public double evaluateAt(double x){
        return f(x)[0];
    }
    public double derivativeAt(double x){
        return derivative(new Point(x, evaluateAt(x)))[0];
    }
    public double
}
