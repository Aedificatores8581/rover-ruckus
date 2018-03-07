package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.ArithmeticException;

/**
 * Created by Hunter Seachrist on 3/2/2018.
 */

@Autonomous(name = "Smoothe Drive Test", group = "bepis")
public class SmootheDriveTest extends Sensor2BotTemplate{
    private int totalEncoders;
    private double percentTimeChangingSpeedPerState;
    private double maxPower;
    private double power;
    private int encodersSpentChanging;
    private final double INITIAL_SPEED_FACTOR = 0.2; // As a factor of maxPower;
    double acceleration;


    private enum SmootheDriveState{
        SPEED_UP,
        MAX_SPEED,
        SLOW_DOWN,
        STOP
    }

    private SmootheDriveState smootheDriveState;


    public void init(){
        super.init();
        setLeftPow(0);
        setRightPow(0);
        lm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        rm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        totalEncoders = 1000;
        percentTimeChangingSpeedPerState = 0.2;
        maxPower = 0.3;
        smootheDriveState = SmootheDriveState.SPEED_UP;

    }

    public void init_loop(){
        if (gamepad1.left_bumper){
            totalEncoders -= 1;
        } else if(gamepad1.right_bumper){
            totalEncoders += 1;
        }

        if (gamepad1.dpad_down){
            percentTimeChangingSpeedPerState -= 0.005;
        } else if(gamepad1.dpad_up){
            percentTimeChangingSpeedPerState += 0.005;
        }

        if (gamepad1.dpad_left){
            maxPower -= 0.005;
        } else if (gamepad1.dpad_right){
            maxPower += 0.005;
        }

        try {
            acceleration = (maxPower / (totalEncoders * percentTimeChangingSpeedPerState));
        } catch(ArithmeticException e){
            telemetry.addLine("\t\tCaught Exception: " + e.getStackTrace() +
                    "\n\t\tDon't set % of Travel time changing the speed = 0" +
                    "\n\t\tYour Impaling the very fabric of the Universe!");
            percentTimeChangingSpeedPerState = 0.005;
        }

        telemetry.addData("Total Encoders Travelled (left and right bumper)", totalEncoders);
        telemetry.addData("% of Travel time changing the speed (DPad Up + Down)", percentTimeChangingSpeedPerState);
        telemetry.addData("Maximum Speed (DPad Left + Right)", maxPower);
        telemetry.addData("Acceleration", acceleration);

    }

    public void start(){
        encodersSpentChanging = (int)(percentTimeChangingSpeedPerState * totalEncoders);
    }

    public void loop(){

        switch(smootheDriveState){
            case SPEED_UP:
                power = (((1 - INITIAL_SPEED_FACTOR) * maxPower) / (totalEncoders * percentTimeChangingSpeedPerState))
                        * lm.getCurrentPosition() + (INITIAL_SPEED_FACTOR * maxPower);

                if(lm.getCurrentPosition() >= encodersSpentChanging){
                    smootheDriveState = SmootheDriveState.MAX_SPEED;
                }
                break;
            case MAX_SPEED:
                power = maxPower;

                if(lm.getCurrentPosition() >= (totalEncoders-encodersSpentChanging)){
                    smootheDriveState = SmootheDriveState.SLOW_DOWN;
                }
                break;
            case SLOW_DOWN:
                power = -(maxPower/(totalEncoders * percentTimeChangingSpeedPerState))
                        * (lm.getCurrentPosition() - (totalEncoders - (encodersSpentChanging))) + maxPower;

                if(lm.getCurrentPosition() >= totalEncoders){
                    smootheDriveState = SmootheDriveState.STOP;
                }
                break;
            case STOP:
                power = 0;
                break;
        }

        setRightPow(power);
        setLeftPow(power);

        telemetry.addData("State", smootheDriveState);
        telemetry.addData("Motor Power", power);
        telemetry.addData("Right Distance: ", rm.getCurrentPosition());
        telemetry.addData("Left Distance: ", lm.getCurrentPosition());

    }

    private double capPower(double pow){
        if (pow > maxPower){
            return maxPower;
        }else{
            return pow;
        }
    }

    public void stop(){
        super.stop();
    }


}
