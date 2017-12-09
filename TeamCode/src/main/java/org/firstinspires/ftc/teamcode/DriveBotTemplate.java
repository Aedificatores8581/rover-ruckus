package org.firstinspires.ftc.teamcode;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * Conjured into existence by The Saminator on 06-29-2017.
 */
public abstract class DriveBotTemplate extends SleepableOpMode {

    MediaPlayer wilhelmScream;

    public static class Constants {
        public static final DcMotor.Direction LEFT_FORE_DIR = DcMotor.Direction.FORWARD;
        public static final DcMotor.Direction LEFT_REAR_DIR = DcMotor.Direction.FORWARD;
        public static final DcMotor.Direction RIGHT_FORE_DIR = DcMotor.Direction.REVERSE;
        public static final DcMotor.Direction RIGHT_REAR_DIR = DcMotor.Direction.REVERSE;
        public static final DcMotor.Direction ARM_DIR = DcMotor.Direction.FORWARD;
        public static final DcMotor.Direction LIFT_1_DIR = DcMotor.Direction.REVERSE;
        public static final DcMotor.Direction LIFT_2_DIR = DcMotor.Direction.REVERSE;

        public static final DcMotor.Direction LEFT_INTAKE1_DIR = DcMotor.Direction.FORWARD;
        public static final DcMotor.Direction LEFT_INTAKE2_DIR = DcMotor.Direction.FORWARD;
        public static final DcMotor.Direction RIGHT_INTAKE1_DIR = DcMotor.Direction.FORWARD;
        public static final DcMotor.Direction RIGHT_INTAKE2_DIR = DcMotor.Direction.FORWARD;

        public static final double LEFT_FORE_SPEED = 1.0;
        public static final double LEFT_REAR_SPEED = 1.0;
        public static final double RIGHT_FORE_SPEED = 1.0;
        public static final double RIGHT_REAR_SPEED = 1.0;
        public static final double ARM_SPEED = 1.0;
        public static final double LIFT_1_SPEED = 1.0;
        public static final double LIFT_2_SPEED = 1.0;
    }

    DcMotor leftFore, leftRear, rightFore, rightRear;
    DcMotor liftMtr1, liftMtr2;
//    DcMotor armMotor;
    // Lift motor 1 is for lifting the glyph onto the platorm.
    // List motor 2 is for dispensing the glyph onto the cryptobox.
    CRServo leftIntake1, leftIntake2, rightIntake1, rightIntake2;

    Servo glyphGrabberRight;
    Servo relicGrabber, relicGrabMover, armTilter, jewelArm, jewelFlipper, rightHinge, leftHinge;
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
//        armMotor = hardwareMap.dcMotor.get("ra");
/*        liftMtr1 = hardwareMap.dcMotor.get("lm1");
        liftMtr2 = hardwareMap.dcMotor.get("lm2");
*/
        colors = colorSensor.getNormalizedColors();

        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "jcolor");

        relicGrabber = hardwareMap.servo.get("rg"); // port 0
        relicGrabMover = hardwareMap.servo.get("rgm");
        armTilter = hardwareMap.servo.get("ts");
        jewelArm = hardwareMap.servo.get("ja");
        jewelFlipper = hardwareMap.servo.get("jf");
        rightHinge = hardwareMap.servo.get("rh");
        leftHinge = hardwareMap.servo.get("lh");

        rightIntake1 = hardwareMap.crservo.get("ri1");
        rightIntake2 = hardwareMap.crservo.get("ri2");
        leftIntake1 = hardwareMap.crservo.get("li1");
        leftIntake2 = hardwareMap.crservo.get("li2");
        //endregion

        leftFore.setDirection(Constants.LEFT_FORE_DIR);
        leftRear.setDirection(Constants.LEFT_REAR_DIR);
        rightFore.setDirection(Constants.RIGHT_FORE_DIR);
        rightRear.setDirection(Constants.RIGHT_REAR_DIR);
//        armMotor.setDirection(Constants.ARM_DIR);
        liftMtr1.setDirection(Constants.LIFT_1_DIR);
        liftMtr2.setDirection(Constants.LIFT_2_DIR);

        leftFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMtr1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMtr2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rightIntake1.setDirection(Constants.RIGHT_INTAKE1_DIR);
        rightIntake2.setDirection(Constants.RIGHT_INTAKE2_DIR);
        leftIntake1.setDirection(Constants.LEFT_INTAKE1_DIR);
        leftIntake2.setDirection(Constants.LEFT_INTAKE2_DIR);

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

/*    protected void setArmPow(double pow) {
        armMotor.setPower(pow * Constants.ARM_SPEED);
    }
*/
    protected void setLift1Pow(double pow) {
        liftMtr1.setPower(pow * Constants.LIFT_1_SPEED);
    }

    protected void setLift2Pow(double pow) {
        liftMtr2.setPower(pow * Constants.LIFT_2_SPEED);
    }

    protected void setIntakePow(double pow) {
        rightIntake1.setPower(pow);
        rightIntake2.setPower(pow);
        leftIntake1.setPower(pow);
        leftIntake2.setPower(pow);
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

    protected double getIntakePow() {
        return rightIntake1.getPower();
    }

    //place value for ticks for right motor here
    //place value for ticks for left motor here
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
//0.15 = finger
//0.25 = jewel arm up