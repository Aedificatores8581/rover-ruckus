package org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Lift;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Components.Sensors.MotorEncoder;
import org.firstinspires.ftc.teamcode.Components.Sensors.TouchSensor;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;


public class AExtendotm {
    public DcMotor extendo;
    public Servo marker, leftArticulator, rightArticulator;
    public TouchSensor backSwitch = new TouchSensor(), frontSwitch = new TouchSensor();
    private boolean automationAllowed = true;
    public boolean isAutonomous = false;
    public double maxSpeed = 1.0;
    public MotorEncoder encoder;
    private AExtendoState AExtendoState;
    private Intake intake = new Intake();
    double waitcheck, startwait;
    public final double MAX_EXTENSION_LENGTH = 29.34,
            MIN_EXTENSION_LENGTH = 0,
            GEAR_RATIO = 7.5,
            TICKS_PER_REVOLUTION = 7,
            INCH_PER_TICK = /*(70*(2+124.6/(276+1.0/3))/Math.PI)/(GEAR_RATIO*TICKS_PER_REVOLUTION)*/25.4;


    public void init(HardwareMap hardwareMap, boolean isAutonomous) {
        extendo = hardwareMap.dcMotor.get("aetm");
        backSwitch.init(hardwareMap, "HESb");
        frontSwitch.init(hardwareMap, "HESf");
        extendo.setDirection(DcMotorSimple.Direction.REVERSE);
        extendo.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extendo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        extendo.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        encoder = new MotorEncoder(extendo);
        encoder.initEncoder();
        this.isAutonomous = isAutonomous;
        AExtendoState = AExtendoState.DRIVER;
    }
    //TODO:add limit switch code
    public void aextendTM(double value) {
            if (backSwitch.isPressed())
                value = UniversalFunctions.clamp(0, value, 1);
            if (frontSwitch.isPressed())
                value = UniversalFunctions.clamp(-1, value, 0);
            extendo.setPower(value);
    }
    public synchronized void automatedTransfer() {
        if(automationAllowed) {
            switch (AExtendoState) {
                case DRIVER:
                    break;
                case START_TRANSFER:
                    if(backSwitch.isPressed()) AExtendoState = AExtendoState.LIFTING_INTAKE;
                    else AExtendoState = AExtendoState.RETRACTING;
                    break;
                case RETRACTING:
                    if(backSwitch.isPressed()) {
                        aextendTM(0);
                        AExtendoState = AExtendoState.LIFTING_INTAKE;
                    }
                    else aextendTM(-1);
                    break;
                case LIFTING_INTAKE:
                    //intek.articulateUp();
                    startwait = UniversalFunctions.getTimeInSeconds();
                    AExtendoState = AExtendoState.LIFTING_WAIT;
                    break;
                case LIFTING_WAIT:
                    waitcheck = UniversalFunctions.getTimeInSeconds();
                    if (waitcheck - startwait >= 1) AExtendoState = AExtendoState.OPENING_DISPENSOR;
                    break;
                case OPENING_DISPENSOR:
                    //intake.openDispensor();
                    startwait = UniversalFunctions.getTimeInSeconds();
                    AExtendoState = AExtendoState.OPEN_WAIT;
                    break;
                case OPEN_WAIT:
                    waitcheck = UniversalFunctions.getTimeInSeconds();
                    if (waitcheck - startwait >= .5) AExtendoState = AExtendoState.CLOSE_DISPENSOR;
                    break;
                case OPENING_DISPENSOR2:
                    //intake.openDispensor();
                    startwait = UniversalFunctions.getTimeInSeconds();
                    AExtendoState = AExtendoState.OPEN_WAIT2;
                    break;
                case OPEN_WAIT2:
                    waitcheck = UniversalFunctions.getTimeInSeconds();
                    if (waitcheck - startwait >= .5) AExtendoState = AExtendoState.LOWER_INTAKE;
                    break;
                case CLOSE_DISPENSOR:
                    //intake.closeDispensor();
                    startwait = UniversalFunctions.getTimeInSeconds();
                    AExtendoState = AExtendoState.CLOSE_WAIT;
                    break;
                case CLOSE_WAIT:
                    waitcheck = UniversalFunctions.getTimeInSeconds();
                    if (waitcheck - startwait >= .5) AExtendoState = AExtendoState.OPENING_DISPENSOR2;
                    break;
                case CLOSE_DISPENSOR2:
                    //intake.closeDispensor();
                    startwait = UniversalFunctions.getTimeInSeconds();
                    AExtendoState = AExtendoState.CLOSE_WAIT2;
                    break;
                case CLOSE_WAIT2:
                    waitcheck = UniversalFunctions.getTimeInSeconds();
                    if (waitcheck - startwait >= .5) AExtendoState = AExtendoState.DONE_TRANSFER;
                    break;
                case LOWER_INTAKE:
                    //intake.articulateDown();
                    startwait = UniversalFunctions.getTimeInSeconds();
                    AExtendoState = AExtendoState.LOWER_WAIT;
                    break;
                case LOWER_WAIT:
                    waitcheck = UniversalFunctions.getTimeInSeconds();
                    if (waitcheck - startwait >= 1) AExtendoState = AExtendoState.CLOSE_DISPENSOR2;
                    break;
                case DONE_TRANSFER:
                    AExtendoState = AExtendoState.DRIVER;
                    break;
            }
        }
    }

    public AExtendoState setAextendotmState(AExtendoState value) {
        AExtendoState = value;
        return AExtendoState;
    }
    public AExtendoState getAExtendoState() {
        return AExtendoState;
    }

    public void allowAutomation(boolean val) {
        automationAllowed = val;
    }

    public boolean isAutomationAllowed() {
        return automationAllowed;
    }
    public double getExtensionLength() {
        encoder.updateEncoder();
        return INCH_PER_TICK * encoder.currentPosition;
    }

}


    /* Unused Code in re-write to automate Extension

/*
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

    public String toString(){
        return getExtensionLength() + " inches extended, articulator is " + articulationState;
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
    enum ArticulationState{
        LOWERED,
        RAISED,
        RETRACTED
    public final double EXTENSION_OFFSET = 0, MARKER_OFFSET = 0;
    }
        public ArticulationState articulationState;
       */