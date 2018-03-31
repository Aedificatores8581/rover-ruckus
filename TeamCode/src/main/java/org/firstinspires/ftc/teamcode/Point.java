package org.firstinspires.ftc.teamcode;

/**
 * Created by fgpor on 3/19/2018.
 */

public class Point {
    protected double x;
    protected double y;
    protected int mult = 1;

    public Point(){
        x = 0;
        y = 0;
    }
    public Point(double X, double Y){
        x = X;
        y = Y;
    }
    public void addX(double xn){
        x += xn;
    }

    public void addY(double yn){
        y += yn;
    }
    public void updateLocation(double a, double ti, double tf, double c, double thf, double thi, int b, int d){
        //a is acceleration, ti is the time of the last iteration, tf is the current time, c is a distance conversion factor,
        //thf is the curret angle, thi is the initial angle, b is either 1 or -1 depending on the initial angle, d is the distance to the center
        x += c * a * Math.pow((tf - ti), 2) * Math.cos(thf) / 2 - b * d * Math.cos(180 - thi);
        y += c * a * Math.pow((tf - ti), 2) * Math.sin(thf) / 2 - b * d * Math.sin(180 - thi);
    }
    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}
