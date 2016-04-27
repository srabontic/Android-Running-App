package com.example.user.demo_gps;

/**
 * Created by Rupesh on 4/26/2016.
 */
public class Trip {
    private String duration;
    private double distance;
    private float numSteps;
    private float avgSpeed;
    private float maxSpeed;
    private float pace;
    private String description;

    public Trip(){
        this.duration = "00:00:00";
        this.distance = 0;
        this.numSteps = 0;
        this.avgSpeed = 0;
        this.maxSpeed = 0;
        this.pace = 0;
        this.description = "";
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public float getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(float numSteps) {
        this.numSteps = numSteps;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getPace() {
        return pace;
    }

    public void setPace(float pace) {
        this.pace = pace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
