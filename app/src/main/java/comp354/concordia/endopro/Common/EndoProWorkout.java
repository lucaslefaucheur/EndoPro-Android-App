package comp354.concordia.endopro.Common;

public class EndoProWorkout {

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

}