package ftc.vision;

import org.opencv.core.Mat;

public abstract class Detector {
    public boolean isInitialized = false;
    public Detector(){
        isInitialized = true;
    }
    public enum OperatingState{
        TUNING,
        DETECTING
    }

    public abstract void detect(Mat image);

    public abstract Mat result();

}
