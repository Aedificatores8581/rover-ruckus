package org.firstinspires.ftc.teamcode.robotTemplates;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.robotUniversal.GyroAngles;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;

import java.util.ResourceBundle;

public class CrabDT extends Drivetrain {
    DcMotor rf, lf, la, ra, cm;
    Servo clutch;
    double speed, dir = 1, desiredPos;
    int encPos;
    final double encRatio;
    TurnMode tm;
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
    };
    //SwerveDT.TurnDir td;
    public CrabDT(double encRat, double brake, double sped){
        super(brake);
        encRatio = encRat;
        speed = sped;
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
    //sets motor power within the specified parameters
    protected void refreshMotors(double I, double II, double III, double IV){
        lf.setPower(speed * II);
        la.setPower(speed * III);
        rf.setPower(speed * I);
        ra.setPower(speed * IV);
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
    public enum TurnMode{
        ARCADE,
        FIELD_CENTRIC;
    }
    public void init(){

        TankDrive.controlState = TankDT.ControlState.ARCADE;
        TankDrive.direction = TankDT.Direction.FOR;
        activateGamepad1();
        activateGamepad2();
        TankDrive.setSpeed(1);
    }
    public void loop(){
        updateGamepad1();
        switch(dm) {
            case SWERVE:
                angles = imu.getAngularOrientation(AxesReference.INTRINSIC, GyroAngles.ORDER, GyroAngles.UNIT);
                angleOfRotation = normalizeGyroAngle();
                setRobotAngle();
                turnMult = 1 - leftStick1.y * (1 - minTurn);
                switch (tm) {
                    case FIELD_CENTRIC:
                        turnMult *= Math.sin(Math.toRadians(rightStick1.angleBetween(robotAngle)));

                        break;
                    case ARCADE:
                        turnMult *= rightStick1.x;
                }
                I = -Math.cos(Math.toRadians(angleOfRotation + ITurnAngle)) * turnMult;
                II = -Math.cos(Math.toRadians(angleOfRotation + 90 + IITurnAngle)) * turnMult;
                III = -Math.cos(Math.toRadians(angleOfRotation + 180 + IIITurnAngle)) * turnMult;
                IV = -Math.cos(Math.toRadians(angleOfRotation + 270 + IVTurnAngle)) * turnMult;
                if (normalized) {
                    maxPow = rightStick1.x / UniversalFunctions.maxAbs(I, II, III, IV);
                    I *= maxPow;
                    II *= maxPow;
                    III *= maxPow;
                    IV *= maxPow;
                }
                I += Math.sqrt(leftStick1.x * leftStick1.x + leftStick1.y * leftStick1.y) * directionMult;
                II += Math.sqrt(leftStick1.x * leftStick1.x + leftStick1.y * leftStick1.y) * directionMult;
                III += Math.sqrt(leftStick1.x * leftStick1.x + leftStick1.y * leftStick1.y) * directionMult;
                IV += Math.sqrt(leftStick1.x * leftStick1.x + leftStick1.y * leftStick1.y) * directionMult;
                maxPow = UniversalFunctions.maxAbs(I, II, III, IV);
                if (maxPow > 1) {
                    I /= maxPow;
                    II /= maxPow;
                    III /= maxPow;
                    IV /= maxPow;
                }
                if (Math.abs(gamepad1.left_stick_y) >= UniversalConstants.Triggered.STICK || Math.abs(gamepad1.left_stick_x) >= UniversalConstants.Triggered.STICK)
                    desiredAngle = normalizeGamepadAngleL(cmAngle);
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
                //encPos = getEncoderRotation(cmAngle);
                encPos += (int) getEncoderRotation(cmAngle);
                //cm.setPower(Math.sin(Math.toRadians(encPos)));
                cm.setTargetPosition(encPos);
                refreshMotors(I, II, III, IV);
                cmAngle = UniversalFunctions.normalizeAngle(getMotorAngle(encPos));
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
        TANK;
    }
    //switches from the act of switching DriveModes
    public enum TankDriveMode{
        SHIFT,
        TANK;
    }
}
