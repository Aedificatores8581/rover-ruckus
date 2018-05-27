package org.firstinspires.ftc.teamcode.robotOpModes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robotTemplates.Mecanum2;
import org.firstinspires.ftc.teamcode.robotTemplates.MecanumDT;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;


/**
 * Created by Frank Portman on 5/27/2018
 */
@TeleOp(name = "Mec2 Test Drive", group = "Test Drive")
public class Mecanum2TestDrive extends OpMode {

    Mecanum2 mec2 = new Mecanum2(0.01, 0.75);
    boolean switchMode = false, canSwitch = false;
    double rt;

    public void init(){
        mec2.init();
        mec2.cs = MecanumDT.ControlState.ARCADE;
        mec2.activateGamepad1();
    }

    public void start(){
        mec2.start();
    }

    @Override
    public void loop(){
        mec2.loop();
        rt = gamepad1.right_trigger;
        switch(mec2.cs){
            case ARCADE:
                if(switchMode){
                    mec2.cs = MecanumDT.ControlState.FIELD_CENTRIC;
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
            case FIELD_CENTRIC:
                if(switchMode){
                    mec2.cs = MecanumDT.ControlState.ARCADE;
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
        }
    }
}
