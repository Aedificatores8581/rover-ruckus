package org.firstinspires.ftc.teamcode.robotOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.robotTemplates.WestCoast15;
import org.firstinspires.ftc.teamcode.robotTemplates.TankDT;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;

/**
 * Created by Frank Portman on 5/25/2018
 */
@TeleOp(name = "WCD15 Test Drive", group = "Test Drive")
public class WestCoast15TestDrive extends WestCoast15 {
    boolean switchMode = false, canSwitch = false;
    double rt, lt;
    @Override
    public void init(){
        super.init();
        controlState = TankDT.ControlState.ARCADE;
        direction = TankDT.Direction.FOR;
        activateGamepad1();
        activateGamepad2();
        setSpeed(0.5);
    }
    @Override
    public void start(){super.start();
    }
    @Override
    public void loop(){
        rt = gamepad1.right_trigger;
        super.loop();
        switch(controlState){
            case ARCADE:
                if(switchMode){
                    controlState = ControlState.FIELD_CENTRIC;
                    switchMode = false;
                    canSwitch = false;
                    directionMult = 1;
                }
                else if(rt < UniversalConstants.Triggered.TRIGGER){
                    switchMode = false;
                    canSwitch = true;
                }
                else if(rt > UniversalConstants.Triggered.TRIGGER && canSwitch)
                    switchMode = true;
                break;
            case FIELD_CENTRIC:
                lt = gamepad1.left_trigger;
                if(switchMode){
                    controlState = ControlState.TANK;
                    switchMode = false;
                    canSwitch = false;
                }
                else if(rt < UniversalConstants.Triggered.TRIGGER){
                    switchMode = false;
                    canSwitch = true;
                }
                else if(rt > UniversalConstants.Triggered.TRIGGER && canSwitch)
                    switchMode = true;
                break;
            case TANK:
                if(switchMode){
                    controlState = ControlState.ARCADE;
                    switchMode = false;
                    canSwitch = false;
                    directionMult = 1;
                }
                else if(rt < UniversalConstants.Triggered.TRIGGER){
                    switchMode = false;
                    canSwitch = true;
                }
                else if(rt > UniversalConstants.Triggered.TRIGGER && canSwitch)
                    switchMode = true;
                break;
        }
    }
}