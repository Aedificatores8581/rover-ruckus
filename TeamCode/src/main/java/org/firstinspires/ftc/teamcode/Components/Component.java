package org.firstinspires.ftc.teamcode.Components;

import org.firstinspires.ftc.teamcode.Universal.Map.Map2;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose3;

public class Component {
    public Pose3 relativeLocation;
    public Pose3 position;

    public Component(){
        relativeLocation = new Pose3();
        position = relativeLocation;
    }
    public Component(Pose3 startingPosition){
        relativeLocation = startingPosition;
        position = relativeLocation;
    }
}
