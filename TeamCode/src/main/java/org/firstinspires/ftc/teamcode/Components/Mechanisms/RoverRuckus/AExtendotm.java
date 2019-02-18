package org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Components.Sensors.MotorEncoder;
import org.firstinspires.ftc.teamcode.Components.Sensors.TouchSensor;
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
                        INCH_PER_TICK = /*(70*(2+124.6/(276+1.0/3))/Math.PI)/(GEAR_RATIO*TICKS_PER_REVOLUTION)/25.4*/ 29.5/2320;

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
        if (isAutonomous && UniversalFunctions.withinTolerance(value - 0.25, getExtensionLength(), value + 0.25))
            extendo.setPower(maxSpeed);
        else if (isAutonomous)
            extendo.setPower(getExtensionLength() > value ? -maxSpeed : maxSpeed);
        else {
            if (backSwitch.isPressed())
                value = UniversalFunctions.clamp(0, value, 1);
            if (frontSwitch.isPressed())
                value = UniversalFunctions.clamp(-1, value, 0);
            extendo.setPower(value);
        }
        if (getExtensionLength() < 0)
            encoder.resetEncoder();
    }

    public double getExtensionLength() {
        encoder.updateEncoder();
        return INCH_PER_TICK * encoder.currentPosition;
    }

    public double getDesiredExtensionLength() {
        return INCH_PER_TICK * extendo.getTargetPosition();
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
