package org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Universal.UniversalConfig;

public class MineralContainer {
    private Servo front, back;
    public final double FRONT_DOWN_POSITION = 0,
                        FRONT_UP_POSITION = 1,
                        BACK_OPEN_POSITION = 1,
                        BACK_CLOSED_POSITION = 0;

    public void init(HardwareMap hardwareMap){
        front = hardwareMap.servo.get(UniversalConfig.MINERAL_CONTAINER_FRONT_SERVO);
        back = hardwareMap.servo.get(UniversalConfig.MINERAL_CONTAINER_BACK_SERVO);
        front.setPosition(FRONT_DOWN_POSITION);
        back.setPosition(BACK_CLOSED_POSITION);
    }

    public void articulateFront(double pos) {
        front.setPosition(pos);
    }

    public double getFrontPos() {
        return front.getPosition();
    }

    public void articulateBack(double pos) {
        front.setPosition(pos);
    }

    public double getBackPos() {
        return back.getPosition();
    }
}
