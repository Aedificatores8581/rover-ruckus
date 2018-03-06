package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Conjured into existence by The Saminator on 10-01-2017.
 */
@TeleOp(name = "DriveBot Multi-thread Tele-Op", group = "this is a test")
public class DriveBotTestTeleopMulti extends DriveBotTestTemplate {
    public class InputHandlerThread extends Thread {
        @Override
        public void run() {
            while (!Thread.interrupted()) {

                // Reset encoders
                if (gamepad1.left_stick_button)
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            leftFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rightFore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rightFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                            leftFore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                            leftRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rightRear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                            rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                            leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        }
                    });

                // Move glyph intake and belt
                if (triggered(gamepad1.right_trigger))
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            succ(1.0);
                            belt(0.5);
                        }
                    });
                else if (triggered(gamepad1.left_trigger))
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            succ(-1.0);
                            belt(-0.5);
                        }
                    });
                else
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            succ(0.0);
                            belt(0.0);
                        }
                    });

                // Gear shift
                if (gamepad1.x && !prev1.x)
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            toggleSpeed();
                        }
                    });

                // Move jewel arm down
                if (gamepad1.dpad_down)
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            jewelArmServoValue -= 0.01;
                            clampJewelArmServo();
                        }
                    });

                // Move jewel arm up
                if (gamepad1.dpad_up)
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            jewelArmServoValue += 0.01;
                            clampJewelArmServo();
                        }
                    });

                // Move glyph dumper up
                if (gamepad2.dpad_up || gamepad2.y)
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            glyphDumpServoValue += 0.05;
                            dumpServoManual = true;
                            clampDumpServo();
                        }
                    });

                // Move glyph dumper down
                if (gamepad2.dpad_down || gamepad2.a)
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            glyphDumpServoValue -= 0.05;
                            dumpServoManual = true;
                            clampDumpServo();
                        }
                    });

                // Move glyph dumper level
                if (gamepad2.dpad_left || gamepad2.dpad_right || gamepad2.x)
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            glyphDumpServoValue = 0.42;
                            dumpServoManual = true;
                            clampDumpServo();
                        }
                    });

                // Move jewel flipper
                if (gamepad1.dpad_left)
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            jewelFlipperServoValue += 0.01;
                            clampJewelArmServo();
                        }
                    });

                // Move jewel flipper
                if (gamepad1.dpad_right)
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            jewelFlipperServoValue -= 0.01;
                            clampJewelFlipperServo();
                        }
                    });

                // Move relic hand
                if (Math.abs(gamepad2.right_stick_y) >= 0.25)
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            relicHandServoValue += gamepad2.right_stick_y * 0.012;
                            clampRelicHandServo();
                        }
                    });

                // Move relic finger in
                if (gamepad2.right_bumper)
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            relicFingersServoValue -= 0.02;
                            clampRelicFingersServo();
                        }
                    });

                // Move relic finger out
                if (gamepad2.left_bumper)
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            relicFingersServoValue += 0.02;
                            clampRelicFingersServo();
                        }
                    });

                // Move glyph lift
                if (triggered(gamepad2.left_trigger) && glyphLiftHigh.getState())
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            glyphLift.setPower(0.75);
                        }
                    });
                else if (triggered(gamepad2.right_trigger) && glyphLiftLow.getState())
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            glyphLift.setPower(-0.75);
                        }
                    });
                else
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            glyphLift.setPower(0.0);
                        }
                    });

                // Toggle auto-glyphing

                if ((gamepad2.x && !prev2.x) || (gamepad2.y && !prev2.y))
                    inputActions.add(new Runnable() {
                        @Override
                        public void run() {
                            enableAutoGlyph = !enableAutoGlyph;
                        }
                    });

                if (enableAutoGlyph)
                    if ((gamepad2.a && !prev2.a) || (gamepad2.b && !prev2.b))
                        inputActions.add(new Runnable() {
                            @Override
                            public void run() {
                                switch (glyphLiftState) {
                                    case LEVELED:
                                        glyphLift.setPower(0.5);
                                        glyphLiftState = GlyphLiftState.ASCENDING;
                                        break;
                                    case ASCENDED:
                                        glyphLiftState = GlyphLiftState.DUMPING;
                                        break;
                                    case DESCENDED:
                                        glyphLiftState = GlyphLiftState.LEVELING;
                                        break;
                                    case DUMPED:
                                        glyphLift.setPower(-0.5);
                                        glyphOutput.setPosition(0.5);
                                        glyphLiftState = GlyphLiftState.DESCENDING;
                                        break;
                                }

                                dumpServoManual = false;
                            }
                        });

                try {
                    prev1.copy(gamepad1);
                    prev2.copy(gamepad2);
                } catch (RobotCoreException e) {
                    telemetry.addData("Exception", e);
                }
            }
        }
    }

    private Gamepad prev1;
    private Gamepad prev2;

    private Thread gamepadThread;
    private Queue<Runnable> inputActions;

    private GlyphLiftState glyphLiftState;
    private SpeedToggle speedMult;
    private byte armPos = 1;
    private double jewelArmServoValue = 0, jewelFlipperServoValue = 0, relicHandServoValue = 0, relicFingersServoValue = 0, glyphDumpServoValue = 0;
    private boolean lifting, valueChange;
    private boolean armExtended;
    private boolean enableAutoGlyph = false;
    private boolean dumpServoManual;
    long waiting = 0, waitTime = 500;

    public enum SpeedToggle {
        SLOW(0.5), // originally 0.45
        FAST(0.7); // originally 0.80

        private double mult;

        SpeedToggle(double mult) {
            this.mult = mult;
        }

        public double getMult() {
            return mult;
        }
    }

    public enum GlyphLiftState {
        LEVELING(true),
        LEVELED(false),
        ASCENDING(true),
        ASCENDED(false),
        DUMPING(true),
        DUMPED(false),
        DESCENDING(true),
        DESCENDED(false);

        private boolean isMoving;

        GlyphLiftState(boolean moving) {
            isMoving = moving;
        }

        public boolean currentlyMoving() {
            return isMoving;
        }
    }

    @Override
    protected boolean isAutonomous() {
        return false;
    }

    @Override
    public void init() { // Configuration for this is in the Google Drive
        super.init();
        prev1 = new Gamepad();
        prev2 = new Gamepad();
        armExtended = false;

        glyphLiftState = GlyphLiftState.DESCENDED;

        inputActions = new ConcurrentLinkedQueue<>();

        gamepadThread = new InputHandlerThread();
    }

    @Override
    public void start() {
        jewelArmServoValue = 0;
        jewelFlipperServoValue = 0.5;
        relicFingersServoValue = 0.5;
        speedMult = SpeedToggle.SLOW;
        jewelFlipper.setPosition(0.5);
        relicHand.setPosition(0.4);
        glyphOutput.setPosition(0.0);

        dumpServoManual = true;
        enableAutoGlyph = false;

        gamepadThread.start();
    }

    @Override
    public void stop() {
        super.stop();

        gamepadThread.interrupt();
    }

    protected void toggleSpeed() {
        if (speedMult.equals(SpeedToggle.SLOW))
            speedMult = SpeedToggle.FAST;
        else
            speedMult = SpeedToggle.SLOW;
    }

    protected void clampJewelArmServo() {
        if (jewelArmServoValue > 0.8) // Maximum position
            jewelArmServoValue = 0.8;
        if (jewelArmServoValue < 0.25) // Minimum position
            jewelArmServoValue = 0.25;
    }

    protected void clampJewelFlipperServo() {
        if (jewelFlipperServoValue > 0.95)
            jewelFlipperServoValue = 0.95;
        if (jewelFlipperServoValue < 0.05)
            jewelFlipperServoValue = 0.05;
    }

    protected void clampRelicHandServo() {

        if (relicHandServoValue > 1) // Maximum position
            relicHandServoValue = 1;
        if (relicHandServoValue < 0) // Minimum position
            relicHandServoValue = 0;
        //0.188 = 0
        //0.23 = 270
        /*
        arm position of servos
        relic hand 270 degrees = 0.25
        relic hand 0 degrees = 0.2
        relic hand start position = 0.165
         */
    }

    protected void clampRelicFingersServo() {
        if (relicFingersServoValue > 1) // Maximum position
            relicFingersServoValue = 1;
        if (relicFingersServoValue < 0) // Minimum position
            relicFingersServoValue = 0;
    }

    protected void clampDumpServo() {
        if (glyphDumpServoValue > 1) // Maximum position
            glyphDumpServoValue = 1;
        if (glyphDumpServoValue < 0) // Minimum position
            glyphDumpServoValue = 0;
    }

    protected void refreshServos() {
        clampJewelArmServo();
        clampJewelFlipperServo();
        clampRelicHandServo();
        clampRelicFingersServo();
        clampDumpServo();

        jewelArm.setPosition(jewelArmServoValue);
        jewelFlipper.setPosition(jewelFlipperServoValue);
        relicHand.setPosition(relicHandServoValue);
        relicFingers.setPosition(relicFingersServoValue);

        if (dumpServoManual)
            glyphOutput.setPosition(glyphDumpServoValue);
    }

    protected void setMotorPowers() {
        setLeftPow(gamepad1.left_stick_y * -speedMult.getMult());
        setRightPow(gamepad1.right_stick_y * -speedMult.getMult());
    }

    @Override
    public void loop() {
        setMotorPowers();
        refreshServos();

        while (inputActions.size() > 0) {
            inputActions.poll().run();
        }

        switch (glyphLiftState) {
            case LEVELING:
                glyphOutput.setPosition(0.42);
                glyphLiftState = GlyphLiftState.LEVELED;
                break;
            case ASCENDING:
                if (!glyphLiftHigh.getState()) {
                    glyphLift.setPower(0);
                    glyphLiftState = GlyphLiftState.ASCENDED;
                }
                break;
            case DUMPING:
                glyphOutput.setPosition(1);
                glyphLiftState = GlyphLiftState.DUMPED;
                break;
            case DESCENDING:
                if (!glyphLiftLow.getState()) {
                    glyphLift.setPower(0);
                    glyphOutput.setPosition(0.0);
                    glyphLiftState = GlyphLiftState.DESCENDED;
                }
                break;
        }

        try {
            prev1.copy(gamepad1);
            prev2.copy(gamepad2);
        } catch (RobotCoreException e) {
            telemetry.addData("Exception", e);
        }
    }
}
