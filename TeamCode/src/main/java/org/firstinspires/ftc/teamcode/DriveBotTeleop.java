package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by The Saminator on 10-01-2017.
 */
@TeleOp(name = "Tele-Op", group = "actually not a test")
public class DriveBotTeleop extends DriveBotTemplate {
    private Gamepad prev1;
    private Gamepad prev2;
    private Timer async;

    private double speedMult;

    private boolean lifting;

    @Override
    public void init() {
        super.init();
        prev1 = new Gamepad();
        prev2 = new Gamepad();
        speedMult = 0.7;
    }

    protected void highDelivery() {
        lifting = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                setLift2Pow(0.5);
                while (Math.abs(liftMtr2.getCurrentPosition()) <= 225);
                setLift2Pow(0.0);
                leftHinge.setPosition(1.0);
                rightHinge.setPosition(1.0);
                setLift2Pow(-0.5);
                while (Math.abs(liftMtr2.getCurrentPosition()) >= 0);
                setLift2Pow(0.0);
                leftHinge.setPosition(0.0);
                rightHinge.setPosition(0.0);
                lifting = false;
            }
        }).run();
    }

    protected void midDelivery() {
        lifting = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                setLift2Pow(0.5);
                while (Math.abs(liftMtr2.getCurrentPosition()) <= 150);
                setLift2Pow(0.0);
                leftHinge.setPosition(1.0);
                rightHinge.setPosition(1.0);
                setLift2Pow(-0.5);
                while (Math.abs(liftMtr2.getCurrentPosition()) >= 0);
                setLift2Pow(0.0);
                leftHinge.setPosition(0.0);
                rightHinge.setPosition(0.0);
                lifting = false;
            }
        }).run();
    }

    protected void lowDelivery() {
        lifting = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                setLift2Pow(0.5);
                while (Math.abs(liftMtr2.getCurrentPosition()) <= 75);
                setLift2Pow(0.0);
                leftHinge.setPosition(1.0);
                rightHinge.setPosition(1.0);
                setLift2Pow(-0.5);
                while (Math.abs(liftMtr2.getCurrentPosition()) >= 0);
                setLift2Pow(0.0);
                leftHinge.setPosition(0.0);
                rightHinge.setPosition(0.0);
                lifting = false;
            }
        }).run();
    }

    protected void toggleSpeed() {
        if (speedMult > 0.5)
            speedMult = 0.3;
        else
            speedMult = 0.7;
    }

    protected void toggleIntake() {
        if (getIntakePow() <= 0.25)
            setIntakePow(0);
        else
            setIntakePow(-0.5);
    }

    protected void toggleGrabber() {
        if (relicGrabber.getPosition() >= 0.5)
            relicGrabber.setPosition(0.0);
        else
            relicGrabber.setPosition(1.0);
    }

    protected void extendArm() {
        setArmPow(0.5);
        while (Math.abs(armMotor.getCurrentPosition()) <= 150);
        setArmPow(0.0);
    }

    protected void retractArm() {
        setArmPow(-0.5);
        while (Math.abs(armMotor.getCurrentPosition()) >= 0);
        setArmPow(0.0);
    }

    @Override
    public void loop() {
        setLeftPow(gamepad1.left_stick_y * speedMult);
        setRightPow(gamepad1.right_stick_y * speedMult);

        if (gamepad1.a && !prev1.a)
            scream();

        if (gamepad1.b && !prev1.b && !lifting)
            highDelivery();

        if (gamepad1.y && !prev1.y && !lifting)
            midDelivery();

        if (gamepad1.x && !prev1.x && !lifting)
            lowDelivery();

        if (gamepad1.left_bumper && !prev1.left_bumper)
            toggleSpeed();

        /*if (gamepad1.dpad_up && !prev1.dpad_up)
            setIntakePow(0.5);

        if (gamepad1.dpad_down && !prev1.dpad_down)
            setIntakePow(-0.5);

        if (prev1.dpad_up && !gamepad1.dpad_up)
            setIntakePow(0);

        if (prev1.dpad_down && !gamepad1.dpad_down)
            setIntakePow(0);

        if (gamepad1.right_bumper && !prev1.right_bumper)
            toggleIntake();

        if (gamepad2.right_bumper && !prev2.right_bumper)
            toggleGrabber();

        if (gamepad2.dpad_up && !prev2.dpad_up)
            extendArm();

        if (gamepad2.dpad_down && !prev2.dpad_down)
            retractArm();

        relicGrabMover.setPosition(relicGrabMover.getPosition() + (gamepad2.right_stick_y * 0.1));

        armTilter.setPosition(armTilter.getPosition() + (gamepad2.left_stick_y * 0.1));*/

        try {
            prev1.fromByteArray(gamepad1.toByteArray());
            prev2.fromByteArray(gamepad2.toByteArray());
        } catch (RobotCoreException e) {
            telemetry.addData("Exception", e.getMessage());
        }
    }
}
