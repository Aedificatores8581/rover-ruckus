package org.firstinspires.ftc.teamcode.Universal.Map;

import org.firstinspires.ftc.teamcode.Universal.Math.Pose;
import org.firstinspires.ftc.teamcode.Universal.Math.Pose3;
import org.firstinspires.ftc.teamcode.Universal.Math.Vector2;

import java.util.ArrayList;

public class RoverRuckusField extends Map3{
    //Samples are named by
    public Sample leftSample, rightSample, centerSample;
    public ArrayList<Sample> samples = new ArrayList<>();
    public ArrayList<QuadrantBasedElement> quadrantBasedElements = new ArrayList<>();
    public Quadrant quadrantOfFocus;
    public QuadrantBasedElement latch;



    public RoverRuckusField(Quadrant quadrant){
        super();
        quadrantOfFocus = quadrant;

        leftSample = new Sample(new Pose(25, 47));
        centerSample = new Sample(new Pose(36, 36));
        leftSample = new Sample(new Pose(47, 25));

        Vector2 latchLocation = new Vector2();
        latchLocation.setFromPolar(23.3, Math.PI / 4);
        latch = new QuadrantBasedElement(latchLocation);

        samples.add(0, rightSample);
        samples.add(0, centerSample);
        samples.add(0, leftSample);

        for(Sample s : samples)
            quadrantBasedElements.add(s);
        quadrantBasedElements.add(latch);
    }


    public void switchQuadrant(Pose location){
        if(location.x != 0 && location.y != 0) {
            if (location.x > 0) {
                if(location.y > 0)
                    quadrantOfFocus = Quadrant.FIRSTtm;
                else
                    quadrantOfFocus = Quadrant.FOURTH;
            }
            else{
                if(location.y > 0)
                    quadrantOfFocus = Quadrant.SECOND;
                else
                    quadrantOfFocus = Quadrant.THIRD;
            }
        }

    }

    public class Sample extends QuadrantBasedElement{
        private int sampleOrdinal;
        public Sample(Pose location){
            super(location);
            sampleOrdinal = 0;
        }
        public boolean identified(){
            return sampleOrdinal != 0;
        }
        public void sampleIsGold(){
            sampleOrdinal = sampleOrdinal == -1 ? -1 : 1;
        }
        public void sampleIsSilver(){
            sampleOrdinal = sampleOrdinal == 1 ? 1 : -1;
        }
        public boolean isGold(){
            return sampleOrdinal == 1;
        }
        public boolean isSilver(){
            return sampleOrdinal == -1;
        }
    }
    public class QuadrantBasedElement{
        public final Vector2 locationInQ1;
        public Pose position;
        public QuadrantBasedElement(Pose location) {
            locationInQ1 = position.toVector();
            position = location;
        }
        public QuadrantBasedElement(Vector2 vec){
            this(new Pose(vec.x, vec.y));
        }
        public void switchQuadrant(Quadrant quadrant){
            Vector2 temp = new Vector2(locationInQ1.x, locationInQ1.y);
            double angleToRotate = 0;
            switch(quadrant){
                case SECOND:
                    angleToRotate = Math.PI / 2;
                    break;
                case THIRD:
                    angleToRotate = Math.PI;
                    break;
                case FOURTH:
                    angleToRotate = 3 * Math.PI / 2;
                    break;
            }
            temp.rotate(angleToRotate);
            position.x = temp.x;
            position.y = temp.y;
        }
    }
    public enum Quadrant{
        FIRSTtm,
        SECOND,
        THIRD,
        FOURTH
    }

}
