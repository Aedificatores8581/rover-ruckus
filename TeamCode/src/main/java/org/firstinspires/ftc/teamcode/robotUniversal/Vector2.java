package org.firstinspires.ftc.teamcode.robotUniversal;

/**
 * Created by vzyrianov on 5/19/2018
 */

//2D Vector. Components are doubles.
public class Vector2 {
    public double x;
    public double y;

    public Vector2(){
        x = 0.0;
        y = 0.0;
    }

    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    //Sets the vector to equal the cartesian equivalent of the polar vector composed of a length and an angle
    public void setFromPolar(double r, double theta) {
        this.x = Math.cos(theta) * r;
        this.y = Math.sin(theta) * r;
    }

    public void add(Vector2 vector) {
        x += vector.x;
        y += vector.y;
    }

    public void subtract(Vector2 vector) {
        vector.scalarMultiply(-1);
        add(vector);
    }

    public void scalarMultiply(double a) {
        x *= a;
        y *= a;
    }

    //Length of vector
    public double magnitude() {
        return Math.sqrt(x*x + y*y);
    }

    //Dot Product
    public double dot(Vector2 vector) {
        return vector.x * this.x + vector.y * this.y;
    }

    //Angular component of the vector if it was converted to polar coordinates
    //Returns angle in radians
    public double angle() {
        return Math.atan2(y, x);
    }

    //Returns angle between two vectors in radians.
    public double angleBetween(Vector2 vector) {
        return Math.acos(this.dot(vector) / (this.magnitude() * vector.magnitude()));
    }

    //Copied from Point class.
    //TODO: Improve parameter naming
    public void updateLocation(double a, double ti, double tf, double c, double thf, double thi, int b, int d){
        //a is acceleration, ti is the time of the last iteration, tf is the current time, c is a distance conversion factor,
        //thf is the current angle, thi is the initial angle, b is either 1 or -1 depending on the initial angle, d is the distance to the center
        x += c * a * Math.pow((tf - ti), 2) * Math.cos(thf) / 2 - b * d * Math.cos(180 - thi);
        y += c * a * Math.pow((tf - ti), 2) * Math.sin(thf) / 2 - b * d * Math.sin(180 - thi);
    }

    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}
