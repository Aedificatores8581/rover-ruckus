package org.firstinspires.ftc.teamcode.robotTemplates;


import com.qualcomm.robotcore.hardware.DcMotor;

public abstract class WestCoastDT extends Drivetrain {

    public WestCoastDT(String[] names, DcMotor[] m, DcMotor.Direction[] dir){
        super(names, m, dir);
    }
    public void init(){
        super.init();
    }
    public abstract void setLeftPow();
    public abstract void setRightPow();

}
