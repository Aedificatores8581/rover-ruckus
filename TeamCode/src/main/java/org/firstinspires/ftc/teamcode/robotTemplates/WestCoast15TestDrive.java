package org.firstinspires.ftc.teamcode.robotTemplates;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "WCD15 Test Drive", group = "Test Drive")
public class WestCoast15TestDrive extends OpMode {
    WestCoast15 WCD15 = new WestCoast15(0.001, 0.5);
    boolean switchMode = false, canSwitch = false;
    double rt;
    public void init(){
        WCD15.init();
        WCD15.cs = WestCoastDT.ControlState.ARCADE;
        WCD15.direction = WestCoastDT.Direction.FOR;
        WCD15.activateGamepad1();
        WCD15.activateGamepad2();
    }

    public void start(){
        WCD15.start();
    }

    @Override
    public void loop(){
        rt = gamepad1.right_trigger;
        WCD15.loop();
        switch(WCD15.cs){
            case ARCADE:
                if(switchMode){
                    WCD15.cs = WCD15.cs.FIELD_CENTRIC;
                    switchMode = false;
                    canSwitch = false;
                    WCD15.mult = 1;
                }
                else if(rt < 0.2){
                    switchMode = false;
                    canSwitch = true;
                }
                else if(rt > 0.2 && canSwitch)
                    switchMode = true;
                break;
            case FIELD_CENTRIC:
                if(switchMode){
                    WCD15.cs = WCD15.cs.TANK;
                    switchMode = false;
                    canSwitch = false;
                    WCD15.mult = 1;
                }
                else if(rt < 0.2){
                    switchMode = false;
                    canSwitch = true;
                }
                else if(rt > 0.2 && canSwitch)
                    switchMode = true;
                break;

            case TANK:
                if(switchMode){
                    WCD15.cs = WCD15.cs.ARCADE;
                    switchMode = false;
                    canSwitch = false;
                    WCD15.mult = 1;
                }
                else if(rt < 0.2){
                    switchMode = false;
                    canSwitch = true;
                }
                else if(rt > 0.2 && canSwitch)
                    switchMode = true;
                break;
        }
    }
}