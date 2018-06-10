package org.firstinspires.ftc.teamcode.robotUniversal;

/**
 * Created by vzyrianov on 5/22/2018
 */

public class Vector3 {
    public double x;
    public double y;
    public double z;

    public Vector3 () {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void add(Vector3 vector) {
        x += vector.x;
        y += vector.y;
        z += vector.z;
    }

    //NOTE TO PREVIOUS AUTHOR: Previous version multiplied the argument by -1, which was possibly unintended behavior. This new version doesn't modify the argument.
    public void subtract(Vector3 vector) {
        x -= vector.x;
        y -= vector.y;
        z -= vector.z;
    }

    public void scalarMultiply(double multiplier) {
        x *= multiplier;
        y *= multiplier;
        z *= multiplier;
    }

    //Length of vector
    public double getMagnitude() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    //Dot Product
    public double dot(Vector3 vector) {
        return vector.x * this.x + vector.y * this.y + vector.z * this.z;
    }

    //Cross Product
    public Vector3 cross(Vector3 vector) {
        Vector3 result = new Vector3();

        result.x = (this.y * vector.z) - (this.z * vector.y);
        result.y = (this.z * vector.x) - (this.x * vector.z);
        result.z = (this.x * vector.y) - (this.y * vector.x);

        return result;
    }

    //Returns the getAngle of how much the vector is raised or lowered vertically.
    public double getVerticalAngle() {
        return Math.atan2(this.z, getMagnitude());
    }

    //Returns the getAngle of how much the vector is turned horizontally, around the vertical axis.
    public double getHorizontalAngle() {
        return Math.atan2(this.y, this.x);
    }

    //Returns getAngle between two vectors in radians.
    public double getAngleBetween(Vector3 vector) {
        return Math.acos(this.dot(vector) / (this.getMagnitude() * vector.getMagnitude()));
    }

    public String toString(){
        return "(" + x + ", " + y + ", " + z + ")";
    }
}