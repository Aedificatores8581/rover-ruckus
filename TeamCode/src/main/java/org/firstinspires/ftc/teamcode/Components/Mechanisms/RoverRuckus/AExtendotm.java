package org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Components.Sensors.MotorEncoder;
import org.firstinspires.ftc.teamcode.Components.Sensors.TouchSensor;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose3;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;

public class AExtendotm {
    public DcMotor extendo;
    public Servo marker, leftArticulator, rightArticulator;
    public ArticulationState articulationState;
    public TouchSensor backSwitch = new TouchSensor(), frontSwitch = new TouchSensor();
    public final double EXTENSION_OFFSET = 0, MARKER_OFFSET = 0;
    public double maxSpeed = 1.0;
    public MotorEncoder encoder;
    //TODO: find these values
    public boolean isAutonomous = false;
    public final double MAX_EXTENSION_LENGTH = 29.34,
                        MIN_EXTENSION_LENGTH = 0,
                        GEAR_RATIO = 7.5,
                        TICKS_PER_REVOLUTION = 7,
                        TICKS_PER_INCH = (70*(2+124.6/(276+1.0/3))/Math.PI)/(GEAR_RATIO*TICKS_PER_REVOLUTION)/25.4;

    public void init(HardwareMap hardwareMap, boolean isAutonomous) {
        extendo = hardwareMap.dcMotor.get("aetm");
        backSwitch.init(hardwareMap, "HESb");
        frontSwitch.init(hardwareMap, "HESf");
        extendo.setDirection(DcMotorSimple.Direction.FORWARD);
        extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        extendo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        encoder = new MotorEncoder(extendo);
        encoder.initEncoder();
        this.isAutonomous = isAutonomous;
    }
    //TODO:add limit switch code
    public void aextendTM(double value) {
        encoder.updateEncoder();
        if(isAutonomous && UniversalFunctions.withinTolerance(value - 0.25, getExtensionLength(), value + 0.25))
            extendo.setPower(maxSpeed);
        else if(isAutonomous)
            extendo.setPower(getExtensionLength() > value ? - maxSpeed : maxSpeed);
        else
            extendo.setPower(value);
        if(getExtensionLength() < 0)
            encoder.resetEncoder();
    }

    public double getExtensionLength() {
        encoder.updateEncoder();
        return TICKS_PER_INCH * encoder.currentPosition;
    }

    public double getDesiredExtensionLength() {
        return TICKS_PER_INCH * extendo.getTargetPosition();
    }

    public boolean isRetracted() {
        return this.encoder.currentPosition < 20;
    }

    public boolean willBeRetracted() {
        return getDesiredExtensionLength() < 1.0;
    }

    public void articulateDown() {
        articulationState = ArticulationState.LOWERED;
        setArticulatorPosition();
    }

    public void articulateUp() {
        switch (articulationState){
            case LOWERED:
                if (willBeRetracted() || isRetracted()) {
                    articulationState = ArticulationState.RETRACTED;
                } else {
                    articulationState = ArticulationState.RAISED;
                }
                break;
        }
        setArticulatorPosition();
    }

    private void setArticulatorPosition(){
        switch (articulationState){
            case RAISED:
                leftArticulator.setPosition(0);
                rightArticulator.setPosition(0);
                break;
            case LOWERED:
                leftArticulator.setPosition(0);
                rightArticulator.setPosition(0);
                break;
            case RETRACTED:
                leftArticulator.setPosition(0);
                rightArticulator.setPosition(0);
                break;
        }
    }

        enum ArticulationState{
        LOWERED,
        RAISED,
        RETRACTED
    }

    public String toString(){
        return getExtensionLength() + " inches extended, articulator is " + articulationState;
    }
}
