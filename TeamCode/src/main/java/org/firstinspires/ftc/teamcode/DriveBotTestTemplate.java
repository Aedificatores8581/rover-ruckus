package org.firstinspires.ftc.teamcode;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Locale;

/**
 * Conjured into existence by The Saminator on 06-29-2017.
 */
public abstract class DriveBotTestTemplate extends OpMode {

    MediaPlayer wilhelmScream, danceMusic;

    public static class Constants {
        public static final DcMotor.Direction LEFT_FORE_DIR = DcMotor.Direction.REVERSE;
        public static final DcMotor.Direction LEFT_REAR_DIR = DcMotor.Direction.REVERSE;
        public static final DcMotor.Direction RIGHT_FORE_DIR = DcMotor.Direction.FORWARD;
        public static final DcMotor.Direction RIGHT_REAR_DIR = DcMotor.Direction.FORWARD;

        public static final DcMotor.Direction INTAKE_LEFT_DIR = DcMotor.Direction.FORWARD;
        public static final DcMotor.Direction INTAKE_RIGHT_DIR = DcMotor.Direction.FORWARD;

        public static final DcMotor.Direction GLYPH_LIFT_DIR = DcMotor.Direction.FORWARD;

        public static final DcMotor.Direction BELT1_DIR = DcMotor.Direction.FORWARD;
        public static final DcMotor.Direction BELT2_DIR = DcMotor.Direction.REVERSE;

        public static final double RAMP_SPEED = 0.0625;

        public static final double LEFT_FORE_SPEED = 1.0;
        public static final double LEFT_REAR_SPEED = 1.0;
        public static final double RIGHT_FORE_SPEED = 1.0;
        public static final double RIGHT_REAR_SPEED = 1.0;
    }

    DcMotor leftFore, leftRear, rightFore, rightRear, dispenserLinearSlide;
    DcMotor relicArm;

    DcMotor glyphLift;
    DigitalChannel glyphLiftHigh, glyphLiftLow;

    //power port 5v

    DcMotor intakeLeft, intakeRight;

    CRServo belt1, belt2;

    Servo jewelArm, jewelFlipper, relicHand, relicFingers, glyphOutput, rIntake, lIntake;

    NormalizedColorSensor color;
    NormalizedRGBA colors;

    BNO055IMU imu;

    protected Orientation angles;
    protected Acceleration gravity;

    private double leftPow, rightPow;
    private boolean dancing;

    public double angleAtStart;

    @Override
    public void init() {
        this.msStuckDetectInit = 60000;

        leftPow = 0.0;
        rightPow = 0.0;

        dancing = false;

        //region Configuration section
        leftFore = hardwareMap.dcMotor.get("lfm"); // port 2
        leftRear = hardwareMap.dcMotor.get("lrm"); // port 3
        rightFore = hardwareMap.dcMotor.get("rfm"); // port 0
        rightRear = hardwareMap.dcMotor.get("rrm"); // port 1

        glyphLift = hardwareMap.dcMotor.get("gl");
        glyphLiftHigh = hardwareMap.digitalChannel.get("tts");
        glyphLiftLow = hardwareMap.digitalChannel.get("bts");

        intakeLeft = hardwareMap.dcMotor.get("iml");
        intakeRight = hardwareMap.dcMotor.get("imr");

        belt1 = hardwareMap.crservo.get("vm1");
        belt2 = hardwareMap.crservo.get("vm2");

        jewelArm = hardwareMap.servo.get("ja");
        jewelFlipper = hardwareMap.servo.get("jf");

        relicArm = hardwareMap.dcMotor.get("ra");
        relicHand = hardwareMap.servo.get("rh");
        relicFingers = hardwareMap.servo.get("rf");

        rIntake = hardwareMap.servo.get("rir");

        lIntake = hardwareMap.servo.get("lir");

        glyphOutput = hardwareMap.servo.get("gd");
        color = hardwareMap.get(NormalizedColorSensor.class, "jcolor");
        //endregion

        //angleAtStart = new GyroAngles(angles).getZ();

        leftFore.setDirection(Constants.LEFT_FORE_DIR);
        leftRear.setDirection(Constants.LEFT_REAR_DIR);
        rightFore.setDirection(Constants.RIGHT_FORE_DIR);
        rightRear.setDirection(Constants.RIGHT_REAR_DIR);

        glyphLift.setDirection(Constants.GLYPH_LIFT_DIR);
        glyphLiftHigh.setMode(DigitalChannel.Mode.INPUT);
        glyphLiftLow.setMode(DigitalChannel.Mode.INPUT);

        intakeLeft.setDirection(Constants.INTAKE_LEFT_DIR);
        intakeRight.setDirection(Constants.INTAKE_RIGHT_DIR);

        belt1.setDirection(Constants.BELT1_DIR);
        belt2.setDirection(Constants.BELT2_DIR);

        leftFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        wilhelmScream = MediaPlayer.create(hardwareMap.appContext, R.raw.scream);
        danceMusic = MediaPlayer.create(hardwareMap.appContext, R.raw.dance);

        if (isAutonomous()) {
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
            parameters.loggingEnabled = true;
            parameters.loggingTag = "IMU";
            parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

            imu = hardwareMap.get(BNO055IMU.class, "imu");
            imu.initialize(parameters);
        }

    }

    public void start() {
        telemetry.addAction(new Runnable() {
            @Override
            public void run() {
                angles = imu.getAngularOrientation(AxesReference.INTRINSIC, GyroAngles.ORDER, GyroAngles.UNIT);
                gravity = imu.getGravity();
            }
        });

        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override
                    public String value() {
                        return imu.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override
                    public String value() {
                        return imu.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override
                    public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override
                    public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override
                    public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });

        telemetry.addLine()
                .addData("grvty", new Func<String>() {
                    @Override
                    public String value() {
                        return gravity.toString();
                    }
                })
                .addData("mag", new Func<String>() {
                    @Override
                    public String value() {
                        return String.format(Locale.getDefault(), "%.3f",
                                Math.sqrt(gravity.xAccel * gravity.xAccel
                                        + gravity.yAccel * gravity.yAccel
                                        + gravity.zAccel * gravity.zAccel));
                    }
                });
    }

    @Override
    public void stop() {
        setLeftPow(0.0);
        setRightPow(0.0);

        wilhelmScream.release();
        wilhelmScream = null;

        danceMusic.release();
        danceMusic = null;
    }

    protected void succ(double power) {
        intakeLeft.setPower(-power);
        intakeRight.setPower(power);
    }

    protected void belt(double power) {
        belt1.setPower(power);
        belt2.setPower(power);
    }

    protected void scream() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                wilhelmScream.start();
            }
        });
    }

    protected double getLeftPow() {
        return leftPow;
    }

    protected double getRightPow() {
        return rightPow;
    }

    protected void setLeftPow(double pow) {
        leftPow = pow;
        leftFore.setPower(pow * Constants.LEFT_FORE_SPEED);
        leftRear.setPower(pow * Constants.LEFT_REAR_SPEED);
    }

    protected void setRightPow(double pow) {
        rightPow = pow;
        rightFore.setPower(pow * Constants.RIGHT_FORE_SPEED);
        rightRear.setPower(pow * Constants.RIGHT_REAR_SPEED);
    }

    protected boolean checkLeftEncoder(int ticks) {
        int distance = Math.abs(ticks);
        int leftForeDist = Math.abs(leftFore.getCurrentPosition());
        int leftRearDist = Math.abs(leftRear.getCurrentPosition());

        boolean mtrsHere = (distance <= leftForeDist)
                || (distance <= leftRearDist);

        return mtrsHere;
    }

    protected boolean checkRightEncoder(int ticks) {
        int distance = Math.abs(ticks);
        int rightForeDist = Math.abs(rightFore.getCurrentPosition());
        int rightRearDist = Math.abs(rightRear.getCurrentPosition());

        boolean mtrsHere = (distance <= rightForeDist)
                || (distance <= rightRearDist);

        return mtrsHere;
    }

    protected boolean checkEncoder(int ticks) {
        return checkLeftEncoder(ticks) || checkRightEncoder(ticks);
    }

    protected boolean checkEncodersReverse(int ticks) {
        int distance = Math.abs(ticks);
        int leftForeDist = Math.abs(leftFore.getCurrentPosition());
        int leftRearDist = Math.abs(leftRear.getCurrentPosition());
        int rightForeDist = Math.abs(rightFore.getCurrentPosition());
        int rightRearDist = Math.abs(rightRear.getCurrentPosition());

        boolean mtrsHere = (distance >= leftForeDist)
                || (distance >= leftRearDist)
                || (distance >= rightForeDist)
                || (distance >= rightRearDist);

        return mtrsHere;
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees) {
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    protected boolean triggered(double value) {
        return value >= 0.05;
    }

    protected void resetEncoders() {
        setLeftPow(0);
        setRightPow(0);

        leftFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    protected void reinitMotors(double leftSpeed, double rightSpeed) {
        leftFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        setLeftPow(leftSpeed);
        setRightPow(rightSpeed);
    }

    protected void startDance() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                danceMusic.setLooping(true);
                danceMusic.start();
            }
        });

        dancing = true;
    }

    protected void stopDance() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                danceMusic.stop();
            }
        });
        dancing = false;
    }

    protected boolean isDancing() {
        return dancing;
    }

    protected void dance() {
        setLeftPow(0.125);
        setRightPow(-0.125);
    }

    // This is here for not loading the gyro sensor when in teleop.
    protected boolean isAutonomous() {
        return true;
    }
}
