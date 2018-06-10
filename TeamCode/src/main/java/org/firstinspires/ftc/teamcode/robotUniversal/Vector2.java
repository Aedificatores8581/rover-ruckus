package org.firstinspires.ftc.teamcode.robotUniversal;

/**
 * Created by vzyrianov on 5/19/2018
 */

//2D Vector. Components are doubles.
public class Vector2 {
    public double x;
    public double y;

    public Vector2() {
        x = 0.0;
        y = 0.0;
    }

    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    //Sets the vector to equal the cartesian equivalent of the polar vector composed of a length and an getAngle
    public void setFromPolar(double r, double theta) {
        this.x = Math.cos(theta) * r;
        this.y = Math.sin(theta) * r;
    }

    public void add(Vector2 vector) {
        x += vector.x;
        y += vector.y;
    }

    //NOTE TO PREVIOUS AUTHOR: Previous version multiplied the argument by -1, which was possibly unintended behavior. This new version doesn't modify the argument.
    public void subtract(Vector2 vector) {
        x -= vector.x;
        y -= vector.y;
    }

    public void scalarMultiply(double multiplier) {
        x *= multiplier;
        y *= multiplier;
    }

    //Length of vector
    public double getMagnitude() {
        return Math.sqrt(x*x + y*y);
    }

    //Dot Product
    public double dot(Vector2 vector) {
        return vector.x * this.x + vector.y * this.y;
    }

    //Cross Product of two 2d vectors is always pointed either into or out of the 2d plane, thus it can be represented with a single number.
    public double cross(Vector2 vector) {
        return (this.x * vector.y) - (this.y * vector.x);
    }

    //Angular component of the vector if it was converted to polar coordinates
    //Returns getAngle in radians
    public double getAngle() {
        return Math.atan2(y, x);
    }

    //Returns getAngle between two vectors in radians.
    public double getAngleBetween(Vector2 vector) {
        return Math.acos(this.dot(vector) / (this.getMagnitude() * vector.getMagnitude()));
    }

    public String toString(){
        return "(" + x + ", " + y + ")";
    }
}