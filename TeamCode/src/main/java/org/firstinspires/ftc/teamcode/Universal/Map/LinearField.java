package org.firstinspires.ftc.teamcode.Universal.Map;

import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

public class LinearField implements ComponentLimitingField{
    public Pose offset;
    public LinearField(Pose position){
        offset = position;
    }

    public void interact(Vector2 vector2, Pose pose){
        Pose tempPose = pose.clone();
        tempPose.x -= offset.x;
        tempPose.y -= offset.y;
        if(UniversalFunctions.normalizeAngleRadians(tempPose.toVector().angle(), offset.angle) > Math.PI)
            if(UniversalFunctions.normalizeAngleRadians(vector2.angle(), offset.angle) < Math.PI){
                vector2.rotate(-offset.angle);
                vector2.y = 0;
                vector2.rotate(offset.angle);

            }
    }
}
