package org.firstinspires.ftc.teamcode.Robots.WestBot15.OpModes.RoverRuckus;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains.TankDrivetrains.TankDT;
import org.firstinspires.ftc.teamcode.Components.Mechanisms.RoverRuckus.Lift;
import org.firstinspires.ftc.teamcode.Robots.WestBot15.WestBot15;
import org.firstinspires.ftc.teamcode.Universal.Map.AttractionField;
import org.firstinspires.ftc.teamcode.Universal.Map.SolanoidalField;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;
import org.firstinspires.ftc.teamcode.Universal.UniversalFunctions;
import org.firstinspires.ftc.teamcode.Vision.Detectors.BlockDetector;

import ftc.vision.Detector;

public class CraterAutoCycle extends WestBot15 {
    CraterAutoCycleState autoState = CraterAutoCycleState.LAND;
    double startTime = 0;
    double prevLeft, prevRight;
    BlockDetector detector;
    Vector2 sampleVect;
    public void init(){
        super.init();
        detector = new BlockDetector();
        detector.opState = Detector.OperatingState.TUNING;
        FtcRobotControllerActivity.frameGrabber.detector = detector;
        drivetrain.controlState = TankDT.ControlState.FIELD_CENTRIC;
        drivetrain.direction = TankDT.Direction.BACK;
        drivetrain.leftFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivetrain.leftFore.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drivetrain.leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivetrain.leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drivetrain.rightFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivetrain.rightFore.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        drivetrain.rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        drivetrain.rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void start(){
        super.start();
        startTime = UniversalFunctions.getTimeInSeconds();
    }

    public void loop(){

        switch(autoState) {
            case LAND:
                aextendo.articulateDown();
                lift.ratchetState = Lift.RatchetState.UP;
                lift.switchRatchetState();
                lift.liftMotor.setPower(0.3);
                if (true/*top.isPressed()*/) {
                    lift.liftMotor.setPower(0);
                    autoState = CraterAutoCycleState.SAMPLE;
                    startTime = UniversalFunctions.getTimeInSeconds();
                }
                break;
            case SAMPLE:
                drivetrain.maxSpeed = 0.4;

                Vector2 temp = new Vector2(-detector.element.x, detector.element.y);
                temp.x += 640/ 2;
                temp.y -= 480 / 2;

                double vertAng = temp.y / 480 * motoG4.rearCamera.horizontalAngleOfView();
                double horiAng = temp.x / 640 * motoG4.rearCamera.verticalAngleOfView();

                double newY = (motoG4.getLocation().z - 2 / 2) / Math.tan(-vertAng - 0.364773814);
                double newX = newY * Math.tan(horiAng);
                newY *= -1;
                if(UniversalFunctions.getTimeInSeconds() - startTime < 1) {
                    sampleVect = new Vector2(newX + motoG4.getLocation().x, newY + motoG4.getLocation().y);
                }
                if (UniversalFunctions.getTimeInSeconds() - startTime > 1) {

                    drivetrain.updateEncoders();
                    double leftChange = drivetrain.averageLeftEncoders() - prevLeft;
                    double rightChange = drivetrain.averageRightEncoders() - prevRight;
                    drivetrain.updateLocation(leftChange, rightChange);
                    prevLeft = drivetrain.averageLeftEncoders();
                    prevRight = drivetrain.averageRightEncoders();
                    setRobotAngle();

                    Vector2 newVect = new Vector2(sampleVect.x, sampleVect.y);
                    newVect.x -= drivetrain.position.x;
                    newVect.y -= drivetrain.position.y;
                    Vector2 temp2 = new Vector2(newVect.x, newVect.y);

                    if(robotAngle.angleBetween(newVect) > Math.PI / 12) {
                        if (newVect.magnitude() > 12)
                            newVect.setFromPolar(1, newVect.angle());
                        else
                            newVect.scalarMultiply(1.0 / 12);
                        drivetrain.teleOpLoop(newVect, new Vector2(), robotAngle);
                        drivetrain.setLeftPow();
                        drivetrain.setRightPow();
                    }
                    else{
                        drivetrain.setLeftPow(0);
                        drivetrain.setRightPow(0);
                        double desiredExtensionLength = newVect.magnitude()- aextendo.EXTENSION_OFFSET + aextendo.getArticulatorLength();
                        if(UniversalFunctions.withinTolerance(aextendo.getTotalExtensionLength(), desiredExtensionLength, -2, 1)) {
                            aextendo.aextendTM(desiredExtensionLength);
                            intaek.setPower(1);
                        }
                        else{
                            aextendo.aextendTM(0);
                            intaek.setPower(0);
                            if(aextendo.isRetracted())
                                autoState = CraterAutoCycleState.RETURN;
                        }
                    }
                }
                break;
            case RETURN:
                SolanoidalField field = new SolanoidalField();
                field.location = new Pose(0, 0, -Math.PI / 2);

                break;
        }
    }

    enum CraterAutoCycleState{
        LAND, SAMPLE, TURNTOCRATER, INTAKE, RETURN, DISPENSE
    }
}
