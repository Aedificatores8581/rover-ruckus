package org.firstinspires.ftc.teamcode;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by The Saminator on 06-29-2017.
 */
public abstract class DriveBotTestTemplate extends SleepableOpMode {

    MediaPlayer wilhelmScream;

    public static class Constants {
        public static final DcMotor.Direction LEFT_FORE_DIR = DcMotor.Direction.FORWARD;
        public static final DcMotor.Direction LEFT_REAR_DIR = DcMotor.Direction.FORWARD;
        public static final DcMotor.Direction RIGHT_FORE_DIR = DcMotor.Direction.REVERSE;
        public static final DcMotor.Direction RIGHT_REAR_DIR = DcMotor.Direction.REVERSE;

        public static final double LEFT_FORE_SPEED = 1.0;
        public static final double LEFT_REAR_SPEED = 1.0;
        public static final double RIGHT_FORE_SPEED = 1.0;
        public static final double RIGHT_REAR_SPEED = 1.0;
    }

    DcMotor leftFore, leftRear, rightFore, rightRear;
    DcMotor relicArm;
    Servo jewelArm, jewelFlipper, relicHand;
    CRServo relicFingers;

    ColorSensor color;

    @Override
    public void init() {
        //region Configuration section
        leftFore = hardwareMap.dcMotor.get("lfm"); // port 2
        leftRear = hardwareMap.dcMotor.get("lrm"); // port 3
        rightFore = hardwareMap.dcMotor.get("rfm"); // port 0
        rightRear = hardwareMap.dcMotor.get("rrm"); // port 1

        jewelArm = hardwareMap.servo.get("ja");
        jewelFlipper = hardwareMap.servo.get("jf");

        relicArm = hardwareMap.dcMotor.get("ra");
        relicHand = hardwareMap.servo.get("rh");
        relicFingers = hardwareMap.crservo.get("rf");

        color = hardwareMap.colorSensor.get("jcolor");
        //endregion

        leftFore.setDirection(Constants.LEFT_FORE_DIR);
        leftRear.setDirection(Constants.LEFT_REAR_DIR);
        rightFore.setDirection(Constants.RIGHT_FORE_DIR);
        rightRear.setDirection(Constants.RIGHT_REAR_DIR);

        leftFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        wilhelmScream = MediaPlayer.create(hardwareMap.appContext, R.raw.scream);
    }

    @Override
    public void stop() {
        setLeftPow(0.0);
        setRightPow(0.0);

        wilhelmScream.release();
        wilhelmScream = null;
    }

    protected void scream() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                wilhelmScream.start();
            }
        });
    }

    protected void setLeftPow(double pow) {
        leftFore.setPower(pow * Constants.LEFT_FORE_SPEED);
        leftRear.setPower(pow * Constants.LEFT_REAR_SPEED);
    }

    protected void setRightPow(double pow) {
        rightFore.setPower(pow * Constants.RIGHT_FORE_SPEED);
        rightRear.setPower(pow * Constants.RIGHT_REAR_SPEED);
    }

    protected boolean checkEncoder(int ticks) {
        int distance = Math.abs(ticks);
        int leftForeDist = Math.abs(leftFore.getCurrentPosition());
        int leftRearDist = Math.abs(leftRear.getCurrentPosition());
        int rightForeDist = Math.abs(rightFore.getCurrentPosition());
        int rightRearDist = Math.abs(rightRear.getCurrentPosition());

        return (distance <= leftForeDist)
                || (distance <= leftRearDist)
                || (distance <= rightForeDist)
                || (distance <= rightRearDist);
    }
}
