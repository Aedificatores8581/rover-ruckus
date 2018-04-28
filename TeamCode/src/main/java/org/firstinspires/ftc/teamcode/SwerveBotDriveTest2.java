package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

/**
 * Created by fgpor on 4/21/2018.
 */
/*
THIS CODE IN ITS CURRENT STATE WILL NOT WORK. WE NEED THE ENCODERS FIRST
 */
@TeleOp(name = "SwervBotTestDrive2", group = "Test_Drive")
public class SwerveBotDriveTest2 extends SwerveBotTemplate {
    double angleOfRotation, I, II, III, IV, max, desiredAngle, desiredPos, xl, yl, yr, xr, mult, cmAngle, turnMult;
    boolean normalized, turn;
    int encPos;
    DriveMode dm;
    TankDriveMode tdm;
    TurnMode tm;
    SensorBotWestTemplate.TurnDir td;
    final double
            I_TURN_ANGLE = 0,
            II_TURN_ANGLE = 0,
            III_TURN_ANGLE = 0,
            IV_TURN_ANGLE = 0,
            TURN_MULT = 0.75,
            TANK_POS = 1.0,
            ANGLE_THRESHHOLD = 1;
    @Override
    public void init() {
        super.init();
        encPos = 0;
        normalized = true;
        mult = 1;
        cmAngle = 0;
        td = SensorBotWestTemplate.TurnDir.FOR;
        turn = true;
        dm = DriveMode.SWERVE;

    }

    @Override
    public void start() {
        super.start();
        desiredAngle = normalizeGyroAngle(getGyroAngle());
    }

    @Override
    public void loop() {
        xr = gamepad1.right_stick_x;
        yr = gamepad1.right_stick_y;
        xl = gamepad1.left_stick_x;
        yl = gamepad1.left_stick_y;
        switch(dm){
            case SWERVE:
                angles = imu.getAngularOrientation(AxesReference.INTRINSIC, GyroAngles.ORDER, GyroAngles.UNIT);
                angleOfRotation = normalizeGyroAngle(getGyroAngle());
                turnMult = 1 - yl * (1 - TURN_MULT);
                switch(tm){
                    case FIELD_CENTRIC:
                        turnMult *= Math.sin(Math.toRadians(normalizeGamepadAngleR(angleOfRotation)));

                        break;
                    case ARCADE:
                        turnMult *= xr;
                }
                I = -Math.cos(Math.toRadians(angleOfRotation + I_TURN_ANGLE)) * turnMult;
                II = -Math.cos(Math.toRadians(angleOfRotation + 90 + II_TURN_ANGLE)) * turnMult;
                III = -Math.cos(Math.toRadians(angleOfRotation + 180 + III_TURN_ANGLE)) * turnMult;
                IV = -Math.cos(Math.toRadians(angleOfRotation + 270 + IV_TURN_ANGLE)) * turnMult;
                if (normalized) {
                    max = xr / UniversalFunctions.maxAbs(I, II, III, IV);
                    I *= max;
                    II *= max;
                    III *= max;
                    IV *= max;
                }
                I += Math.sqrt(xl * xl + yl * yl) * mult;
                II += Math.sqrt(xl * xl + yl * yl) * mult;
                III += Math.sqrt(xl * xl + yl * yl) * mult;
                IV += Math.sqrt(xl * xl + yl * yl) * mult;
                max = UniversalFunctions.maxAbs(I, II, III, IV);
                if (max > 1) {
                    I /= max;
                    II /= max;
                    III /= max;
                    IV /= max;
                }
                if (Math.abs(gamepad1.left_stick_y) >= UniversalConstants.Triggered.STICK || Math.abs(gamepad1.left_stick_x) >= UniversalConstants.Triggered.STICK)
                    desiredAngle = normalizeGamepadAngleL(cmAngle);
                switch (td) {
                    case FOR:
                        if (desiredAngle > 180 && turn) {
                            td = SensorBotWestTemplate.TurnDir.BACK;
                            mult *= -1;
                            desiredAngle = UniversalFunctions.normalizeAngle(desiredAngle + 180, 0);
                            turn = false;
                        } else if (desiredAngle <= 0)
                            turn = true;
                        break;
                    case BACK:
                        if (desiredAngle < 180 && turn) {
                            td = SensorBotWestTemplate.TurnDir.FOR;
                            turn = false;
                            mult *= -1;
                        } else if (desiredAngle <= 0)
                            turn = true;
                        break;
                }
                desiredAngle = UniversalFunctions.normalizeAngle(desiredAngle, angleOfRotation);
                desiredPos = getEncoderRotation(desiredAngle, cmAngle);
                //encPos = getEncoderRotation(cmAngle);
                encPos += (int)getEncoderRotation(cmAngle);
                //cm.setPower(Math.sin(Math.toRadians(encPos)));
                cm.setTargetPosition(encPos);
                refreshMotors(I, II, III, IV, true);
                cmAngle = UniversalFunctions.normalizeAngle(getMotorAngle(encPos));
                if(gamepad1.left_stick_button && gamepad1.right_stick_button){
                    dm = DriveMode.TANK;
                    tdm = TankDriveMode.SHIFT;
                }
                break;
            case TANK:
                switch(tdm){
                    case SHIFT:
                        cmAngle = UniversalFunctions.normalizeAngle(getMotorAngle(encPos));
                        desiredAngle = UniversalFunctions.normalizeAngle(0, angleOfRotation);
                        desiredPos = getEncoderRotation(desiredAngle, cmAngle);
                        encPos = (int)getEncoderRotation(cmAngle);
                        cm.setPower(Math.sin(Math.toRadians(encPos)));
                        if(Math.abs(desiredAngle) < ANGLE_THRESHHOLD){
                            ballShift.setPosition(TANK_POS);
                            tdm = TankDriveMode.TANK;
                        }
                        break;
                    case TANK:
                        //PUT DESIRED WCD TURNMODE HERE
                }

        }

    }
}