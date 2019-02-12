package org.firstinspires.ftc.teamcode.Robots.WestBot15;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.WestCoast15;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.AExtendotm;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Intake;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Lift;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.MineralContainer;
import org.firstinspires.ftc.teamcode.Components.Sensors.Cameras.MotoG4;
import org.firstinspires.ftc.teamcode.Robots.Robot;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.opencv.core.Point3;

/**
 * Created by Frank Portman on 6/1/2018
 */

public abstract class WestBot15 extends Robot {
    public Servo maerkrLeft;
    public Servo maerkrRight;

    public boolean isAutonomous = false;
    //IMPORTANT: phone locations should be taken in relation to the robot, not the field

    public Intake intaek = new Intake();
    public Lift lift = new Lift();
    public AExtendotm aextendo = new AExtendotm();
    protected WestCoast15 drivetrain = new WestCoast15(DcMotor.ZeroPowerBehavior.FLOAT, 1.0);
    public final static boolean HADLEY_ON_SCHEDULE = false;
    public MotoG4 motoG4 = new MotoG4();
    public MineralContainer mineralContainer = new MineralContainer();
    public final double MARKER_OPEN_POSITION = 0.5, MARKER_CLOSED_POSITION = 1;

    @Override
    public void init(){
        msStuckDetectInit = UniversalConstants.MS_STUCK_DETECT_INIT_DEFAULT;
        super.init();

        maerkrLeft = hardwareMap.servo.get("lmrkr");
        maerkrRight = hardwareMap.servo.get("rmrkr");

        maerkrLeft.setPosition(UniversalConstants.MarkerServoConstants.LEFT_CLOSED.getPos());
        maerkrRight.setPosition(UniversalConstants.MarkerServoConstants.RIGHT_CLOSED.getPos());

        drivetrain.maxSpeed = 1.0;
        drivetrain.initMotors(hardwareMap);
        drivetrain.position = new Pose();

        motoG4 = new MotoG4();
        motoG4.setLocationAndOrientation(new Point3(0, 3.715263697 + 5.275000000 / 2 + Math.sin(Math.toDegrees(37)) * 3.02 + 7.5/25.4, 14.92590572034875), new Point3(-Math.toRadians(37), -Math.PI/2, 0));


        intaek.init(hardwareMap);
        lift.init(hardwareMap);
        maerkrLeft = hardwareMap.servo.get("mrkr");
        maerkrLeft.setPosition(MARKER_CLOSED_POSITION);

        aextendo.init(hardwareMap, false);
        //mineralContainer.init(hardwareMap);
    }

    @Override
    public void start(){
        super.start();
    }

    @Override
    public double getGyroAngle(){
        if (!usingIMU) {
            return startAngle + (drivetrain.averageRightEncoders() - drivetrain.averageLeftEncoders()) / drivetrain.ENC_PER_INCH / drivetrain.DISTANCE_BETWEEN_WHEELS;
        }

        return super.getGyroAngle();
    }

    public enum AutoState{
        HANG,
        LOWER,
        SAMPLE,
        DOUBLE_SAMPLE,
        CYCLE,
        CLAIM,
        PARK,
        FORWARD
    }
}

class MaerkrConstants {
    public static double LEFT_SERVO_ENGAGED    = 1.0;
    public static double LEFT_SERVO_DISENGAGED = -1.0;
    public static double RIGHT_SERVO_ENGAGED   = -1.0;
    public static double RIGHT_SERVO_DISENGAGED= 1.0;
}