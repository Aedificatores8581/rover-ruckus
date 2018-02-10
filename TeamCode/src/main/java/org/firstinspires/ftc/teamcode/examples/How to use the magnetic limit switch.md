Declare the magnetic switch as a DigitalChannel:

    DigitalChannel magnetSensor;

Instantiate it from the hardware map in init():

    magnetSensor = hardwareMap.digitalChannel.get("<<< INSERT NAME HERE >>>");
    magnetSensor.setMode(DigitalChannel.Mode.INPUT);

Use the sensor; for some reason, FALSE reports a magnet present, and TRUE reports a magnet absent:

    boolean magnetDetected = !magnetSensor.getState();