package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/*
 * Created by Mister-Minister-Master on 11/12/2017.
 */
@TeleOp (name = "Solon Scrimmage TeleOp For Sensor Bot", group = "8581")
public class SolonScrimmageTeleop extends OpMode{

    //region Initialize servos and motors; also create the constants that are used with position[] and any increment values.
    private DcMotor rearLeft, rearRight;
    private Servo glyphGrabberRight, glyphGrabberCenter, glyphGrabberLeft, ballSensorArm;

    double position[] = new double[4]; /************
                                        * Position for different servos:
                                        * position[0] ----> glyphGrabberRight
                                        * position[1] ----> glyphGrabberCenter
                                        * position[2] ----> glyphGrabberLeft
                                        * position[3] ----> ballSnsorArm (Never used in TeleOp)*/

    private abstract class SERVO_CONSTANTS {

        protected static final double INCREMENT_VALUE = 0.005;

        // Indexes for the different servos in position[]
        protected static final int GLYPH_GRABBER_INDEX_RIGHT = 0;
        protected static final int GLYPH_GRABBER_INDEX_CENTER = 1;
        protected static final int GLYPH_GRABBER_INDEX_LEFT = 2;
        protected static final int BALL_SENSOR_ARM_INDEX = 3;

        // Initial positions for all the Servos
    }
    //endregion


    ColorSensor colorSensor; // Likely to be not used in TeleOp

    public void init(){

        rearLeft = hardwareMap.dcMotor.get("lm");
        rearRight = hardwareMap.dcMotor.get("rm");


        glyphGrabberRight = hardwareMap.servo.get("ggr");
        glyphGrabberCenter = hardwareMap.servo.get("ggc");
        glyphGrabberLeft = hardwareMap.servo.get("ggl");
        ballSensorArm = hardwareMap.servo.get("bsa");

        glyphGrabberRight.setDirection(Servo.Direction.FORWARD);
        glyphGrabberCenter.setDirection(Servo.Direction.REVERSE);
        glyphGrabberLeft.setDirection(Servo.Direction.REVERSE);
        ballSensorArm.setDirection(Servo.Direction.FORWARD);
        colorSensor = hardwareMap.colorSensor.get("cs");



    }

    public void start(){
        glyphGrabberRight.setPosition(0.0);
        glyphGrabberCenter.setPosition(0.0);
        glyphGrabberLeft.setPosition(0.0);
        ballSensorArm.setPosition(.59);

    }

    public void loop() {
        telemetry.clear();
        telemetry.update();

        //region Setting the Servo position in the position[] array
        position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_RIGHT] = glyphGrabberRight.getPosition();
        position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_CENTER] = glyphGrabberCenter.getPosition();
        position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_LEFT] = glyphGrabberLeft.getPosition();
        position[SERVO_CONSTANTS.BALL_SENSOR_ARM_INDEX] = ballSensorArm.getPosition();
        //endregion


        //region rearLeft and rearRight Code: moves the actual bot using the y position of the left and right stick
        if (Math.abs(gamepad1.left_stick_y) > .5) {
            setRightPow(gamepad1.left_stick_y * 1.2);
        }else{
            setRightPow(0.0);
        }
        if (Math.abs(gamepad1.right_stick_y) > .5){
            setLeftPow(-gamepad1.right_stick_y * 1.2);
        }else{
            setLeftPow(0.0);
        }
        //endregion


        //region glyphGrabberCenter Code: Moves the grabber arms up and down using the up and down buttons on the DPAD
        if (gamepad1.dpad_up){
            telemetry.addLine("Pressing up");

            position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_CENTER] += SERVO_CONSTANTS.INCREMENT_VALUE;

            glyphGrabberCenter.setPosition(position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_CENTER]);

        }else if (gamepad1.dpad_down){
            telemetry.addLine("Pressing down");

            position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_CENTER] -= SERVO_CONSTANTS.INCREMENT_VALUE;

            glyphGrabberCenter.setPosition(position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_CENTER]);

        }
        //endregion


        //region Glyph Grabber (Left and right): Allows for the opening and closing of the grabber arms
        if (gamepad1.right_bumper){
            telemetry.addLine("Pressing right_bumper");
            position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_LEFT] += SERVO_CONSTANTS.INCREMENT_VALUE;
            telemetry.addData("position[left]",position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_LEFT]);
            position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_RIGHT] += SERVO_CONSTANTS.INCREMENT_VALUE;

            glyphGrabberLeft.setPosition(position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_LEFT]);
            glyphGrabberRight.setPosition(position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_RIGHT]);
        } else if (gamepad1.right_trigger > .5){
            telemetry.addLine("Pressing right_trigger");
            position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_LEFT] -= SERVO_CONSTANTS.INCREMENT_VALUE;
            position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_RIGHT] -= SERVO_CONSTANTS.INCREMENT_VALUE;
            telemetry.addData("position[left]",position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_LEFT]);

            glyphGrabberLeft.setPosition(position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_LEFT]);
            glyphGrabberRight.setPosition(position[SERVO_CONSTANTS.GLYPH_GRABBER_INDEX_RIGHT]);
        }

        //endregion


        //region ballSensorArm code: moves ball sensor arm up and down (generally for debugging autonomous)
        if(gamepad1.x){
            telemetry.addLine("Pressing X");
            position[SERVO_CONSTANTS.BALL_SENSOR_ARM_INDEX] -= SERVO_CONSTANTS.INCREMENT_VALUE;
            ballSensorArm.setPosition(position[SERVO_CONSTANTS.BALL_SENSOR_ARM_INDEX]);
        } else if(gamepad1.a){
            telemetry.addLine("Pressing A");
            position[SERVO_CONSTANTS.BALL_SENSOR_ARM_INDEX] += SERVO_CONSTANTS.INCREMENT_VALUE;
            ballSensorArm.setPosition(position[SERVO_CONSTANTS.BALL_SENSOR_ARM_INDEX]);
        }
        //endregion


        //region Some Telemetry Voodoo
        telemetry.addData("Right glyph",glyphGrabberRight.getPosition());
        telemetry.addData("Center glyph",glyphGrabberCenter.getPosition());
        telemetry.addData("Left glyph",glyphGrabberLeft.getPosition());
        telemetry.addData("ball sensor",ballSensorArm.getPosition());
        telemetry.addData("Left Motor",rearLeft.getCurrentPosition());
        telemetry.addData("Right Motor",rearRight.getCurrentPosition());
        //endregion

    }

    protected void setLeftPow(double pow) {
        rearLeft.setPower(pow * SmolBotTemplate.Constants.LEFT_SPEED);
    }
    protected void setRightPow(double pow) {
        rearRight.setPower(pow * SmolBotTemplate.Constants.RIGHT_SPEED);
    }

    protected boolean checkEncoder(int ticks) {
        int distance = Math.abs(ticks);
        int rearLeftDist = Math.abs(rearLeft.getCurrentPosition());
        int rearRightDist = Math.abs(rearRight.getCurrentPosition());

        return (distance <= rearLeftDist) || (distance <= rearRightDist);
    }
}
