package org.firstinspires.ftc.teamcode.Universal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Class for logging Telemetry data into CSV files
 * */
public class TelemetryLogger {
    /**
     * File to be written to.*/
    private File teleLog;
    private FileOutputStream os;

    /**
     * Path the file is written to (the sdcard folder here is where all external storage on the phone goes).*/
    private static final String dirPath = "/sdcard/TeleLogs";

    /**
     * Class constructor which creates a new log file.
     * */
    public TelemetryLogger() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd  HH-mm-ss");

        File teleLogDir = new File(dirPath);

        if (!teleLogDir.exists()) {
            teleLogDir.mkdirs();
        }

        teleLog = new File(teleLogDir, dateFormat.format(new Date()) + ".csv");

        teleLog.createNewFile();

        os = new FileOutputStream(teleLog);
    }

    /**
     * Accepts variables in a similar vain to telemetry.addData(), but instead of writing it
     * to the console, it writes it to a file
     *
     * @param data information to be written to a file
     */
    public void writeToLogInCSV(Object... data) throws IOException{
        for (int i = 0; i < data.length; ++i) {
            this.os.write((data[i].toString() + ",").getBytes());
        }

        this.os.write((byte)'\n');
    }

    /**
     * Closes the output stream.
     * */

    public void close() throws IOException {
        os.close();
    }
}
