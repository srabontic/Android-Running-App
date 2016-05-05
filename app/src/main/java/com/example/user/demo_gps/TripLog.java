package com.example.user.demo_gps;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rupesh.
 * The TripLog Activity
 * Here the trips are displayed in a list view
 *  > ADAPTER: TripAdapter
 *  > LIST VIEW: tripsList
 */

public class TripLog extends AppCompatActivity {
    public static ArrayList<Trip> tripsArray = new ArrayList<Trip>();
    public static TripAdapter adapter;
    public ListView listView1;

    //@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(TripLog.this, "in log", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_trip_log);
        Toast.makeText(TripLog.this, "in log1", Toast.LENGTH_SHORT).show();
        // ToolBar
        Toolbar TripLogTop = (Toolbar) findViewById(R.id.view);
        setSupportActionBar(TripLogTop);
        setSupportActionBar(TripLogTop);
        TripLogTop.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        TripLogTop.setTitle("Trip Log");
        TripLogTop.setNavigationOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                TripLog.super.onBackPressed();
            }
        });
        //top.setNavigationIcon(R.drawable.);
        // Sets color of Phone's status bar
        Window window = TripLog.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(TripLog.this.getResources().getColor(android.R.color.black));
        // List View Code
        listView1 = (ListView) findViewById(R.id.tripsList);

        //create Path for file access
        try {
            createPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)  {
                    /*Bundle b = new Bundle();
                    b.putInt("pos", position);
                    Intent myIntent = new Intent(view.getContext(), TripView.class);
                    myIntent.putExtras(b);
                    startActivityForResult(myIntent, 0);*/
                try {
                    actionEdit(position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /********Created By: Srabonti Chakraborty**************/
    ////send the Trip details on selection of any rows in listview
    private void actionEdit(int position) throws IOException {
        final List<Trip> listTrip = readContact();
        Trip p = listTrip.get(position);

        Intent editintent = new Intent(TripLog.this, TripView.class);
        Bundle bundle = new Bundle();
        bundle.putString("duration", p.getDuration() );
        bundle.putString("distance", p.getDistance() );
        bundle.putString("steps", p.getNumSteps() );
        bundle.putString("avgspeed", p.getAvgSpeed());
        bundle.putString("Date", p.getDateOfTrip());
        bundle.putString("EditMode", "Edit");
        editintent.putExtras(bundle);
        startActivity(editintent);
    }

    /********Created By: Srabonti Chakraborty**************/
    //this method creats path for the file
    private void createPath() throws IOException {

        FileOperations fileIO = new FileOperations();
        fileIO.createPath(Environment.getExternalStorageDirectory());
        //Toast.makeText(MainActivity_contact_list.this,message1, Toast.LENGTH_LONG).show();
        //Log.i("Create Path: ", Environment.getExternalStorageDirectory().toString());

    }

    /********Created By: Srabonti Chakraborty**************/
    //on resume (is a sub routine of create), add data to list o
    protected void onResume(){
        super.onResume();

        try {
            createPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            makeTable();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /********Created By: Srabonti Chakraborty**************/
    //creating custom ArrayAdaptor list
    private void makeTable() throws  IOException{
        //Typeface fontHeading = Typeface.createFromAsset(getAssets(),"fonts/Segoe-UI-Symbol.ttf");
        //Typeface fontBody = Typeface.createFromAsset(getAssets(),"fonts/Segoe-Regular.ttf");
        //Toast.makeText(MainActivity_contact_list.this,message, Toast.LENGTH_LONG).show();

        //before showing data make no trip invisible
        TextView t = (TextView) findViewById(R.id.noContactMessage);
        t.setVisibility(View.GONE);

        final List<Trip> listTrip = readContact();
        //Log.i("Show Name", listPerson.get(0).getLastName());
        adapter = new TripAdapter(this, R.layout.item_trip, listTrip);

        listView1 = (ListView)findViewById(R.id.tripsList);

        listView1.setAdapter(adapter);

        //sortListByName();

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // Call Edit function
                Log.i("sort", "pos:" + position);
                try {
                    actionEdit(position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        registerForContextMenu(listView1);
        //return true;

    }


    /********Created By: Srabonti Chakraborty**************/
    public List<Trip> readContact() throws IOException{
        FileOperations fileIO = new FileOperations();

        List<Trip> personList = fileIO.readFile( Environment.getExternalStorageDirectory());
//        Log.i("First Name", personList.get(0).getFirstName());
        return personList;
    }

}
