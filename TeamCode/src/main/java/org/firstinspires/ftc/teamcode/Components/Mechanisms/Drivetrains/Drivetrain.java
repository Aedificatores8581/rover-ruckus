package org.firstinspires.ftc.teamcode.Components.Mechanisms.Drivetrains;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Components.Component;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;

/**
 * Created by Frank Portman on 5/21/2018
 */
public abstract class Drivetrain extends Component {
    public static final DcMotor.Direction FORWARD  = DcMotor.Direction.FORWARD,
                                          REVERSE  = DcMotor.Direction.REVERSE;
    public double minTurn;
    public double maxSpeed = 1;
    public Pose position;
    public enum Direction {FOR, BACK}
    public Vector2 destinationVector = new Vector2(), trajectoryVector = new Vector2();
    //initializes the motors
    public abstract void initMotors(HardwareMap map);

    //normalizes the motor values
    public abstract void normalizeMotors();

    public void resetEncoders() {}
}