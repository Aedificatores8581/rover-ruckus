package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;


@TeleOp(name = "ballSensorTele", group = "bepis")
public class BallSensorTele extends OpMode {
    DcMotor left, right;
    Servo grab, finger;
    NormalizedColorSensor colorSensor;
    NormalizedRGBA colors;

    @Override
    public void init() {
        left = hardwareMap.dcMotor.get("lm");
        right = hardwareMap.dcMotor.get("rm");
        grab = hardwareMap.servo.get("gr");
        finger = hardwareMap.servo.get("fr");
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "color sensor");
        left.setDirection(Constants.LEFT_DIR);
        right.setDirection(Constants.RIGHT_DIR);
        grab.setDirection(Servo.Direction.REVERSE);


//        finger.scaleRange(0.0, 1.0);


    }

    public BallSensorTele() {

        // Initialize base classes.
        //
        // All via self-construction.
        //
        // Initialize class members.
        //
        // All via self-construction.


    }

    @Override
    public void start() {





    }
    double servoPosition = .5;
    double servoPosition1 = 0.17;
    double max = 1.0;
    double min = 0.0;
    double counter1 = 0.0;
    double counter2 = 0.0;
    @Override

    public void loop(){

/*        while (gamepad1.a == true) {
            finger.setPosition(servoPosition + 0.05 * counter2);
            counter2++;
        }
        if(gamepad1.y)
            max = servoPosition + .05 * counter2;
        while (gamepad1.b == true) {
            finger.setPosition(servoPosition - 0.05 * counter1);
            counter1++;
        }
        if(gamepad1.x)
            min = servoPosition - .05 * counter1;
            */
        if (gamepad1.a){
            servoPosition += .001;
            if (servoPosition >= max)
                servoPosition = max;

            finger.setPosition(servoPosition);
        }
        else if (gamepad1.b){
            servoPosition -= .001;
            if (servoPosition <= min) {
                servoPosition = min;
            }
            finger.setPosition(servoPosition);
        }
        else if(gamepad1.x){
            servoPosition1 += .001;
            if (servoPosition1 >= max) {
                servoPosition1 = max;
            }
            grab.setPosition(servoPosition1);
        }
        else if(gamepad1.y){
            servoPosition1 -= .001;
            if (servoPosition1 <= min) {
                servoPosition1 = min;
            }
            grab.setPosition(servoPosition1);
        }


        telemetry.addLine()
                .addData("finger", finger.getPosition())
                .addData("servoposition", servoPosition)
                .addData("servoposition1", servoPosition1)

                .addData("min", min)
                .addData("max", max);
        telemetry.update();
    }

    }

