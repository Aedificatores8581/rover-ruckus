package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;


/**
 * Created by The Saminator on 06-29-2017.
 */
public abstract class RevBotTemplate extends OpMode {
    DcMotor motor;
    CRServo crs1, crs2;

    @Override
    public void init() {
        motor = hardwareMap.dcMotor.get("motor");
        crs1 = hardwareMap.crservo.get("crs1");
        crs2 = hardwareMap.crservo.get("crs2");

        motor.setDirection(DcMotor.Direction.FORWARD);
        crs1.setDirection(DcMotorSimple.Direction.FORWARD);
        crs1.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void stop() {
        motor.setPower(0.0);
        crs1.setPower(0.0);
        crs2.setPower(0.0);
    }

}
