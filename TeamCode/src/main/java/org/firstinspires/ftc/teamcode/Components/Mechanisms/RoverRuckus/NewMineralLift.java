package org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Components.Sensors.MotorEncoder;
import org.firstinspires.ftc.teamcode.Components.Sensors.TouchSensor;
import org.firstinspires.ftc.teamcode.Universal.UniversalConfig;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;
import org.firstinspires.ftc.teamcode.Vision.UniversalVision;

public class NewMineralLift {

    // region Fields
    public DcMotor liftMotor;
    private MotorEncoder liftEncoder;

    private MineralLiftState mineralLiftState;

    // No point in implementing methods in this class for the sake of making this field private
    // Even though that is exactly what I do with MotorEncoder
    public MineralContainer mineralContainer = new MineralContainer();

    private Servo pivots[];
    public Servo pivot1, pivot2;
    private TouchSensor topLimitSwitch = new TouchSensor(), botLimitSwitch = new TouchSensor();
    public boolean canSetPowerPositive = true, canSwitchTime = false;
    public boolean mineral_lift_stuck = false;
    private boolean automationAllowed = true;
    double prevTime = 0;
    // endregion

    // region Constants
    public static final double LIFT_MOTOR_UP = 1;
    public static final double LIFT_MOTOR_DOWN = -0.4 ;

    public static final double PIVOT_TELE_FORWARD_POS = 0.98;
    public static final double PIVOT_TELE_UP_POS = 0.3;
    public static final double PIVOT_TELE_DOWN_POS = 1;
    public static double ServoAdjust = .01;

    // endregion

    public void init(HardwareMap hardwareMap){
        liftMotor = hardwareMap.dcMotor.get(UniversalConfig.MINERAL_LIFT_MOTOR);
        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        liftEncoder = new MotorEncoder(liftMotor);

        pivots = new Servo[2];
        pivot1 = hardwareMap.servo.get(UniversalConfig.MINERAL_LIFT_PIVOT[0]);
        pivot2 = hardwareMap.servo.get(UniversalConfig.MINERAL_LIFT_PIVOT[1]);
        pivot1.setPosition(PIVOT_TELE_DOWN_POS);
        pivot2.setPosition(PIVOT_TELE_DOWN_POS);
        mineralContainer.init(hardwareMap);

        topLimitSwitch.init(hardwareMap, UniversalConfig.MINERAL_LIFT_TOP_LIMIT);
        botLimitSwitch.init(hardwareMap, UniversalConfig.MINERAL_LIFT_BOT_LIMIT);

        mineralLiftState = MineralLiftState.DONE_LOWERING;
    }

    // region Lift Motor
    public synchronized void setLiftPower(double value){
        if(UniversalFunctions.getTimeInSeconds() - prevTime > 0.7) {
            if(botLimitSwitch.isPressed() && !mineral_lift_stuck) {
                value = UniversalFunctions.clamp(0, value, 1);
                pivot1.setPosition(PIVOT_TELE_FORWARD_POS);
                pivot2.setPosition(PIVOT_TELE_FORWARD_POS);
                mineralContainer.articulateFront(mineralContainer.FRONT_UP_POSITION);
                prevTime = UniversalFunctions.getTimeInSeconds();
            }
            if (topLimitSwitch.isPressed() && !mineral_lift_stuck)
                value = UniversalFunctions.clamp(-1, value, 0);
            if(!botLimitSwitch.isPressed() && !topLimitSwitch.isPressed() && canSetPowerPositive) {
                pivot1.setPosition(PIVOT_TELE_DOWN_POS);
                pivot2.setPosition(PIVOT_TELE_DOWN_POS);
                mineralContainer.articulateFront(mineralContainer.FRONT_DOWN_POSITION);
            }
            if (topLimitSwitch.isPressed()) {
                pivot1.setPosition(PIVOT_TELE_UP_POS);
                pivot2.setPosition(PIVOT_TELE_UP_POS);
                prevTime = UniversalFunctions.getTimeInSeconds();
                canSetPowerPositive = false;
            }
            else if (value < 0)
                canSetPowerPositive = true;
            liftMotor.setPower(value);
        }
        else
            liftMotor.setPower(value);
    }

    public synchronized void initEncoder(){
        liftEncoder.initEncoder();
    }

    public int getLiftEncoder() {
        liftEncoder.updateEncoder();
        return liftEncoder.currentPosition;
    }

    public synchronized void resetLiftEncoder() {
        liftEncoder.resetEncoder();
    }

    public synchronized void hardResetLiftEncoer() {
        liftEncoder.hardResetEncoder();
    }
    // endregion

    // region Pivots
    public synchronized void articulatePivots(double value){
        pivot1.setPosition(value);
        pivot2.setPosition(value);
    }

    // Returns only one of the pivot positions, since the to pivot servos should be the same
    public double getPivotPosition(){
        return pivot1.getPosition();
    }
    public synchronized void articulateUp(){
        articulatePivots(PIVOT_TELE_UP_POS);
    }
    public synchronized void articulateDown(){
        articulatePivots(PIVOT_TELE_DOWN_POS);
    }
    //endregion

    public MineralLiftState getMineralLiftState() {
        return mineralLiftState;
    }

    // Used to make sure we don't try to raise and lower the intake at the same time
    public boolean isMovingLift() {
        return !(getMineralLiftState() == MineralLiftState.DONE_LOWERING ||
                getMineralLiftState() == MineralLiftState.DONE_RAISING);
    }

    public MineralLiftState setMineralLiftState(MineralLiftState value) {
        mineralLiftState = value;
        return mineralLiftState;
    }

    public boolean isTopPushed () {
        if (topLimitSwitch.isPressed()) return true;
        else return false;
    }

    public boolean isbottomPushed () {
        if (botLimitSwitch.isPressed()) return true;
        else return false;
    }
    public synchronized void automatedMineralLift() {
        if(automationAllowed) {
                switch (mineralLiftState) {
                    case EXTEND_LIFT:
                        mineralContainer.articulateFront(mineralContainer.FRONT_DOWN_POSITION);
                        setLiftPower(LIFT_MOTOR_UP);
                        if (topLimitSwitch.isPressed()) {
                            setLiftPower(0.0);
                            mineralLiftState = MineralLiftState.DONE_RAISING;
                        }
                        break;
                    case DONE_RAISING:
                        break;
                    case ARTICULATE_PIVOTS_DOWN:
                        articulatePivots(getPivotPosition() + ServoAdjust);
                        if (getPivotPosition() >= PIVOT_TELE_DOWN_POS) {
                            mineralLiftState = MineralLiftState.RETRACT_LIFT;
                        }
                        break;
                    case RETRACT_LIFT:
                        setLiftPower(LIFT_MOTOR_DOWN);
                        if (botLimitSwitch.isPressed()) {
                            mineralLiftState = MineralLiftState.DONE_LOWERING;
                            setLiftPower(0.0);
                        }
                        break;
                    case DONE_LOWERING:
                        break;
                }
            }
    }

    public void allowAutomation(boolean val) {
        automationAllowed = val;
    }

    public boolean isAutomationAllowed() {
        return automationAllowed;
    }

}

