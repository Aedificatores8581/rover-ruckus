package org.firstinspires.ftc.teamcode.robotTemplates;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;


public abstract class SensorBot extends Robot {
    DcMotor lm, rm;
    Servo phoneServo1, phoneServo2;
    double  ps1InitPos = 0,
            ps2InitPos = 0;
    WestCoastDT drivetrain = new WestCoastDT(0.05) {
        @Override
        public void setLeftPow(double pow) {

        }

        @Override
        public void setRightPow(double pow) {

        }

        @Override
        public String[] names() {
            String[] names = {"lm", "rm"};
            return names;
        }

        @Override
        public DcMotor[] motors() {
            DcMotor[] motors = {lm, rm};
            return motors;
        }

        @Override
        public DcMotor.Direction[] dir() {
            DcMotor.Direction[] dir = {FORWARD, REVERSE};
            return dir;
        }

        @Override
        public void loop() {

        }
    };
    public void init(){
        super.init();
        phoneServo1 = hardwareMap.servo.get("ps1");
        phoneServo2 = hardwareMap.servo.get("ps2");
        phoneServo1.setPosition(ps1InitPos);
        phoneServo2.setPosition(ps2InitPos);
    }
    public void start(){
        super.start();
    }
    @Override
    public void loop(){

    }

}