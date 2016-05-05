package com.example.user.demo_gps;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Statistics extends AppCompatActivity {
    List<Trip> trips;
    double miles = 0;
    String totalTime = "";
    long timeSum = 0;
    double pace = 0;    // Calculated Value
    double speed = 0;   // Calculted Value
    int steps = 0;
    SimpleDateFormat duration = new SimpleDateFormat("hh:mm:ss");
    TripAdapter adapter;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // ToolBar Creation
        Toolbar statisticsTop = (Toolbar) findViewById(R.id.statistics);
        setSupportActionBar(statisticsTop);
        statisticsTop.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        statisticsTop.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Statistics.super.onBackPressed();
            }
        });

        // Sets color of Phone's status bar
        Window window = Statistics.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Statistics.this.getResources().getColor(android.R.color.black));

        //create path
        try {
            createPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Process data
        try {
            gatherValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
        calculateStats();

        // Set Text View Titles
        TextView totDistance = (TextView) findViewById(R.id.totalDistanceValue);
        totDistance.setText(miles + " kilometer");
        TextView totTime = (TextView) findViewById(R.id.totalTimeTravelledValue);
        totTime.setText(totalTime);
        TextView avgPace = (TextView) findViewById(R.id.avgPaceValue);
        avgPace.setText(Math.round(pace)+ " minutes/km");
        TextView avgSpeed = (TextView) findViewById(R.id.avgSpeedValue);
        avgSpeed.setText(Math.round(speed) + " kph");
        TextView totSteps = (TextView) findViewById(R.id.totalStepsValue);
        totSteps.setText(steps + "");
    }

    /********Created By: Srabonti Chakraborty**************/
    public List<Trip> readContact() throws IOException{
        FileOperations fileIO = new FileOperations();

        List<Trip> tripList = fileIO.readFile( Environment.getExternalStorageDirectory());
//        Log.i("First Name", personList.get(0).getFirstName());
        return tripList;
    }

    public void gatherValues() throws IOException {
        /********Edited By: Srabonti Chakraborty**************/
        //read all the file data into an arraylist
        trips = readContact();


        // Gathers total values
        for(int i=0; i<trips.size(); i++){
            miles += Double.parseDouble(trips.get(i).getDistance());
            timeParser(trips.get(i).getDuration());
            steps += Integer.parseInt(trips.get(i).getNumSteps());
            speed += Double.parseDouble(trips.get(i).getAvgSpeed());
        }
    }

    // Parses time of various trips into one
    private void timeParser(String time){
        try {
            Date date = duration.parse(time);
            timeSum += date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Calculates the statistics
    private void calculateStats(){
        totalTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeSum),
                TimeUnit.MILLISECONDS.toMinutes(timeSum) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(timeSum) % TimeUnit.MINUTES.toSeconds(1));
        speed = speed / trips.size();
        pace = 60.0/speed;
    }

    /********Created By: Srabonti Chakraborty**************/
    //this method creats path for the file
    private void createPath() throws IOException {

        FileOperations fileIO = new FileOperations();
        fileIO.createPath(Environment.getExternalStorageDirectory());
        //Toast.makeText(MainActivity_contact_list.this,message1, Toast.LENGTH_LONG).show();
        Log.i("Create Path: ", Environment.getExternalStorageDirectory().toString());

    }
}
