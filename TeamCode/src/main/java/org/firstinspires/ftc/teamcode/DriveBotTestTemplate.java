package org.firstinspires.ftc.teamcode;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Conjured into existence by The Saminator on 06-29-2017.
 */
public abstract class DriveBotTestTemplate extends OpMode {

    MediaPlayer wilhelmScream;
    BNO055IMU imu;

    Orientation angles;
    Acceleration gravity;
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
    Servo jewelArm, jewelFlipper, relicHand, relicFingers;

    NormalizedRGBA colors;



    NormalizedColorSensor colorSensor;
    protected int prevLeftForeEncr = 0;
    protected int prevLeftRearEncr = 0;
    protected int prevRightForeEncr = 0;
    protected int prevRightRearEncr = 0;

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
        relicFingers = hardwareMap.servo.get("rf");

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "jcolor");

        colors = colorSensor.getNormalizedColors();



        leftFore.setDirection(Constants.LEFT_FORE_DIR);
        leftRear.setDirection(Constants.LEFT_REAR_DIR);
        rightFore.setDirection(Constants.RIGHT_FORE_DIR);
        rightRear.setDirection(Constants.RIGHT_REAR_DIR);

        leftFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        wilhelmScream = MediaPlayer.create(hardwareMap.appContext, R.raw.scream);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

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
    protected boolean checkLeftEncoder(int target) {
        boolean fore = (Math.abs(leftFore.getCurrentPosition() - prevLeftForeEncr) >= target);
        boolean rear = (Math.abs(leftRear.getCurrentPosition() - prevLeftRearEncr) >= target);
        return fore || rear;
    }

    protected boolean checkRightEncoder(int target) {
        boolean fore = (Math.abs(rightFore.getCurrentPosition() - prevRightForeEncr) >= target);
        boolean rear = (Math.abs(rightRear.getCurrentPosition() - prevRightRearEncr) >= target);
        return fore || rear;
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
