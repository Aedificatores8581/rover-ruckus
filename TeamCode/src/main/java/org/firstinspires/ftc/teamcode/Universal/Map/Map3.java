package org.firstinspires.ftc.teamcode.Universal.Map;

import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose3;

import java.util.ArrayList;

public class Map3 {
    public Pose3 location;
    public ArrayList<Map3.Component> components = new ArrayList<Map3.Component>();
    public Map3(){
        location = new Pose3();
    }
    public Map3(double x, double y){
        location = new Pose3(x, y, 0, 0, 0, 0);
    }
    public Map3(double x, double y, double angle){
        location = new Pose3(x, y, 0, 0, 0, angle);
    }
    public Map3(Pose3 pose){
        location = pose;
    }
    public void setStartingPose(Pose3 pose){
        location = pose;
        updatePositions(location);
    }
    public void updatePositions(Pose3 centerPose){
        for(int i = 0; i < components.size(); i++)
            components.get(i).position.add(centerPose);
    }
    public class Component{
        public Pose3 startingPosition;
        public Pose3 position;
        public Component(Pose3 position){
            this.startingPosition = position;
            this.position = position;
        }
    }

    public double x(){
        return location.x;
    }
    public double y(){
        return location.y;
    }
    public double xAngle(){
        return location.xAngle;
    }
    public double yAngle(){
        return location.yAngle;
    }
    public double zAngle(){
        return location.zAngle;
    }
    public void add(Map3.Component component){
        components.add(component);
    }
    /*public Map2 switchReferenceMap(Map2 map){
        Map2 output = new Map2(map.x() - x(), map.y() - y(), map.angle() - angle());
        for(Map2.Component c : components)
            output.add(c);
        return output;
    }*/
}
