package org.firstinspires.ftc.teamcode;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.util.Range;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Locale;
/**
 * Created by fgpor on 3/31/2018.
 */

public abstract class MecBotTemplate extends OpMode{
    DcMotor lf, lr, rf, rr;
    public static final DcMotor.Direction LDir = DcMotorSimple.Direction.FORWARD, RDir = DcMotorSimple.Direction.REVERSE;
    public void init(){
        lf = hardwareMap.dcMotor.get("lf");
        lr = hardwareMap.dcMotor.get("lr");
        rf = hardwareMap.dcMotor.get("rf");
        rr = hardwareMap.dcMotor.get("rr");
    }

    public void start(){
        lf.setDirection(LDir);
        lr.setDirection(LDir);
        rf.setDirection(RDir);
        rr.setDirection(RDir);

    }
    public void refreshMotors(double I, double II, double III, double IV){
        lf.setPower(II);
        lr.setPower(III);
        rf.setPower(I);
        rr.setPower(IV);
    }
}
