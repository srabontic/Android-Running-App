package com.example.user.demo_gps;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Rupesh
 * The Trip View page gives a more detailed view of the user's individual trips
 */
public class TripView extends AppCompatActivity {
    int position;
    String duration_show;
    String distance_show;
    String numsteps_show;
    String angspeed_show;
    String date_show;
    String EditMode="Add";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_view);
        // Position is received from the bundle
        position = getIntent().getExtras().getInt("pos");

        // ToolBar Creation
        Toolbar tripViewTop = (Toolbar) findViewById(R.id.tripViewToolbar);
        setSupportActionBar(tripViewTop);
        tripViewTop.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        tripViewTop.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                TripView.super.onBackPressed();
            }
        });

        // Sets color of Phone's status bar
        Window window = TripView.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(TripView.this.getResources().getColor(android.R.color.black));

        // Trip is found from the position passed by the bundle
        //Trip trip = TripLog.tripsArray.get(position);

        /********Created By: Srabonti Chakraborty**************/
        Bundle bundle = getIntent().getExtras();
        duration_show = bundle.getString("duration");
        distance_show = bundle.getString("distance");
        numsteps_show = bundle.getString("steps");
        angspeed_show = bundle.getString("avgspeed");
        date_show = bundle.getString("Date");
        //EditMode = bundle.getString("Edit");

        String distance_show_new;
        //truncate distance before show
        Log.i("Dist", distance_show);
        if (distance_show.length() > 6){
            distance_show_new = distance_show.substring(0,4);
            Log.i("Dist", distance_show_new);
        }
        else
        {
            distance_show_new = distance_show;
        }


        // Set Text View Titles
        /*********Edited by: srabonti Chakraborty***************/
        TextView titleDate = (TextView) findViewById(R.id.tripViewTitleDate);
        titleDate.setText("    Trip Date: " + date_show);
        TextView duration = (TextView) findViewById(R.id.tripViewDuration);
        duration.setText("     Duration: " + duration_show);
        TextView distance = (TextView) findViewById(R.id.tripViewDistance);
        distance.setText("     Distance: " + distance_show_new + " km");
        TextView avgSpeed = (TextView) findViewById(R.id.tripViewAvgSpeed);
        avgSpeed.setText("     Avg Speed: " + angspeed_show + " km/h");
        TextView numSteps = (TextView) findViewById(R.id.tripViewNumSteps);
        numSteps.setText("     Number of Steps: " + numsteps_show);

        /********Created By: Srabonti Chakraborty**************/
        // Delete trip from list
        ImageButton delete = (ImageButton) findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TripLog.tripsArray.remove(position);
                //TripLog.adapter.notifyDataSetChanged();
                //TripView.super.onBackPressed();
                //delete contact from file on delete button click
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                FileOperations fileIO = new FileOperations();
                                try {
                                    fileIO.deleteRecord( Environment.getExternalStorageDirectory(), duration_show, distance_show, numsteps_show, angspeed_show, date_show);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                finish();
                                Toast.makeText(getBaseContext(), "Trip Summery is deleted!", Toast.LENGTH_SHORT).show();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(TripView.this);
                builder.setMessage("Are you sure you want to delete this trip summery?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

    }
}
