 * This program detects one of the three VuMark Picture, and based on the picture, will move a set distance.
 *      1. If pictureState is set to PictureState.LEFT, then it will move (X + a constant) encoder ticks
 *      2. If the same variable is set to PictureState.CENTER, then it will move (X + 2 times a constant)encoder ticks
 *      3. If the same variable is set to PicutreState.RIGHT, then it will move (X + 3 times a constant)encoder ticks
 */

@TeleOp(name = "SensorBot: Vuforia Test", group = "anti-bepis")
public class PictureDetectionTest extends OpMode{

    VuforiaLocalizer vuforia;
    DcMotor left, right;
    int encoderAmount;

    int cameraMonitorViewId;
    VuforiaLocalizer.Parameters parameters;
    VuforiaTrackables relicTrackables;
    VuforiaTrackable relicTemplate;

    enum ROBOT_ACTIVITY_STATE {reading, moving};
    ROBOT_ACTIVITY_STATE state;

    @Override public void init(){
        state = ROBOT_ACTIVITY_STATE.reading;

        left = hardwareMap.dcMotor.get("lm");
        right = hardwareMap.dcMotor.get("rm");

        cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "NOT PRESENT FOR GIT REPOSITORY";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

    }

    public void start(){
        relicTrackables.activate();
        telemetry.addData("activated", "");
    }

    @Override public void loop(){
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);

        if(state == ROBOT_ACTIVITY_STATE.moving) {
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
