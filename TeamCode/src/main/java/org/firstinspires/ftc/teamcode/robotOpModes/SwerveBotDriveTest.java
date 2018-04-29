package org.firstinspires.ftc.teamcode.robotOpModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.teamcode.robotTemplates.SensorBotWestTemplate;
import org.firstinspires.ftc.teamcode.robotTemplates.SwerveBotTemplate;
import org.firstinspires.ftc.teamcode.robotUniversal.GyroAngles;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalConstants;
import org.firstinspires.ftc.teamcode.robotUniversal.UniversalFunctions;

/**
 * Created by Frank Portman 3/31/2018.
 */
//
@TeleOp(name = "SwervBotTestDrive", group = "Test_Drive")
public class SwerveBotDriveTest extends SwerveBotTemplate {
    double angleOfRotation, I, II, III, IV, max, desiredAngle, desiredPos, swervoPos, normSwervoPos, xl, yr, yl, xr, mult, swervoAngle, botAngle;
    boolean normalized, turn;
    SensorBotWestTemplate.TurnDir td;
    public final double
            I_TURN_ANGLE = 0,
            II_TURN_ANGLE = 0,
            III_TURN_ANGLE = 0,
            IV_TURN_ANGLE = 0;
    @Override
    public void init(){
        super.init();
        swervoPos = 0;
        normSwervoPos = 0;
        normalized = true;
        mult = 1;
    }
    @Override
    public void start(){
        super.start();
        desiredAngle = normalizeGyroAngle(getGyroAngle());
        lfswervo.setPosition(normSwervoPos);
        rfswervo.setPosition(normSwervoPos);
        rrswervo.setPosition(normSwervoPos);
        lrswervo.setPosition(normSwervoPos);
        turn = true;
    }
    @Override
    public void loop(){
        xr = gamepad1.right_stick_x;
        yr = gamepad1.right_stick_y;
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, GyroAngles.ORDER, GyroAngles.UNIT);
        angleOfRotation = normalizeGyroAngle(getGyroAngle());
        xl = gamepad1.left_stick_x;
        yl = gamepad1.left_stick_y;
        botAngle = Math.toRadians(normalizeGamepadAngleR(angleOfRotation));
        I = -Math.cos(Math.toRadians(angleOfRotation + I_TURN_ANGLE)) * Math.sin(botAngle);
        II = -Math.cos(Math.toRadians(angleOfRotation + 90 + II_TURN_ANGLE)) * Math.sin(botAngle);
        III = -Math.cos(Math.toRadians(angleOfRotation + 180 + III_TURN_ANGLE)) * Math.sin(botAngle);
        IV = -Math.cos(Math.toRadians(angleOfRotation + 270 + IV_TURN_ANGLE)) * Math.sin(botAngle);
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
            desiredAngle = normalizeGamepadAngleL(swervoAngle);
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
        desiredPos = getSwervoRotation(desiredAngle, swervoAngle);
        swervoPos = getSwervoRotation(swervoAngle);
        if ((int) swervoPos == (int) Math.abs(desiredPos)) {
            lfswervo.setPosition(Math.abs(desiredPos));
            rfswervo.setPosition(Math.abs(desiredPos));
            rrswervo.setPosition(Math.abs(desiredPos));
            lrswervo.setPosition(Math.abs(desiredPos));
            swervoAngle = UniversalFunctions.normalizeAngle(getSwervoAngle(desiredPos) + swervoAngle);
        } else if (desiredPos < 0) {
            if (lfswervo.getPosition() < 1 && lfswervo.getPosition() > 0)
                swervoAngle = prevRotation(swervoAngle);
            else
                swervoAngle = UniversalFunctions.normalizeAngle(swervoAngle - swervoRotationRatio);
            lfswervo.setPosition(0);
            rfswervo.setPosition(0);
            rrswervo.setPosition(0);
            lrswervo.setPosition(0);
        } else if (desiredPos > 0) {
            if (lfswervo.getPosition() < 1 && lfswervo.getPosition() > 0)
                swervoAngle = nextRotation(swervoAngle);
            else
                swervoAngle = UniversalFunctions.normalizeAngle(swervoAngle + swervoRotationRatio);
            lfswervo.setPosition(1);
            rfswervo.setPosition(1);
            rrswervo.setPosition(1);
            lrswervo.setPosition(1);
        }
        refreshMotors(I, II, III, IV, true);
    }
}
