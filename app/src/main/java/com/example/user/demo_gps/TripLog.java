package com.example.user.demo_gps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class TripLog extends AppCompatActivity {
    public static ArrayList<Trip> tripsArray = new ArrayList<Trip>();
    public static TripAdapter adapter;
    public ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_log);
        // List View Code
        listView = (ListView) findViewById(R.id.tripsList);
        adapter = new TripAdapter(getBaseContext(), tripsArray);
        listView.setAdapter(adapter);
        listView.setEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent myIntent = new Intent(view.getContext(), NewContact.class);
                //newConFlag = true;
                //pos = position;
               // contact = contactsArray.get(pos);
                //myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivityForResult(myIntent, 0);
            }
        });
    }
}
