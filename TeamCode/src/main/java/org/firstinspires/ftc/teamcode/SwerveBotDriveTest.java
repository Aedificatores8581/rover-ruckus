package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Frank Portman 3/31/2018.
 */
@TeleOp(name = "SwervBotTestDrive", group = "Test_Drive")
public class SwerveBotDriveTest extends SwerveBotTemplate{
    double angleOfRotation, I, II, III, IV, max, desiredAngle, desiredPos, swervoPos, normSwervoPos, x, y;
    int swervoDirection;
    public final double TURN_ANGLE = 0;
    @Override
    public void init(){
        super.init();
        swervoPos = 0;
        normSwervoPos = 0;
    }
    @Override
    public void start(){
        super.start();
        desiredAngle = normalizeGyroAngle(gyroSensor.rawX());
        lfswervo.setPosition(normSwervoPos);
        rfswervo.setPosition(normSwervoPos);
        rrswervo.setPosition(normSwervoPos);
        lrswervo.setPosition(normSwervoPos);
    }
    @Override
    public void loop(){
        angleOfRotation = normalizeGyroAngle(gyroSensor.rawX());
        x = gamepad1.left_stick_x;
        y = gamepad1.left_stick_y;
        I = Math.sqrt(x * x + y * y) * round(y) + Math.cos(angleOfRotation + TURN_ANGLE) * gamepad1.right_stick_x;

        II = Math.sqrt(x * x + y * y) * round(y) + Math.cos(angleOfRotation + 90 + TURN_ANGLE) * gamepad1.right_stick_x;

        III = Math.sqrt(x * x + y * y) * round(y) + Math.cos(angleOfRotation + 90 + TURN_ANGLE) * gamepad1.right_stick_x;

        IV = Math.sqrt(x * x + y * y) * round(y) + Math.cos(angleOfRotation + 180 + TURN_ANGLE) * gamepad1.right_stick_x;

        max = Math.max(Math.max(Math.abs(I), Math.abs(II)), Math.max(Math.abs(III), Math.abs(IV)));

        if(max > 1){
            I /= max;
            II /= max;
            III /= max;
            IV /= max;
        }

        if(Math.abs(gamepad1.left_stick_y) >= 0.2 || Math.abs(gamepad1.left_stick_x) >= 0.2)
            desiredAngle = getGamepadAngle();
        desiredPos = getSwervoRotation(desiredAngle + angleOfRotation, startAngle);
        setSwervoPos(desiredPos);
        refreshMotors(I, II, III, IV, true);
    }
    public void setSwervoPos(double pos){
        swervoDirection = (int)round(desiredPos - swervoPos);
        Range.clip(swervoDirection, 1, -1);
        swervoPos +=  swervoDirection * normSwervoPos;
        if(normSwervoPos == 0)
            normSwervoPos = 1;
        else if(normSwervoPos == 1)
            normSwervoPos = 0;
        lfswervo.setPosition(normSwervoPos);
        rfswervo.setPosition(normSwervoPos);
        rrswervo.setPosition(normSwervoPos);
        lrswervo.setPosition(normSwervoPos);
        normSwervoPos = desiredPos - swervoPos;
        Range.clip(normSwervoPos, 0, 1);
    }
}
