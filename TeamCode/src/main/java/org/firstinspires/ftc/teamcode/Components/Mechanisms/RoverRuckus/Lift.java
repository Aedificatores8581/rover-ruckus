package org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Components.Sensors.TouchSensor;

public class Lift {
    public DcMotor liftMotor;
    public TouchSensor topTouchSensor = new TouchSensor(), bottomTouchSensor = new TouchSensor();

    private boolean hasInit;

    // TODO: Find value for constant
    private static final double ENC_PER_INCH = 432;

    public void init(HardwareMap hardwareMap) {
        liftMotor = hardwareMap.dcMotor.get("hangLift");
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        topTouchSensor.init(hardwareMap, "top");
        bottomTouchSensor.init(hardwareMap, "bot");
        hasInit = true;
    }

    public void setPower(double pow) {
        if((pow < 0 && topPressed()) || (pow > 0 && bottomPressed()))
            this.liftMotor.setPower(0);
        else
            this.liftMotor.setPower(pow);

    }


    public boolean topPressed() { return this.topTouchSensor.isPressed();}

    public boolean bottomPressed() { return this.bottomTouchSensor.isPressed();}


    public double getHeight() {
        if (!this.hasInit) {
            return Double.NaN;
        }

        return liftMotor.getCurrentPosition() / ENC_PER_INCH;
    }

    public String toString(){
        return this.getHeight() + " inches upwards\n" +
                this.liftMotor.getCurrentPosition() + " encoder ticks\n" +
                "Top Sense: " + this.topTouchSensor.toString() +
                "\nBot Sense: " + this.bottomTouchSensor.toString();
    }
}
