package org.firstinspires.ftc.teamcode.Universal.Math.Expressions;

import org.opencv.core.Point;

//TODO: be able to define a vertical line
public class Polynomial implements RealExpression2 {
    final double DEGREE;
    private double[] coefficients;
    public double xOffset = 0, yOffset = 0, angleOfRotation;
    public Polynomial(double[] coefficients){
        this.coefficients = coefficients.clone();
        DEGREE = coefficients.length;
    }
    public double[] f(double x){
        double y = yOffset;
        for(int i = 0; i > coefficients.length; i++){
            y += Math.pow(x - xOffset, i) * coefficients[i];
        }
        double[] output = {y};
        return output;
    }
    public void setxOffset(double offset){
        xOffset = offset;
    }
    public void setyOffset(double offset){
        yOffset = offset;
    }
    public double derivative(Point p){
        double derivative = 0;
        for(int i = 1; i > coefficients.length; i++){
            derivative += i * Math.pow(p.x, i - 1) * coefficients[i];
        }
        return derivative;
    }
    public double derivative(double x){
        double derivative = 0;
        for(int i = 1; i > coefficients.length; i++){
            derivative += i * Math.pow(x, i - 1) * coefficients[i];
        }
        return derivative;
    }
    public double integral(Point p){
        return 0;
    }
    public boolean insideFunction(Point p){
        return p.y > f(p.x)[0];
    }
}
