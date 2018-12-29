package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Components.Sensors.Cameras.MotoG4;
import org.firstinspires.ftc.teamcode.Robots.Robot;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.Math.GyroAngles;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.opencv.core.Point3;

/**
 * Written by Theo Lovinski, 5/11/2018.
 *
 * This is a test, among many.
 */

@Autonomous(name = "ScaleCrater", group = "Auto Testing")
public class AutoScaleCraterTest extends WestBot15 {
    MotoG4 motoG4;
    GyroAngles gyroAngles;
    Orientation angle;

    private static final double ON_CRATER_RIM_THRESHOLD = 15;
    // private static final double CRATER_ANGLE_ADJUSTMENT_INCREMENT = 1;
    public boolean onCrater = false; // ABSTRACT

    @Override
    public void init() {
        angle = new Orientation();
        gyroAngles = new GyroAngles(angle);

        usingIMU = true;
        super.init();

        msStuckDetectInit = UniversalConstants.MS_STUCK_DETECT_INIT_DEFAULT;
        normalizeGyroAngleY();
        setStartAngle();
        startAngleY = getGyroAngleY();

        telemetry.addData("Start Y angle.", Math.abs(normalizeGyroAngleY()));
        telemetry.addData("onCrater?", onCrater);

        motoG4 = new MotoG4();
        motoG4.setLocationAndOrientation(
                new Point3(0, 0, 12),
                new Point3(0, 0, 0)
        );
    }

    @Override
    public void start() { super.start(); }

    @Override
    public void loop() {

        if (Math.abs(normalizeGyroAngleY()) > ON_CRATER_RIM_THRESHOLD) {
            onCrater = true;
        } else {
            onCrater = false;
        }

        if (!onCrater) {
            drivetrain.setRightPow(0.4);
            drivetrain.setLeftPow(0.4);
        } else {
            // Stopped
            drivetrain.setRightPow(0.0);
            drivetrain.setLeftPow(0.0);
		}

        drivetrain.updateEncoders();

        telemetry.addData("Rim Threshold", ON_CRATER_RIM_THRESHOLD);
        telemetry.addData("onCrater?", onCrater);
        telemetry.addData("Robot Y", Math.abs(normalizeGyroAngleY()));
        //telemetry.addData("Robot X", getGyroAngleX());
        //telemetry.addData("Robot Z", getGyroAngle());
    }
}
