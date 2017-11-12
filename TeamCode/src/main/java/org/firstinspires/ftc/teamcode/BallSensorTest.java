package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


@Autonomous(name = "ballSensorTest", group = "bepis")
public class BallSensorTest extends OpMode {
    /*private VuforiaLocalizer vuforia;
    private int encoderAmount;
    private final int ONE_SECOND = 1000;

    private int cameraMonitorViewId;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables relicTrackables;
    private VuforiaTrackable relicTemplate;

    private enum ROBOT_ACTIVITY_STATE {reading, moving}
    private ROBOT_ACTIVITY_STATE state;*/
    DcMotor left, right, arm, hand;
    Servo grab, finger;
    NormalizedColorSensor colorSensor;
    NormalizedRGBA colors;

    @Override
    public void init() {
        left = hardwareMap.dcMotor.get("lm");
        right = hardwareMap.dcMotor.get("rm");
        arm = hardwareMap.dcMotor.get("am");
        hand = hardwareMap.dcMotor.get("hm");
        grab = hardwareMap.servo.get("gr");
        finger = hardwareMap.servo.get("fr");
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "color sensor");
        left.setDirection(Constants.LEFT_DIR);
        right.setDirection(Constants.RIGHT_DIR);
        arm.setDirection(Constants.ARM_DIR);
        hand.setDirection(Constants.HAND_DIR);
        /*
        state = ROBOT_ACTIVITY_STATE.reading;
        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AdNikLv/////AAAAGWU+M9z3/00mqU+dDTTtHfZ20J0oyIXsfm2hNe0Oy/LXv4LbAaeEkgXQoLcO6ks5K0ixdWt+3WIRzmcncN31UCbuk1UJkfKtJ8IcaY+zBJe8jTlAyupXFBvONjLNShkis/kU0LHMVhFTgJZVCVaVWjaQ21nnfYHq9I2UNU1bq8+CHBDYD62VvGdSY4jwwJRgR4Rq+HYOpj/4m6P/XyqnDmFPWzF/V3If1FJaQj5E3ZZRm4lKSzvWhClfrdX/LwTkTpf3/j8QOJYEvhe9JkUwppMiKXp1iy/wEgNRFMjJKPLU5VtAqQYh/zsSEhfpeyryPGfU123eSJQoCQpq/f3Sjn37iR0ILx8dsnT1mlVStrm8";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

*/


    }
    public BallSensorTest() {

        // Initialize base classes.
        //
        // All via self-construction.
        //
        // Initialize class members.
        //
        // All via self-construction.

    }
    @Override
    public void start() {
        /*relicTrackables.activate();

        telemetry.addData("activated", "");

        try{
            Thread.sleep(ONE_SECOND);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        */

    }
    @Override
    public void loop(){
        colors = colorSensor.getNormalizedColors();
        double redRatio = colors.red / (colors.red + colors.blue + colors.green);
        double blueRatio = colors.blue / (colors.red + colors.blue + colors.green);



        boolean redAliance = false;
        grab.setPosition(0.385);
//        if(blueRatio < 0.4 && redRatio < 0.55 && )

        if(redAliance == true) {
            if (redRatio >= 0.55 && redRatio > blueRatio) {
                finger.setPosition(1.0);

            }
            else if (blueRatio >= 0.4 && redRatio < blueRatio) {
                finger.setPosition(0.0);
            }
            else
                finger.setPosition(0.6044444);
        }
        if(redAliance == false) {
            if (redRatio >= 0.55 && redRatio > blueRatio) {
                finger.setPosition(0.0);
            }
            else if (blueRatio >= 0.4 && redRatio < blueRatio){
                finger.setPosition(1.0);
            }
            else
                finger.setPosition(0.6044444);
        }


        telemetry.addLine()
                .addData("a", colors.alpha )
                .addData("red Ratio", (colors.red/(colors.blue + colors.red + colors.green)))
                .addData("green Ratio", (colors.green/(colors.blue + colors.red + colors.green)))
                .addData("blue Ratio", (colors.blue/(colors.blue + colors.red + colors.green)))
                .addData("blue", colors.blue)
                .addData("red", colors.red)
                .addData("finger", finger.getPosition());

        telemetry.update();
/*        RelicRecoveryVuMark vuMark;
        if(state == ROBOT_ACTIVITY_STATE.reading) {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            switch (vuMark) {
                case LEFT:
                    state = ROBOT_ACTIVITY_STATE.moving;
                    encoderAmount = 8000;
                    break;
                case CENTER:
                    state = ROBOT_ACTIVITY_STATE.moving;
                    encoderAmount = 12000;
                    break;
                case RIGHT:
                    state = ROBOT_ACTIVITY_STATE.moving;
                    encoderAmount = 16000;
                    break;
                default:
                    state = ROBOT_ACTIVITY_STATE.reading;
                    break;
            }
        } else {
            if (!checkEncoder(encoderAmount)){
                setLeftPow(1.0);
                setRightPow(1.0);
            }else{
                setLeftPow(0.0);
                setRightPow(0.0);
            }
        }
        */

        telemetry.addData("a", colors.alpha);
        telemetry.addData("red Ratio", (colors.red / (colors.blue + colors.red + colors.green)));
        telemetry.addData("green Ratio", (colors.green / (colors.blue + colors.red + colors.green)));
        telemetry.addData("blue Ratio", (colors.blue / (colors.blue + colors.red + colors.green)));
        telemetry.addData("blue", colors.blue);
        telemetry.addData("red", colors.red);
        telemetry.addData("finger", finger.getPosition());

    }

    public void stop() {
        setLeftPow(0.0);
        setRightPow(0.0);

    }

    protected void setLeftPow(double pow) {
        left.setPower(pow * Constants.LEFT_SPEED);
    }
    protected void setRightPow(double pow) {
        right.setPower(pow * Constants.RIGHT_SPEED);
    }

    protected boolean checkEncoder(int ticks) {
        int distance = Math.abs(ticks);
        int leftDist = Math.abs(left.getCurrentPosition());
        int rightDist = Math.abs(right.getCurrentPosition());

        return (distance <= leftDist) || (distance <= rightDist);
    }
}