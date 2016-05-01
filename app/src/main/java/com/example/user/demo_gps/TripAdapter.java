package com.example.user.demo_gps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rupesh.
 * This is the Adapter for the TripLog Activity.
 */
public class TripAdapter extends ArrayAdapter<Trip>{
    public TripAdapter(Context context, ArrayList<Trip> trips){
        super(context, 0, trips);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Trip trip = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_trip, parent, false);
        }
        //TextView distance = (TextView)
        return convertView;
    }

}
