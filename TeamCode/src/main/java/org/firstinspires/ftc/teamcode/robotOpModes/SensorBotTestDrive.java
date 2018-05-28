package org.firstinspires.ftc.teamcode.robotOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotTemplates.SensorBot;
import org.firstinspires.ftc.teamcode.robotTemplates.SensorBotWestTemplate;
import org.firstinspires.ftc.teamcode.robotTemplates.WestBotTemplate;
import org.firstinspires.ftc.teamcode.robotTemplates.WestCoastDT;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;

@TeleOp(name = "SensorBot test drive", group = "Test Drive")
public class SensorBotTestDrive extends SensorBot {
    double rt, lt;
    boolean switchMode = false, canSwitch = false, switchTurnState = false, switchTSBool = false;
    public void init(){
        super.init();
        drivetrain.cs = WestCoastDT.ControlState.ARCADE;
        drivetrain.direction = WestCoastDT.Direction.FOR;
        activateGamepad1();
        activateGamepad2();
        drivetrain.setSpeed(1.0);
        drivetrain.ts = WestCoastDT.FCTurnState.SMOOTH;
    }
    public void start(){
        super.start();
    }
    public void loop(){
        rt = gamepad1.right_trigger;
        super.loop();
        switch(drivetrain.cs) {
            case ARCADE:
                if (switchMode) {
                    drivetrain.cs = WestCoastDT.ControlState.FIELD_CENTRIC;
                    switchMode = false;
                    canSwitch = false;
                    drivetrain.directionMult = 1;
                } else if (rt < UniversalConstants.Triggered.TRIGGER) {
                    switchMode = false;
                    canSwitch = true;
                } else if (rt > UniversalConstants.Triggered.TRIGGER && canSwitch)
                    switchMode = true;
                break;
            case FIELD_CENTRIC:
                lt = gamepad1.left_trigger;
                switch (drivetrain.ts) {
                    case FAST:
                        if (lt > UniversalConstants.Triggered.TRIGGER) {
                            drivetrain.ts = WestCoastDT.FCTurnState.SMOOTH;
                            switchTurnState = false;
                            switchTSBool = false;
                        } else if (lt < UniversalConstants.Triggered.TRIGGER) {
                            switchTurnState = false;
                            switchTSBool = true;
                        } else if (lt > UniversalConstants.Triggered.TRIGGER && switchTSBool)
                            switchTurnState = true;
                        break;
                    case SMOOTH:
                        if (lt > UniversalConstants.Triggered.TRIGGER) {
                            drivetrain.ts = WestCoastDT.FCTurnState.FAST;
                            switchTurnState = false;
                            switchTSBool = false;
                        } else if (lt < 0.2) {
                            switchMode = false;
                            switchTSBool = true;
                        } else if (lt > 0.2 && switchTSBool)
                            switchTurnState = true;
                        break;

                }
                if (switchMode) {
                    drivetrain.cs = WestCoastDT.ControlState.TANK;
                    switchMode = false;
                    canSwitch = false;
                } else if (rt < UniversalConstants.Triggered.TRIGGER) {
                    switchMode = false;
                    canSwitch = true;
                } else if (rt > UniversalConstants.Triggered.TRIGGER && canSwitch)
                    switchMode = true;
                break;

            case TANK:
                if (switchMode) {
                    drivetrain.cs = WestCoastDT.ControlState.ARCADE;
                    switchMode = false;
                    canSwitch = false;
                    drivetrain.directionMult = 1;
                } else if (rt < UniversalConstants.Triggered.TRIGGER) {
                    switchMode = false;
                    canSwitch = true;
                } else if (rt > UniversalConstants.Triggered.TRIGGER && canSwitch)
                    switchMode = true;
                break;
        }
    }
}
