package org.firstinspires.ftc.teamcode;

/*
 * Created by Mister-Minister-Master on 11/1/2017.
 */

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/* This program detects one of the three VuMark Picture, and based on the picture, will move a set distance.
 *      1. If pictureState is set to PictureState.LEFT, then it will move (X + a constant) encoder ticks
 *      2. If the same variable is set to PictureState.CENTER, then it will move (X + 2 times a constant)encoder ticks
 *      3. If the same variable is set to PicutreState.RIGHT, then it will move (X + 3 times a constant)encoder ticks
 */

@TeleOp(name = "SensorBot: Vuforia Test", group = "anti-bepis")
public class PictureDetectionTest extends OpMode {

    private VuforiaLocalizer vuforia;
    private DcMotor left, right;
    private int encoderAmount;
    private final int ONE_SECOND = 1000;

    private int cameraMonitorViewId;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;

    private enum ROBOT_ACTIVITY_STATE {reading, moving}
    private ROBOT_ACTIVITY_STATE state;

    @Override public void init(){
        state = ROBOT_ACTIVITY_STATE.reading;

        left = hardwareMap.dcMotor.get("lm");
        right = hardwareMap.dcMotor.get("rm");

        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AdNikLv/////AAAAGWU+M9z3/00mqU+dDTTtHfZ20J0oyIXsfm2hNe0Oy/LXv4LbAaeEkgXQoLcO6ks5K0ixdWt+3WIRzmcncN31UCbuk1UJkfKtJ8IcaY+zBJe8jTlAyupXFBvONjLNShkis/kU0LHMVhFTgJZVCVaVWjaQ21nnfYHq9I2UNU1bq8+CHBDYD62VvGdSY4jwwJRgR4Rq+HYOpj/4m6P/XyqnDmFPWzF/V3If1FJaQj5E3ZZRm4lKSzvWhClfrdX/LwTkTpf3/j8QOJYEvhe9JkUwppMiKXp1iy/wEgNRFMjJKPLU5VtAqQYh/zsSEhfpeyryPGfU123eSJQoCQpq/f3Sjn37iR0ILx8dsnT1mlVStrm8";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

    }

    public void start(){
        relicTrackables.activate();

        telemetry.addData("activated", "");

        try{
            Thread.sleep(ONE_SECOND);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override public void loop(){

        RelicRecoveryVuMark vuMark;
        if(state == ROBOT_ACTIVITY_STATE.reading) {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            switch (vuMark) {
                case LEFT:
                    state = ROBOT_ACTIVITY_STATE.moving;
                    encoderAmount = 30;
                    break;
                case CENTER:
                    state = ROBOT_ACTIVITY_STATE.moving;
                    encoderAmount = 60;
                    break;
                case RIGHT:
                    state = ROBOT_ACTIVITY_STATE.moving;
                    encoderAmount = 90;
                    break;
                default:
                    state = ROBOT_ACTIVITY_STATE.reading;
                    break;
            }
        } else {
            if (!checkEncoder(encoderAmount)){
                setLeftPow(1.0);
                setRightPow(1.0);
            }
        }
    }

    public void stop(){
        setLeftPow(0.0);
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        setRightPow(0.0);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private void setLeftPow(double pow){
        left.setPower(pow * Constants.LEFT_SPEED);
    }

    private void setRightPow(double pow){
        right.setPower(pow * Constants.RIGHT_SPEED);
    }

    private boolean checkEncoder(int ticks) {
        int distance = Math.abs(ticks);
        int leftDist = Math.abs(left.getCurrentPosition());
        int rightDist = Math.abs(right.getCurrentPosition());

        return (distance <= leftDist) || (distance <= rightDist);
    }

}
