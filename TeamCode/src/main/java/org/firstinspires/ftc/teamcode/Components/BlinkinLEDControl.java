package org.firstinspires.ftc.teamcode.Components;

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

interface blinkinServoColors {
	double  BLACK = 0.99,
			DARK_GREY = 0.97,
			GRAY = 0.95,
			WHITE = 0.93,
			VIOLET = 0.91,
			BLUE_VIOLET = 0.89,
			BLUE = 0.87,
			DARK_BLUE = 0.85,
			SKY_BLUE = 0.83,
			AQUA = 0.81,
			BLUE_GREEN = 0.79,
			GREEN = 0.77,
			DARK_GREEN = 0.75,
			LIME = 0.73,
			LAWN_GREEN = 0.71,
			YELLOW = 0.69,
			GOLD = 0.67,
			ORANGE = 0.65,
			RED_ORANGE = 0.63,
			RED = 0.61,
			DARK_RED = 0.59,
			HOT_PINK = 0.57;
}

interface blinkinPWMColors {
	int PORT = 0;
	int BLACK = 1995,
		DARK_GREY = 1985,
		GRAY = 1975,
		WHITE = 1965,
		VIOLET = 1955,
		BLUE_VIOLET = 1945,
		BLUE = 1935,
		DARK_BLUE = 1925,
		SKY_BLUE = 1915,
		AQUA = 1905,
		BLUE_GREEN = 1895,
		GREEN = 1885,
		DARK_GREEN = 1875,
		LIME = 1865,
		LAWN_GREEN = 1855,
		YELLOW = 1845,
		GOLD = 1835,
		ORANGE = 1825,
		RED_ORANGE = 1815,
		RED = 1805,
		DARK_RED = 1795,
		HOT_PINK = 1785;
}

public class BlinkinLEDControl {
	protected blinkinServoColors servoColors;
	protected blinkinPWMColors pwmColors;

	public Servo colorControlServo;
	public PWMOutputController colorControlModulator;

	public blinkinLightsState lightState;

	public void init(HardwareMap hardwareMap) {
		colorControlServo = hardwareMap.servo.get("blinkin");
		lightState = blinkinLightsState.AESTHETIC;
		setSolidColor(blinkinServoColors.BLUE);
	}

	public void init() {
		lightState = blinkinLightsState.AESTHETIC;
		setSolidColor(servoColors.AQUA);
	}

	public void setSolidColor(double servoColorValue) {
		colorControlServo.setPosition(servoColorValue);
	}

	public void setSolidColor(int PWM) {
		colorControlModulator.setPulseWidthPeriod(pwmColors.PORT, PWM);
	}

	private static final double MAX_SERVO_ADDRESS = -0.05;
	private static final double MIN_SERVO_ADDRESS = -0.99;
	public void setPattern(double pattern) {
		if (pattern > MIN_SERVO_ADDRESS && pattern < MAX_SERVO_ADDRESS) {
			setSolidColor(pattern);
		} else {
			// Not within given range.
			setSolidColor(servoColors.RED);
		}
	}

	private static final double MAX_PWM_ADDRESS = 1475;
	private static final double MIN_PWM_ADDRESS = 1005;
	public void setPattern(int pattern) {
		if (pattern > MIN_PWM_ADDRESS && pattern < MAX_PWM_ADDRESS) {
			setSolidColor(pattern);
		} else {
			setSolidColor(pwmColors.RED);
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
