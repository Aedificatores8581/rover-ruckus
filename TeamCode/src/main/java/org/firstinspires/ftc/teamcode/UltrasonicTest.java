package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "SensorBot: Ultrasonic Sensor Test", group = "bepis")
public class UltrasonicTest extends Sensor2BotTemplate {



    public void init() {
        super.init();
    }

    public void loop() {

        if (ods == null)
            telemetry.addData("ERROR ERROR ERROR", "ULTRASONIC SENSOR IS NULL THIS IS VERY BAD REQUESTING TO ABORT REPEAT REQUESTING TO ABORT");
        else {
            try {
                telemetry.addData("Light", ultraSonic.getUltrasonicLevel());
            } catch(Exception e) {
                telemetry.addLine(e.toString());
            }
        }

    }
}
