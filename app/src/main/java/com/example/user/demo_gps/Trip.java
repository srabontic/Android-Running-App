package com.example.user.demo_gps;

/**
 * Created by Rupesh on 4/26/2016.
 */
//this is the trip class which is used to store and retrieve trip records from text file
public class Trip {
    private String duration;
    private String distance;
    private String numSteps;
    private String avgSpeed;
    private String dateOfTrip;

    public String getDateOfTrip() {
        return dateOfTrip;
    }

    public void setDateOfTrip(String dateOfTrip) {
        this.dateOfTrip = dateOfTrip;
    }

    /********Created By: Srabonti Chakraborty**************/
    public Trip(){
        this.duration = "00:00:00";
        this.distance = "0.000";
        this.numSteps = "0";
        this.avgSpeed = "0.000";
        this.dateOfTrip = "00/00/0000";

    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(String numSteps) {
        this.numSteps = numSteps;
    }

    public String getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }


    /********Created By: Srabonti Chakraborty**************/

    public Trip(String duration, String distance, String numSteps,String avgSpeed, String dateOfTrip) {
        super();
        this.duration = duration;
        this.distance = distance;
        this.numSteps = numSteps;
        this.avgSpeed = avgSpeed;
        this.dateOfTrip = dateOfTrip;
    }
}
