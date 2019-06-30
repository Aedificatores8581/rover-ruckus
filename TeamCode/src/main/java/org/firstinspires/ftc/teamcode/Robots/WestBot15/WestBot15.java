package org.firstinspires.ftc.teamcode.Robots.WestBot15;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.Drivetrain;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.WestCoast15;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.AExtendotm;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Intake;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Lift;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.MineralContainer;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.NewMineralLift;
import org.firstinspires.ftc.teamcode.Components.Sensors.Cameras.MotoG4;
import org.firstinspires.ftc.teamcode.Robots.Robot;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;
import org.opencv.core.Point3;

/**
 * Created by Frank Portman on 6/1/2018
 */

public abstract class WestBot15 extends Robot {
    public Servo maerkrLeft;

    public boolean isAutonomous = false;
    //IMPORTANT: phone locations should be taken in relation to the robot, not the field

    public Intake intaek = new Intake();
    public Lift lift = new Lift();
    public AExtendotm aextendo = new AExtendotm();
    protected WestCoast15 drivetrain = new WestCoast15(DcMotor.ZeroPowerBehavior.FLOAT, 1.0);
    public NewMineralLift mineralLift = new NewMineralLift();
    public final static boolean HADLEY_ON_SCHEDULE = true;
    public MotoG4 motoG4 = new MotoG4();
    public final double MARKER_OPEN_POSITION = 0.5, MARKER_CLOSED_POSITION = 1;
    public MineralContainer mineralContainer = new MineralContainer();

    public Vector2 intakePosition = new Vector2();
    @Override
    public void init(){
        msStuckDetectInit = UniversalConstants.MS_STUCK_DETECT_INIT_DEFAULT;
        super.init();


        mineralContainer.init(hardwareMap);
        drivetrain.maxSpeed = 1.0;
        drivetrain.initMotors(hardwareMap);
        drivetrain.position = new Pose();

        motoG4 = new MotoG4();
        motoG4.setLocationAndOrientation(new Point3(0, 3.715263697 + 5.275000000 / 2 + Math.sin(Math.toDegrees(37)) * 3.02 + 7.5/25.4, 14.92590572034875), new Point3(-Math.toRadians(37), -Math.PI/2, 0));


        intaek.init(hardwareMap);
        lift.init(hardwareMap);
        maerkrLeft = hardwareMap.servo.get("mrkr");
        aextendo.init(hardwareMap, false);
        //mineralContainer.init(hardwareMap);
        mineralLift.init(hardwareMap);
        if (HADLEY_ON_SCHEDULE) {
            aextendo.init(hardwareMap, false);
            //mineralContainer.init(hardwareMap);
        }
        maerkrLeft.setPosition(0.9);
        mineralContainer.articulateBack(mineralContainer.BACK_CLOSED_POSITION);

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

    public void aextendIntake(Vector2 destination, Vector2 angle, double threshold) {

        double extensionDistance = aextendo.getExtensionLength() + 16;
        intakePosition = new Vector2();
        intakePosition.setFromPolar(extensionDistance, robotAngle.angle() + Math.PI / 2);

        intakePosition.x -= 0.455 * Math.sin(intakePosition.angle());
        intakePosition.y += 0.455 * Math.cos(intakePosition.angle());
        Vector2 vector = new Vector2(destination.x - drivetrain.position.x + 0.455 * Math.sin(intakePosition.angle()), destination.y - drivetrain.position.y - 0.455 * Math.cos(intakePosition.angle()));

        if (vector.magnitude() > threshold)
            vector.setFromPolar(1, vector.angle());
        else
            vector.scalarMultiply(1.0 / threshold);
        double angleBetween = UniversalFunctions.normalizeAngleRadians(vector.angle(), angle.angle());
        double tempTurnMult = 0;
        double directionMult = 1;
        if (Math.sin(angleBetween) < 0) {
            drivetrain.setLeftPow(Math.cos(angleBetween) < 0 ? -1 : 1);
            drivetrain.setRightPow(Math.cos(angleBetween) > 0 ? -1 : 1);
        } else {
            double cos = Math.cos(angleBetween);
            tempTurnMult = Math.abs(cos) + 1;
            drivetrain.rightPow = directionMult * (0.0 * vector.magnitude() - tempTurnMult * drivetrain.turnMult * cos);
            drivetrain.leftPow = directionMult * (0.0 * vector.magnitude() + tempTurnMult * drivetrain.turnMult * cos);
            aextendo.aextendTM(vector.magnitude() * Math.signum(UniversalFunctions.normalizeAngle180Radians(vector.angle())));
            drivetrain.setLeftPow();
            drivetrain.setRightPow();
        }
    }

    @Override
    public void stop() {
        super.stop();
        mineralLift.allowAutomation(false);
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