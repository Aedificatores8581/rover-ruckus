package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.Tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;

@Disabled
@TeleOp(name = "Hang Lift Enc Per Inch")
public class HangLeftEncPerInchTest extends WestBot15 {
    private static final double STICK_MULT = 1;

    @Override
    public void loop() {
        lift.setPower(gamepad1.left_stick_y * STICK_MULT);

        telemetry.addLine(lift.toString());
    }
}