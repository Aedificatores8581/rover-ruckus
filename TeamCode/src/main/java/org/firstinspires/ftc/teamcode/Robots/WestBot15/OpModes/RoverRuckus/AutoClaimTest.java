package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Components.Sensors.Cameras.MotoG4;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;

public class AutoClaimTest extends WestBot15 {
    Servo claimDropServo;

    private static final double DISPENSE_POSITION = 0.3;

    public void init() {
        claimDropServo = hardwareMap.servo.get("cds");

        usingIMU = true;
        super.init();

        msStuckDetectInit = UniversalConstants.MS_STUCK_DETECT_INIT_DEFAULT;
    }

    public void start() { super.start(); }

    public void loop() {
        if (gamepad1.right_bumper || gamepad1.left_bumper) {
            claimDropServo.setPosition(DISPENSE_POSITION);
        }
    }
}
