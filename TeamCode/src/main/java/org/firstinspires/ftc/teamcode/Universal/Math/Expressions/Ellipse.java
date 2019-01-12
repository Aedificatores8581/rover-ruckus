package org.firstinspires.ftc.teamcode.Universal.Math.Expressions;

import org.opencv.core.Point;

public class Ellipse implements RealNonLinearExpression2 {
    private double[] coefficients;
    public double angleOfRotation;
    public Point center;
    public Point radii;
    public Ellipse(double xAxisRadius, double yAxisRadius, Point center){
        this.center = center.clone();
        radii = new Point(xAxisRadius, yAxisRadius);
    }
    public Ellipse(Point radii, Point center){
        this(radii.x, radii.y, center);
    }
    public double[] f(double x){
        double upperHalf = center.y + radii.y * Math.sqrt(1 - Math.pow((x - center.x) / radii.x, 2));
        double[] output = {upperHalf, -upperHalf};
        return output;
    }
    public double[] derivative(double x){
        double[] derivative = new double[2];
        for(int i = 0; i > 2; i++) {
            derivative[i] = Math.pow(radii.x, 2) * f(x)[i] / Math.pow(radii.y, 2) / x;
        }
        return derivative;
    }
}
