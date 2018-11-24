package org.firstinspires.ftc.teamcode.Universal.Testers;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Universal.JSONAutonGetter;
import org.firstinspires.ftc.teamcode.Universal.UniversalConstants;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

//Todo: Test!
@Autonomous(name = "JSONVisionInfoSaverTester", group = "tester")
public class JSONVisionInfoSaverTester extends OpMode {
    private JSONAutonGetter jsonAutonGetter;
    private int currentLocationIndex;
    ArrayList<TestLocation> testLocations;
    Gamepad prev1;

    @Override
    public void init() {

        try {
            testLocations = new ArrayList<>(jsonAutonGetter.jsonObject.getJSONArray("locations").length());

            jsonAutonGetter = new JSONAutonGetter("JSONVisionInfoSaverTester.json");

            for (int i = 0; i < jsonAutonGetter.jsonObject.getJSONArray("locations").length(); ++i) {
                telemetry.addLine(jsonAutonGetter.jsonObject.getJSONArray("locations").getJSONObject(i).toString());
                testLocations.add(new TestLocation(jsonAutonGetter.jsonObject.getJSONArray("locations").getJSONObject(i)));
            }

            currentLocationIndex = jsonAutonGetter.jsonObject.getInt("lastSavedLocation");
        } catch (IOException | JSONException | NullPointerException e) {
            telemetry.addLine(e.getMessage());
            stop();
        }
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            if ((currentLocationIndex - 1) < 0) {
                currentLocationIndex = testLocations.size() - 1;
            }
            currentLocationIndex = (currentLocationIndex - 1) % testLocations.size();
        }

        if (gamepad1.dpad_down) {

            if ((currentLocationIndex + 1) > testLocations.size()) {
                currentLocationIndex = 0;
            } else {
                currentLocationIndex++;
            }
        }

        if (gamepad1.left_stick_y > UniversalConstants.Triggered.STICK){
            testLocations.get(currentLocationIndex).x += 1;
        }else if (gamepad1.left_stick_y < -UniversalConstants.Triggered.STICK){
            testLocations.get(currentLocationIndex).x -= 1;
        }

        if (gamepad1.right_stick_y > UniversalConstants.Triggered.STICK){
            testLocations.get(currentLocationIndex).y += 1;
        }else if (gamepad1.right_stick_y < -UniversalConstants.Triggered.STICK){
            testLocations.get(currentLocationIndex).y -= 1;
        }

        if (gamepad1.a && !prev1.a) {
            try {
                jsonAutonGetter.jsonObject.put("locations", testLocations.toArray());
                jsonAutonGetter.saveToFile();
            } catch (IOException e) {
                telemetry.addData("Issue with File Saving", e.getMessage());
            } catch (JSONException e) {
                telemetry.addData("JSON Issue with File Saving", e.getMessage());
            }
        }


        telemetry.addData("Index",currentLocationIndex);
        telemetry.addData("Name", testLocations.get(currentLocationIndex).name);
        telemetry.addData("X", testLocations.get(currentLocationIndex).x);
        telemetry.addData("Y", testLocations.get(currentLocationIndex).y);

        try {
            prev1.copy(gamepad1);
        } catch (RobotCoreException e) {
            telemetry.addData("Issue with Gamepad", e.getMessage());
        }
    }

    @Override
    public void stop(){
        try {
            jsonAutonGetter.jsonObject.put("lastSavedLocation", currentLocationIndex);
            jsonAutonGetter.saveToFile();
        } catch (JSONException | IOException e) {
            telemetry.addData("Issue with saving currentLocationIndex", e.getMessage());
        }
    }
}

class TestLocation {
    public String name;
    public int x;
    public int y;

    TestLocation(JSONObject jsonObject) throws JSONException, NullPointerException {
        this.name = jsonObject.getString("name");
        this.x = jsonObject.getInt("x");
        this.y = jsonObject.getInt("y");
    }
}