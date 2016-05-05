package com.example.user.demo_gps;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rupesh.
 * This is the Adapter for the TripLog Activity.
 */
public class TripAdapter extends ArrayAdapter<Trip>{
    Context context;
    int layoutResourceId;
    List<Trip> tripList;

    public TripAdapter(Context context,int layoutResourceId, List<Trip> trips){

        super(context, layoutResourceId, trips);
        /********Created By: Srabonti Chakraborty**************/
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.tripList = trips;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        TripHolder holder;
        Trip trip = getItem(position);
        /*if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_trip, parent, false);
        }
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        return convertView;*/
        /********Created By: Srabonti Chakraborty**************/
        if(row == null)
        {
            // Uses the Android built in Layout Inflater to inflate (parse) the xml layout file.
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new TripHolder();
            holder.tripLogIcon = (ImageView)row.findViewById(R.id.tripLogIcon);
            holder.txtdistance = (TextView)row.findViewById(R.id.txtdistance);
            holder.txttime = (TextView)row.findViewById(R.id.txttime);
            holder.txtdate = (TextView)row.findViewById(R.id.txtdate);

            holder.txtdistance.setId(position);
            //holder.txtName.setTypeface(fontText);
            //holder.txtPhone.setTypeface(fontText);

            row.setTag(holder);
        }
        else
        {
            holder = (TripHolder)row.getTag();
        }
        //Log.i("First Name", personList.get(position).getFirstName());
        holder.tripLogIcon.setImageResource(R.drawable.ic_directions_run_white_48dp);
        holder.txtdistance.setText( tripList.get(position).getDistance().substring(0,4)+ " km");
        holder.txttime.setText( tripList.get(position).getDuration() );
        holder.txtdate.setText(tripList.get(position).getDateOfTrip());

        return row;
    }
    /********Created By: Srabonti Chakraborty**************/
    static class TripHolder
    {
        ImageView tripLogIcon;
        TextView txtdistance;
        TextView txttime;
        TextView txtdate;
    }

}
