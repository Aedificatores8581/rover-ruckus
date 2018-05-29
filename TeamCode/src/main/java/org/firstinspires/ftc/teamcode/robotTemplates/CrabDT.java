package org.firstinspires.ftc.teamcode.robotTemplates;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.robotUniversal.GyroAngles;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;
import org.firstinspires.ftc.teamcode.robotUniversal.Vector2;

import java.util.ResourceBundle;

public class CrabDT extends HolonomicDT {
    DcMotor rf, lf, la, ra, cm;
    public double drivePow, rfPow, lfPow, raPow, laPow;
    Servo clutch;
    double desiredPos;
    int encPos;
    final double encRatio;
    DriveMode dm;
    double tankPos;
    double cmAngle, desiredAngle;
    double angleOfRotation, turnMult;
    double I, II, III, IV;
    double directionMult;
    public double ITurnAngle, IITurnAngle, IIITurnAngle, IVTurnAngle;
    double angleThreshhold;
    boolean normalized;
    double maxPow;
    WheelRotationMode wheelRotationMode;
    boolean wheelModeSet = false;
    Drivetrain.Direction td;
    boolean turn;
    TankDriveMode tdm;
    TankDT TankDrive = new TankDT(brakePow) {
        public void setLeftPow(double pow){
            setPower(lf, pow);
            setPower(la, pow);
            leftPow = pow;
        }
        public void setRightPow(double pow){
            setPower(rf, pow);
            setPower(ra, pow);
            rightPow = pow;
        }
        public void initMotors(){

        }
        public void normalizeMotors(){
            maxPow = UniversalFunctions.maxAbs(rightPow, leftPow);
            rightPow /= maxPow;
            leftPow /= maxPow;
        }
    };
    //SwerveDT.TurnDir td;
    public CrabDT(double encRat, double brake, double sped){
        super(brake);
        encRatio = encRat;
        maxSpeed = sped;
        clutch = hardwareMap.servo.get("bs");
    }
    public void initMotors(){
        rf = hardwareMap.dcMotor.get("rf");
        lf = hardwareMap.dcMotor.get("lf");
        la = hardwareMap.dcMotor.get("la");
        ra = hardwareMap.dcMotor.get("ra");
        rf.setDirection(FORWARD);
        lf.setDirection(REVERSE);
        la.setDirection(REVERSE);
        ra.setDirection(FORWARD);
    }
    public void refreshMotors(){
        rf.setPower(drivePow - Math.cos(Math.toRadians(angleOfRotation + ITurnAngle)) * turnPow);
        lf.setPower(drivePow - Math.cos(Math.toRadians(angleOfRotation + 90 + IITurnAngle)) * turnPow);
        la.setPower(drivePow - Math.cos(Math.toRadians(angleOfRotation + 180 + IIITurnAngle)) * turnPow);
        ra.setPower(drivePow - Math.cos(Math.toRadians(angleOfRotation + 270 + IIITurnAngle)) * turnPow);
    }
    public void refreshMotors(double I, double II, double III, double IV){
        setPower(rf, I);
        setPower(lf, II);
        setPower(la, III);
        setPower(ra, IV);
    }
    @Override
    //Gives the motors holding power
    public void brake(){
        if(rf.getPower() == 0 && lf.getPower() == 0 && la.getPower() == 0 && ra.getPower() == 0){
            rf.setPower(-brakePow);
            ra.setPower(brakePow);
            la.setPower(-brakePow);
            lf.setPower(brakePow);
        }
    }
    //specifies the turn multiplier
    public void init(){
        encPos = cm.getCurrentPosition();
        /*
        TankDrive.controlState = TankDT.ControlState.ARCADE;
        TankDrive.direction = TankDT.Direction.FOR;
        activateGamepad1();
        activateGamepad2();
        TankDrive.setSpeed(1);*/
    }
    public void loop(){
        updateGamepad1();
        switch(dm) {
            case SWERVE:
                angleOfRotation = normalizeGyroAngle();
                setRobotAngle();
                switchTurnState();
                setVelocity(leftStick1);
                refreshMotors();
                if (gamepad1.left_stick_button && gamepad1.right_stick_button) {
                    dm = DriveMode.TANK;
                    tdm = TankDriveMode.SHIFT;
                }
                break;
            case TANK:
                switch (tdm) {
                    case SHIFT:
                        cmAngle = UniversalFunctions.normalizeAngle(getMotorAngle(encPos));
                        desiredAngle = UniversalFunctions.normalizeAngle(0, angleOfRotation);
                        desiredPos = getEncoderRotation(desiredAngle, cmAngle);
                        encPos = (int) getEncoderRotation(cmAngle);
                        cm.setPower(Math.sin(Math.toRadians(encPos)));
                        if (Math.abs(desiredAngle) < angleThreshhold) {
                            clutch.setPosition(tankPos);
                            tdm = TankDriveMode.TANK;
                        }
                        break;
                    case TANK:
                        TankDrive.loop();
                }
        }
    }
    //returns the number of encoder ticks coresponding to specified angle
    public double getEncoderRotation(double angle){
        return angle / 360 * encRatio;
    }
    //returns the angle of rotation coresponding to a specified number of encoder ticks
    public double getMotorAngle(double enc){
        return UniversalFunctions.normalizeAngle(enc * 360 / encRatio);
    }
    //normalizes getMotorAngle from -180 to 180 degrees
    public double getMotorAngle180(double enc){
        return UniversalFunctions.normalizeAngle180(enc * 360 / encRatio);
    }
    //returns the number of encoder ticks coresponding to specified change in angle
    public double getEncoderRotation(double desiredAngle, double currentAngle) {
        return UniversalFunctions.normalizeAngle180(desiredAngle, currentAngle) / 360 * encRatio;
    }

    public void setVelocity(double ang, double speed){
        cmAngle = UniversalFunctions.normalizeAngle(getMotorAngle(encPos));
        if (Math.abs(gamepad1.left_stick_y) >= UniversalConstants.Triggered.STICK || Math.abs(gamepad1.left_stick_x) >= UniversalConstants.Triggered.STICK)
            desiredAngle = UniversalFunctions.normalizeAngle(ang, cmAngle);
        switch (td) {
            case FOR:
                if (desiredAngle > 180 && turn) {
                    td = Direction.BACK;
                    directionMult *= -1;
                    desiredAngle = UniversalFunctions.normalizeAngle(desiredAngle + 180, 0);
                    turn = false;
                } else if (desiredAngle <= 0)
                    turn = true;
                break;
            case BACK:
                if (desiredAngle < 180 && turn) {
                    td = Direction.FOR;
                    turn = false;
                    directionMult *= -1;
                } else if (desiredAngle <= 0)
                    turn = true;
                break;
        }
        desiredAngle = UniversalFunctions.normalizeAngle(desiredAngle, angleOfRotation);
        desiredPos = getEncoderRotation(desiredAngle, cmAngle);
        switch(wheelRotationMode){
            case SMOOTH:
                if(!wheelModeSet) {
                    cm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    wheelModeSet = true;
                }
                encPos = (int)getEncoderRotation(cmAngle);
                cm.setPower(Math.sin(Math.toRadians(encPos)));
                break;
            case FAST:
                if(!wheelModeSet) {
                    cm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    wheelModeSet = true;
                }
                encPos += (int) getEncoderRotation(cmAngle);
                cm.setTargetPosition(encPos);
                break;
        }
        drivePow = directionMult * speed;
    }
    public void setVelocity(Vector2 vel){
        setVelocity(vel.angle(), vel.magnitude());
    }
    /*public double setWheelVelocity(double ang, double sped){
        switch(td){
            case FORWARD:
                if(Math.sin(Math.toRadians(getEncoderRotation(ang, normalizeGyroAngle()))) < 0){
                    dir *= -1;
                    ang += 180;
                    td = TurnDir.BACKWARD;
                }
                break;
            case BACKWARD:
                if(Math.sin(Math.toRadians(getEncoderRotation(ang, normalizeGyroAngle()))) < 0){
                    dir *= -1;
                    ang += 180;
                    td = TurnDir.FORWARD;
                }
                break;
        }
        return ang;
    }*/
    //switches the control system from swerve to non-holonomic
    public enum DriveMode{
        SWERVE,
        TANK
    }
    //switches from the act of switching DriveModes
    public enum TankDriveMode{
        SHIFT,
        TANK
    }
    public enum WheelRotationMode{
        FAST,
        SMOOTH
    }
    public void normalizeMotors(){
        maxPow = UniversalFunctions.maxAbs(ra.getPower(), la.getPower(), rf.getPower(), lf.getPower());
        if (maxPow > 1) {
            refreshMotors(rf.getPower() / maxPow, lf.getPower() / maxPow, la.getPower() / maxPow, ra.getPower() / maxPow);
        }
    }
}
