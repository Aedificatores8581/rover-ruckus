package org.firstinspires.ftc.teamcode.robotOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.robotTemplates.SensorBotWestTemplate;
import org.firstinspires.ftc.teamcode.robotTemplates.SwerveBotTemplate;
import org.firstinspires.ftc.teamcode.robotTemplates.WestBotTemplate;
import org.firstinspires.ftc.teamcode.robotUniversal.GyroAngles;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;

/**
 * Created by Frank Portman on 4/23/2018.
 */
//
@TeleOp(name = "CrabBotDriveTest", group = "Test_Drive")
public class CrabBotDriveTest extends SwerveBotTemplate {
    double angleOfRotation, I, II, III, IV, max, desiredAngle, desiredPos, xl, yl, yr, xr, mult, cmAngle, turnMult, cos, lp, rp, rad, normAngle, sin;
    boolean normalized, turn;
    int encPos;
    DriveMode dm;
    TankDriveMode tdm;
    TurnMode tm;
    WestBotTemplate.TurnDir tdWest;
    SensorBotWestTemplate.TurnDir td;
    WestBotTemplate.ControlState cs;
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
        //Put desired west coast turn mode here
        cs = WestBotTemplate.ControlState.FIELD_CENTRIC;
    }
    @Override
    public void start() {
        super.start();
        desiredAngle = normalizeGyroAngle();
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
                angleOfRotation = normalizeGyroAngle();
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
                    max = xr / Math.max(Math.max(Math.abs(I), Math.abs(II)), Math.max(Math.abs(III), Math.abs(IV)));
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
                        switch(td){
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
                        desiredPos = getEncoderRotation(desiredAngle, cmAngle);
                        encPos = (int)getEncoderRotation(cmAngle);
                        cm.setPower(Math.sin(Math.toRadians(encPos)));
                        if(Math.abs(desiredAngle) < ANGLE_THRESHHOLD){
                            ballShift.setPosition(TANK_POS);
                            tdm = TankDriveMode.TANK;
                        }
                        break;
                    case TANK:
                        switch(cs){
                            case ARCADE:
                                switch(cs){
                                    case ARCADE:
                                        yl = -Math.sqrt(xl * xl + yl * yl) * UniversalFunctions.round(yl);
                                        turnMult = 1 - gamepad1.left_stick_y * (1 - TURN_MULT);
                                        refreshMotors(-yl + turnMult * xr, -yl - turnMult * xr, -yl - turnMult * xr, -yl * turnMult * xr, true);
                                        break;
                                    case FIELD_CENTRIC:
                                        rad = Math.sqrt(xl * xl + yl * yl);
                                        normAngle = Math.toRadians(normalizeGamepadAngleL(normalizeGyroAngle()));
                                        if(rad < UniversalConstants.Triggered.STICK) {
                                            refreshMotors(0, 0, 0, 0, true);
                                        }
                                        else {
                                            switch (td) {
                                                case FOR:
                                                    sin = Math.sin(normAngle);
                                                    if (Math.sin(normAngle) < 0 && turn) {
                                                        tdWest = WestBotTemplate.TurnDir.BACK;
                                                        mult *= -1;
                                                        turn = false;
                                                    } else if (Math.sin(normAngle) >= 0)
                                                        turn = true;
                                                    break;
                                                case BACK:
                                                    sin = Math.sin(normAngle);
                                                    if (Math.sin(normAngle) > 0 && turn) {
                                                        tdWest = WestBotTemplate.TurnDir.FOR;
                                                        turn = false;
                                                        mult *= -1;
                                                    } else if (Math.sin(normAngle) <= 0)
                                                        turn = true;
                                                    break;
                                            }
                                            cos = Math.cos(normAngle);
                                            lp = -rad * mult - mult * (Math.abs(cos) + 1) * cos;
                                            rp = -rad * mult + mult * (Math.abs(cos) + 1) * cos;
                                            refreshMotors(rp, lp, lp, rp, true);
                                        }
                                        break;
                                    case TANK:
                                        lp = -gamepad1.left_stick_y;
                                        rp = -gamepad1.right_stick_y;
                                        refreshMotors(rp, lp, lp, rp, true);
                                        break;
                                }
                        }
                }
        }
    }
}