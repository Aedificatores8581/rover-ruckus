package org.firstinspires.ftc.teamcode.Components.Blinkin;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PWMOutputController;
import com.qualcomm.robotcore.hardware.Servo;

/**
 *
 * Written by Theodore Lovinski 08 - 12 - 2018
 *
 * TODO: Test {test} {!!p:3} {!!allow_multiline}
 *
 */

enum blinkinLightsState {
	AESTHETIC,
	FUNCTIONAL,
	TEST
}

public class BlinkinLEDControl {
	public Servo colorControlServo;
	public PWMOutputController colorControlModulator;
	public BlinkinPresets blinkinPresets;

	public blinkinLightsState lightState;

	public void init(HardwareMap hardwareMap) {
		colorControlServo = hardwareMap.servo.get("blinkin");
		lightState = blinkinLightsState.AESTHETIC;
		setSolidColor(blinkinPresets.BLUE);
	}

	public void init() {
		lightState = blinkinLightsState.AESTHETIC;
		setSolidColor(blinkinPresets.RAW_PWM_AQUA);
	}

	public void setSolidColor(double PWM) {
		colorControlServo.setPosition(PWM);
	}

	public void setSolidColor(int PWM) {
		colorControlModulator.setPulseWidthPeriod(blinkinPresets.RAW_PWM_PORT, PWM);
	}

	public static final double MAX_PWM_ADDRESS = -0.05;
	public static final double MIN_PWM_ADDRESS = -0.99;
	public void setPattern(double pattern) {
		if (pattern > MIN_PWM_ADDRESS && pattern < MAX_PWM_ADDRESS) {
			setSolidColor(pattern);
		} else {
			// Not within given range.
			setSolidColor(blinkinPresets.RED);
		}
	}

	private static final double MAX_RAW_PWM_ADDRESS = 1475;
	private static final double MIN_RAW_PWM_ADDRESS = 1005;
	public void setPattern(int pattern) {
		if (pattern > MIN_RAW_PWM_ADDRESS && pattern < MAX_RAW_PWM_ADDRESS) {
			setSolidColor(pattern);
		} else {
			setSolidColor(blinkinPresets.RED);
		}
	}

	public void grabCustomSetting() { }

	public void switchDisplayState(blinkinLightsState state) { lightState = state; }

	public void switchDisplayState() {
		if (lightState == blinkinLightsState.AESTHETIC) {
			lightState = blinkinLightsState.FUNCTIONAL;
		} else if (lightState == blinkinLightsState.FUNCTIONAL) {
			lightState = blinkinLightsState.AESTHETIC;
		} else {
			lightState = blinkinLightsState.AESTHETIC;
		}
	}

	public blinkinLightsState getDisplayState() { return lightState; }
}
