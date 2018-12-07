package org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class MineralContainer {
    public Servo articulator, cage;
    public final double ARTICULATOR_DOWN_POSITION = 0,
                        ARTICULATOR_UP_POSITION = 1,
                        CAGE_OPEN_POSITION = 1,
                        CAGE_CLOSED_POSITION = 0;
    public void init(HardwareMap hardwareMap){
        articulator = hardwareMap.servo.get("cart");
        cage = hardwareMap.servo.get("cage");
        articulator.setPosition(ARTICULATOR_DOWN_POSITION);
        cage.setPosition(CAGE_CLOSED_POSITION);
    }
    public void openCage(){
        if(articulator.getPosition() == ARTICULATOR_DOWN_POSITION)
            cage.setPosition(CAGE_CLOSED_POSITION);
    }
    public void closeCage(){
        cage.setPosition(CAGE_CLOSED_POSITION);
    }
    public void articulateUp(){
        articulator.setPosition(ARTICULATOR_UP_POSITION);
    }
    public void articulateDown() {
        articulator.setPosition(ARTICULATOR_DOWN_POSITION);
    }
}
