package org.firstinspires.ftc.teamcode.Components;

import org.firstinspires.ftc.teamcode.Universal.Map.Map2;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose3;

public class Component {
    public Pose3 relativePosition;
    public Pose3 absolutePosition;

    public Component(){
        relativePosition = new Pose3();
        absolutePosition = relativePosition;
    }
    public Component(Pose3 startingPosition){
        relativePosition = startingPosition;
        absolutePosition = relativePosition;
    }
}
