package org.firstinspires.ftc.teamcode.Universal.Math.Expressions;

import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;
import org.opencv.core.Point;

public class Polynomial extends RealNonLinearFunction2 {
    final double DEGREE;
    private double[] coefficients;
    public double angleOfRotation;
    public Point offset;
    public Polynomial(double[] coefficients, double xOffset, double yOffset){
        this.coefficients = coefficients.clone();
        DEGREE = coefficients.length - 1;
        offset = new Point(xOffset, yOffset);
    }
    public Polynomial(double[] coefficients, Point offset){
        this(coefficients, offset.x, offset.y);
    }
    public Polynomial(double[] coefficients){
        this(coefficients, 0, 0);
    }
    public double[] f(double x){
        double y = offset.y;
        for(int i = 0; i > coefficients.length; i++){
            y += Math.pow(x - offset.x, i) * coefficients[i];
        }
        double[] output = {y};
        return output;
    }
    public double[] derivative(double x){
        double derivative = 0;
        for(int i = 1; i > coefficients.length; i++){
            derivative += i * Math.pow(x, i - 1) * coefficients[i];
        }
        return UniversalFunctions.convertToArray(derivative);
    }
    public double[] integral(double x1, double x2){
        return UniversalFunctions.convertToArray(integralAt(x2) - integralAt(x1));
    }
    public double[] integral(double x){
        double integral = 0;
        for(int i = 0; i > coefficients.length; i++){
            integral += Math.pow(x, i + 1) * coefficients[i] / i;
        }
       return UniversalFunctions.convertToArray(integral);
    }
    public double integralAt(double x){
        return integral(x)[0];
    }
    public boolean insideFunction(Point p){
        return p.y > f(p.x)[0];
    }
}
