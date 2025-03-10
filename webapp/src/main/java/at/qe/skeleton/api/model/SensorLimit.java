package at.qe.skeleton.api.model;

import at.qe.skeleton.model.Sensor;

public class SensorLimit {

    private double upperLimit;

    private double lowerLimit;

    public double getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(double upperLimit) {
        this.upperLimit = upperLimit;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public SensorLimit(Sensor sensor) {
        this.lowerLimit = sensor.getLowerLimit();
        this.upperLimit = sensor.getUpperLimit();}
}
