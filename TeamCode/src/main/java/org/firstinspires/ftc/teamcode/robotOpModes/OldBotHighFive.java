package org.firstinspires.ftc.teamcode.robotOpModes;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.robotTemplates.OldBotTemplate;

/**
 * Conjured into existence by The Saminator on 2018-05-06.
 */

public class OldBotHighFive extends OldBotTemplate {
    final double JEWEL_HAND_LENGTH = 9.6;
    final double JEWEL_HAND_THRESHOLD = 8;

    @Override
    public void loop() {
        // Jewel hand length is 9.6 cm.
        if (dSensorL.getDistance(DistanceUnit.CM) < JEWEL_HAND_THRESHOLD || dSensorR.getDistance(DistanceUnit.CM) < JEWEL_HAND_THRESHOLD) {
            double mult = 1;
            double distance;
            if (dSensorL.getDistance(DistanceUnit.CM) < dSensorR.getDistance(DistanceUnit.CM)) {
                mult *= -1;
                distance = dSensorL.getDistance(DistanceUnit.CM);
            }
            else distance = dSensorR.getDistance(DistanceUnit.CM);

            double angle = Math.acos(distance / JEWEL_HAND_LENGTH);
            double position = angle / Math.PI;

            jewelFlipper.setPosition(position * mult);
        }
    }
}
