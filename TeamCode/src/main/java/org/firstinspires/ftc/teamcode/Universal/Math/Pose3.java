package org.firstinspires.ftc.teamcode.Universal.Math;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class Pose3 {
    public double x, y, z, xAngle, yAngle, zAngle;
    public Pose3(double x, double y, double z, double xAngle, double yAngle, double zAngle){
        this.x = x;
        this.y = y;
        this.z = z;
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        this.zAngle = zAngle;
    }
    public Pose3(Pose pose){
        this(pose.x, pose.y, 0, 0, 0, 0);
    }
    public Pose3(){
        x = 0;
        y = 0;
        z = 0;
        xAngle = 0;
        yAngle = 0;
        zAngle = 0;
    }
    public void add(Pose3 pose){
        x += pose.x;
        y += pose.y;
        z += pose.z;
        xAngle += pose.xAngle;
        yAngle += pose.yAngle;
        zAngle += pose.zAngle;
    }

}
