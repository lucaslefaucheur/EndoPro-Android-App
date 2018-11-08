package comp354.concordia.endopro.Common;

import java.io.Serializable;

public class EndoProWorkout implements Serializable {

    private double speed_avg;
    private double speed_max;

    private double distance;
    private double duration;

    private String startTime; // "DDMMYYYY"

    public EndoProWorkout(double speed_avg, double speed_max, double distance, double duration, String startTime) {
        this.speed_avg = speed_avg;
        this.speed_max = speed_max;
        this.distance = distance;
        this.duration = duration;
        this.startTime = startTime;
    }
    public double getSpeedAverage() { return speed_avg; }

    public double getSpeedMax() { return speed_max; }

    public double getDistance() { return distance; }

    public double getDuration() { return duration; }

    public String getStartTime() { return startTime; }
}
