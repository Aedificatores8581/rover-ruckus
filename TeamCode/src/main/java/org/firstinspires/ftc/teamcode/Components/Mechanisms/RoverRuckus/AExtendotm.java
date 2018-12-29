package org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose3;

public class AExtendotm {
    public DcMotor extendo;
    public Servo marker, leftArticulator, rightArticulator;
    public ArticulationState articulationState;

    public final double EXTENSION_OFFSET = 0, MARKER_OFFSET = 0;
    public double maxSpeed = 1;
    //TODO: find these values
    public boolean isAutonomous = false;
    private final double MARKER_CLOSED_POSITION = 0,
                        MARKER_OPEN_POSITION = 1,
                        MAX_EXTENSION_LENGTH = 36,
                        ENC_PER_RADIAN = 10,
                        TICKS_PER_INCH = 210/Math.PI*25.4,
                        LEFT_ARTICULATOR_UPRIGHT_POSITION = 0,
                        RIGHT_ARTICULATOR_UPRIGHT_POSITION = 0,
                        ARTICULATOR_LENGTH = 0;

    public void init(HardwareMap hardwareMap, boolean isAutonomous) {
        leftArticulator = hardwareMap.servo.get("lart");
        rightArticulator = hardwareMap.servo.get("rart");
        extendo = hardwareMap.dcMotor.get("aetm");
        marker = hardwareMap.servo.get("mrkr");

        marker.setPosition(MARKER_CLOSED_POSITION);
        extendo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendo.setMode(isAutonomous ? DcMotor.RunMode.RUN_TO_POSITION : DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public int angleToEnc(double ang){
        return (int) (ang * ENC_PER_RADIAN);
    }
    public void dropMarker(){
        marker.setPosition(MARKER_OPEN_POSITION);
    }
    public void aextendTM(double value) {
        if(getExtensionLength() > 0 && getExtensionLength() < MAX_EXTENSION_LENGTH) {
            if (extendo.getMode().equals(DcMotor.RunMode.RUN_WITHOUT_ENCODER)) {
                extendo.setPower(maxSpeed * value);
            } else {
                extendo.setTargetPosition((int) (value * TICKS_PER_INCH));
            }
        } else {
            extendo.setPower(0);
        }
    }

    public double getTotalExtensionLength() {
        return TICKS_PER_INCH * extendo.getCurrentPosition() + ARTICULATOR_LENGTH * Math.sin(getArticulatorAngle());
    }

    public double getArticulatorLength() {
        return ARTICULATOR_LENGTH * Math.sin(getArticulatorAngle());
    }

    public double getExtensionLength() {
        return TICKS_PER_INCH * extendo.getCurrentPosition();
    }

    public double getDesiredExtensionLength() {
        return TICKS_PER_INCH * extendo.getTargetPosition();
    }

    public boolean isRetracted() {
        return getExtensionLength() < 5;
    }

    public boolean willBeRetracted() {
        return getDesiredExtensionLength() < 5;
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
    private double getArticulatorAngle(){
        double output = 0;
        switch (articulationState){
            case RAISED:
                output = 0;
                break;
            case LOWERED:
                output = 0;
                break;
        }

        return output;
    }

    public Pose getExtensionLocation(Pose location) {
        return new Pose(location.x + getExtensionLength() * Math.cos(location.angle), location.y + EXTENSION_OFFSET + getExtensionLength() * Math.sin(location.angle), location.angle);
    }

    public Pose getMarkerLocation(Pose location) {
        return new Pose(location.x + getExtensionLength() * Math.cos(location.angle), location.y + MARKER_OFFSET + getExtensionLength() * Math.sin(location.angle), location.angle);
    }

    enum ArticulationState{
        LOWERED,
        RAISED,
        RETRACTED
    }

    public String toString(){
        String markerTerm  = marker.getPosition() == MARKER_OPEN_POSITION ? "marker is deployed" : "marker is not deployed";
        return getExtensionLength() + " inches extended, articulator is " + articulationState + ", " + markerTerm;
    }
}
