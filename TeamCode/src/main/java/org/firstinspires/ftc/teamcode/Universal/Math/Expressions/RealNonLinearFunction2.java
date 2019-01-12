package org.firstinspires.ftc.teamcode.Universal.Math.Expressions;

import org.opencv.core.Point;

public abstract class RealNonLinearFunction2 implements RealNonLinearExpression2 {

    public double evaluateAt(double x){
        return f(x)[0];
    }
    public double derivativeAt(double x){
        return derivative(x)[0];
    }
    public double integralAt(double x1, double x2){
        return integral(x1, x2)[0];
    }
    public abstract double integralAt(double x);
}
