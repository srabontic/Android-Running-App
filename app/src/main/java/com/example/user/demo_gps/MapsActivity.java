package com.example.user.demo_gps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/********Created By: Srabonti Chakraborty**************/
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener  {

    /**
     * Map Activity:
     * Provides an interface to display the location of the user and tracks the path
     * travelled by the user. This activity also displays the statistics of the
     * ongoing trip.
     **/

    private GoogleMap mMap;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    TextView txtLat;
    LatLng here;
    private Polyline line;
    private ArrayList<LatLng> routePoints;
    private int firstPoint_flag = 0;
    double new_lat =  0.0;
    double new_lng =  0.0;
    double prev_lat = 0.0;
    double prev_lng = 0.0;
    double dist_prev_lat = 0.0;
    double dist_prev_lng = 0.0;
    private int start_stop_flag = 0; //start = 1, stop =0
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    //private float dist =0;
    private double distance =0.000;

    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private String timerValue = "00:00:00";
    String a = "aaaa";
    PopupWindow pw;
    double maxspeed, myspeed =0;
    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
    private Sensor mStepDetectorSensor;
    private int stepvalue = -1;
    private double avgspeed =0;
    ArrayList<Double> storeSpeed;
    String save_timer;
    int count =0;
    int distinterval =0;
    boolean activityRunning;


    /********Created By: Srabonti Chakraborty**************/
    //This OnCreate method loads the Map
    //On Button Click event it points to the current location and starts tracking the path
    //It also gets the duration of trip
    //and distance traversed by the user
    //Speed of the user
    //Average speed calculated after 5 secs
    //Steps taken by the user
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Creation of main activity toolbar
        // Must be linked in with Maps fragment
        Toolbar top = (Toolbar) findViewById(R.id.view);
        top.setLogo(R.drawable.ic_launcher);
        top.setTitle("Walk-A-Lot");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        mMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        //set sensor manager
        //step count init

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        //mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);


        // Navigation Drawer List View

        //LatLng sydney = new LatLng(-34, 151);
        TextView t1 = (TextView) findViewById(R.id.textView_distance);
        t1.setText("       Distance     : " + distance + " km");
        TextView t2 = (TextView) findViewById(R.id.textView_duration);
        t2.setText("       Duration     :  " + timerValue);
        TextView t3 = (TextView) findViewById(R.id.textView_speed);
        t3.setText("       Speed         : " + myspeed + " km/h");
        TextView t4 = (TextView) findViewById(R.id.textView_Avgspeed);
        t4.setText("       Avg Speed  : " + avgspeed + " km/h");
        TextView t5 = (TextView) findViewById(R.id.textView_steps);
        stepvalue =0;
        t5.setText("       Steps          : " + stepvalue);



        if (mMap != null) {
            //start stop button click
            //First on start/stop button click event tracking will be started and it will stop tracking
            //when button click will be called second time.
            Button button = (Button) findViewById(R.id.button_start_stop);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //Toast.makeText(MapsActivity.this, "button clicked", Toast.LENGTH_SHORT).show();
                    if (start_stop_flag == 0){
                        start_stop_flag =1;  //start
                        Toast.makeText(MapsActivity.this, "start", Toast.LENGTH_SHORT).show();
                        stepvalue =0;
                        //array to store speed
                        storeSpeed = new ArrayList<Double>();
                    }
                    else {
                        //on click of start/stop button as stop
                        //stop distance, duration, speed updates
                        Toast.makeText(MapsActivity.this, "stop", Toast.LENGTH_SHORT).show();
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        start_stop_flag = 0;
                                        Toast.makeText(MapsActivity.this, "stop", Toast.LENGTH_SHORT).show();

                                        save_timer = timerValue;
                                        //stop duration count
                                        timeSwapBuff += timeInMilliseconds;
                                        customHandler.removeCallbacks(updateTimerThread);

                                        //show trip summery using popup window
                                        showTripSummeryPopup(MapsActivity.this);

                                        //reset all field after finish
                                        stepvalue=0;
                                        resetFields();


                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;

                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                        builder.setMessage("Do you want to finish the trip?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }


                    //Toast.makeText(MapsActivity.this, "before if", Toast.LENGTH_SHORT).show();
                    if (start_stop_flag == 1) {
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        //Toast.makeText(MapsActivity.this, "location manager", Toast.LENGTH_SHORT).show();
                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Toast.makeText(MapsActivity.this, "before return", Toast.LENGTH_SHORT).show();
                            routePoints = new ArrayList<LatLng>();
                            return;
                        }
                        //Toast.makeText(MapsActivity.this, "i am here", Toast.LENGTH_SHORT).show();
                        //Log.i("flag",Integer.toString(start_stop_flag) );

                        if (start_stop_flag == 1) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {

                                @Override
                                public void onLocationChanged(Location location) {
                                    // TODO Auto-generated method stub
                                    //Toast.makeText(MapsActivity.this, "onLocationChanged1", Toast.LENGTH_SHORT).show();
                                    new_lat = location.getLatitude();
                                    new_lng = location.getLongitude();

                                    //point to current location
                                    here = new LatLng(location.getLatitude(), location.getLongitude());

                                    if (firstPoint_flag == 0) {
                                        Marker m = mMap.addMarker(new MarkerOptions().position(here).title("Start"));
                                        prev_lat = new_lat;
                                        prev_lng = new_lng;
                                        firstPoint_flag = 1;
                                        startTime = SystemClock.uptimeMillis();
                                        customHandler.postDelayed(updateTimerThread, 0);
                                    }
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(here, 17));
                                    //draw trail
                                    PolylineOptions options = new PolylineOptions();
                                    options.add(new LatLng(prev_lat, prev_lng));
                                    options.add(new LatLng(new_lat, new_lng));
                                    options.width(15);
                                    options.color(Color.YELLOW);
                                    //if (start_stop_flag == 1) {
                                    mMap.addPolyline(options);
                                    //}
                                    prev_lat = new_lat;
                                    prev_lng = new_lng;

                                    //calculate distance, duration, speed fields
                                    //calculate distance between two locations after each 5 secs
                                    distinterval += 1;

                                    if (distinterval == 1) {
                                        dist_prev_lat = new_lat;
                                        dist_prev_lng = new_lng;
                                    }

                                    //if (start_stop_flag == 1) {
                                    if (distinterval > 5) {
                                        distinterval = 0;
                                        distance += distanceCovered(dist_prev_lat, dist_prev_lng, new_lat, new_lng);
                                        String distStr = String.valueOf(distance);
                                        //Log.i("distance:", distStr);
                                        TextView t1 = (TextView) findViewById(R.id.textView_distance);
                                        t1.setText("       Distance     : " + String.format("%.3f", distance) + " km");

                                    }
                                    //}

                                    //calculate speed
                                    //if (start_stop_flag == 1) {
                                    if (location.hasSpeed()) {
                                        myspeed = location.getSpeed();
                                        count++;
                                        //Log.i("speed:", String.valueOf(myspeed));
                                        //Log.i(" sec count:", String.valueOf(count));
                                        TextView t3 = (TextView) findViewById(R.id.textView_speed);
                                        t3.setText("       Speed         : " + String.format("%.3f", myspeed) + " km/h");
                                        //calculate average for the stored speed
                                        storeSpeed.add(myspeed);
                                        double sumspeed = 0;

                                        //calculate average speed
                                        if (count > 100) {    //refresh arralist every 5 mins
                                            //calculate avg speed
                                            count = 0;
                                            for (Double d : storeSpeed) {
                                                sumspeed += d;
                                            }
                                            avgspeed = ((avgspeed * 100) + sumspeed) / (storeSpeed.size() + 100);

                                        } else {
                                            //only calc avg speed from arraylist
                                            for (Double d : storeSpeed) {
                                                sumspeed += d;
                                            }
                                            avgspeed = sumspeed / storeSpeed.size();

                                        }
                                        //show average speed
                                        TextView t4 = (TextView) findViewById(R.id.textView_Avgspeed);
                                        t4.setText("       Avg Speed  : " + String.format("%.3f", avgspeed) + " km/h");
                                    }
                                    //}

                                }

                                /********
                                 * Created By: Srabonti Chakraborty
                                 **************/
                                //OnSensor changed show steps taken
                                /*public void onSensorChanged(SensorEvent event) {
                                    Toast.makeText(MapsActivity.this, "before sensor changes", Toast.LENGTH_SHORT).show();
                                    Sensor sensor = event.sensor;
                                    float[] values = event.values;
                                    //int value = -1;

                                    if (values.length > 0) {
                                        stepvalue = (int) values[0];
                                    }

                                    if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                                        TextView t5 = (TextView) findViewById(R.id.textView_steps);
                                        t5.setText("       Steps          : " + stepvalue);
                                    } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                                        // For test only. Only allowed value is 1.0 i.e. for step taken
                                        TextView t5 = (TextView) findViewById(R.id.textView_steps);
                                        t5.setText("       Steps          : " + stepvalue);
                                    }
                                }*/


                                @Override
                                public void onProviderDisabled(String provider) {
                                    // TODO Auto-generated method stub
                                }

                                @Override
                                public void onProviderEnabled(String provider) {
                                    // TODO Auto-generated method stub
                                }

                                @Override
                                public void onStatusChanged(String provider, int status,
                                                            Bundle extras) {
                                    // TODO Auto-generated method stub
                                }


                            });
                            //}

                        }
                    }
                }

                    /********Created By: Srabonti Chakraborty**************/
                //update timer when start button is ON
                private Runnable updateTimerThread = new Runnable() {
                    public void run() {
                        timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
                        updatedTime = timeSwapBuff + timeInMilliseconds;
                        int secs = (int) (updatedTime / 1000);
                        int mins = secs / 60;
                        secs = secs % 60;
                        int hrs = mins / 60;
                        //int milliseconds = (int) (updatedTime % 1000);
                        timerValue = "" + String.format("%02d", hrs) + ":" + String.format("%02d", mins) + ":"
                                + String.format("%02d", secs);
                        customHandler.postDelayed(this, 0);
                        TextView t2 = (TextView) findViewById(R.id.textView_duration);
                        t2.setText("       Duration     :  " + timerValue);

                    }
                };

            });

            //Onclick event of Trip Log Button Go to trip log page
            ImageButton tripButton = (ImageButton) findViewById(R.id.toTripLog);
            tripButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(MapsActivity.this, "trip button", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapsActivity.this, TripLog.class);
                    //Toast.makeText(MapsActivity.this, "trip button???", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    //Toast.makeText(MapsActivity.this, "trip button?", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    //Toast.makeText(MapsActivity.this, "trip button??", Toast.LENGTH_SHORT).show();
                }
            });
            //Onclick event of Trip Statistics Button Go to trip Statistics page
            ImageButton statsButton = (ImageButton) findViewById(R.id.toStatistics);
            statsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MapsActivity.this, "stats button", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapsActivity.this, Statistics.class);
                    Bundle bundle = new Bundle();
                    startActivity(intent);
                }
            });

        }
    }

    /********Created By: Srabonti Chakraborty**************/
    //register unregister sensors
    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        stepvalue =0;
        //mSensorManager.registerListener(MapsActivity.this, mStepCounterSensor,SensorManager.SENSOR_DELAY_FASTEST);
        //mSensorManager.registerListener(MapsActivity.this, mStepDetectorSensor,SensorManager.SENSOR_DELAY_FASTEST);
        Sensor countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            mSensorManager.registerListener(MapsActivity.this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }
        else{
            Toast.makeText(MapsActivity.this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }

    }

    /********Created By: Srabonti Chakraborty**************/
    //register unregister sensors
    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
        // if you unregister the last listener, the hardware will stop detecting step events
        //sensorManager.unregisterListener(this);
        //stepvalue=0;
        //TextView t5 = (TextView) findViewById(R.id.textView_steps);
        //t5.setText("       Steps          : " + stepvalue);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            String step = String.valueOf(event.values[0]);
            stepvalue = (int)event.values[0];
            //Log.i("steps>>>>>>>>>>>>>", step);
            TextView t5 = (TextView) findViewById(R.id.textView_steps);
            t5.setText("       Steps          : " + stepvalue);
        }

    }


    /********Created By: Srabonti Chakraborty**************/
    //register unregister sensors
    /*@Override
    protected void onStop() {
        super.onStop();
        //mSensorManager.unregisterListener(this, mStepCounterSensor);
        //mSensorManager.unregisterListener(this, mStepDetectorSensor);
        //locationManager.removeUpdates(MapsActivity.this);
        //stepvalue=0;
        //TextView t5 = (TextView) findViewById(R.id.textView_steps);
        //t5.setText("       Steps          : " + stepvalue);
    }*/


    /********Created By: Srabonti Chakraborty**************/
    //resets all fields on the screen
    private void resetFields() {
        Toast.makeText(MapsActivity.this, "reset fields", Toast.LENGTH_SHORT).show();
        timerValue = "00:00:00";
        TextView t2 = (TextView) findViewById(R.id.textView_duration);
        t2.setText("       Duration     :  " + timerValue);
        distance =0.000;
        TextView t1 = (TextView) findViewById(R.id.textView_distance);
        t1.setText("       Distance     : " + String.format( "%.3f", distance) + " km");
        myspeed = 0.000;
        TextView t3 = (TextView) findViewById(R.id.textView_speed);
        t3.setText("       Speed         : " + String.format( "%.3f", myspeed) + " km/h");
        avgspeed = 0.000;
        TextView t4 = (TextView) findViewById(R.id.textView_Avgspeed);
        t4.setText("       Avg Speed  : " + avgspeed + " km/h");
        TextView t5 = (TextView) findViewById(R.id.textView_steps);
        //stepvalue =0;
        t5.setText("       Steps          : " + stepvalue);

        //reset map
        mMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        mMap.clear();
    }


    /********Created By: Srabonti Chakraborty**************/
    //shows the popup window at the end of the trip
    //And also displays trip summery
    private void showTripSummeryPopup(MapsActivity mapsActivity) {
        LayoutInflater inflater = (LayoutInflater) MapsActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.trip_summery_layout,
                (ViewGroup) findViewById(R.id.popup));

        pw = new PopupWindow(layout, 800, 800, true);
        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

        TextView t1 = (TextView) layout.findViewById(R.id.popup_distance);
        t1.setText("  Total Distance   : " + String.format( "%.3f", distance) + " km");
        t1.setGravity(Gravity.CENTER_VERTICAL);
        TextView t2 = (TextView) layout.findViewById(R.id.popup_duration);
        t2.setText("  Total Duration   : " + save_timer);
        t2.setGravity(Gravity.CENTER_VERTICAL);
        TextView t3 = (TextView) layout.findViewById(R.id.popup_avgspeed);
        t3.setText("  Avg Speed      : " + String.format( "%.3f", avgspeed) + " km/h");
        t3.setGravity(Gravity.CENTER_VERTICAL);
        TextView t4 = (TextView) layout.findViewById(R.id.popup_steps);
        t4.setText("  Steps Taken    : " + stepvalue);

        Button buttoncancel=(Button)layout.findViewById(R.id.button_close);
        buttoncancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //save trip summery in file
                //saveTripSummery(save_timer, distance, stepvalue, avgspeed);
                saveTripSummery();
                pw.dismiss();


            }

        });
    }

    /********Created By: Srabonti Chakraborty**************/
    //this method saves the trip summery in the file
    //private void saveTripSummery(String save_timer, double distance, int stepvalue, double avgspeed) {
    private void saveTripSummery() {
        //format variable to String
        String file_duration = save_timer.toString();
        String file_distance = Double.toString(distance);
        String file_numofsteps = Integer.toString(stepvalue);
        String file_avgspeed = Double.toString(avgspeed);
        //get current date and time
        String file_date = getDateTime();
        Toast.makeText(MapsActivity.this, file_date, Toast.LENGTH_SHORT).show();

        FileOperations fileIO = new FileOperations();
        //create string to store in file
        String lineToWrite = file_duration + "|" + file_distance + "|" + file_numofsteps + "|" + file_avgspeed + "|" + file_date + "|";

        boolean flg = false;
        try {
            flg = fileIO.saveToDisk( Environment.getExternalStorageDirectory(), lineToWrite );
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(flg)
        {
            Toast.makeText(getBaseContext(), "Trip Saved!", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }

    /********Created By: Srabonti Chakraborty**************/
    //this method retrieves the current date and time
    private String getDateTime() {
        Calendar cal = Calendar.getInstance();
        int yy = cal.get(Calendar.YEAR);
        int mm = cal.get(Calendar.MONTH);
        int dd = cal.get(Calendar.DAY_OF_MONTH);
        String st_mm;
        String st_dd;

        if (mm+1 < 10){
            st_mm = "0"+ Integer.toString(mm+1);
        }
        else{
            st_mm = Integer.toString(mm);
        }

        if(dd < 10){
            st_dd = "0"+ Integer.toString(dd);
        }
        else{
            st_dd = Integer.toString(dd);
        }
        StringBuilder todays_date = new StringBuilder().append(yy).append("-").append(st_mm).append("-").append(st_dd);

        return todays_date.toString();
    }

    /********Created By: Srabonti Chakraborty**************/
    //calculates distance covered
    private double distanceCovered(double prev_lat, double prev_lng, double new_lat, double new_lng) {
        float[] result = new float[1];
        double distance_temp;
        Log.i("prev_lat", String.valueOf(prev_lat));
        Log.i("new_lat", String.valueOf(new_lat));
        int R = 6371;
        double x = (new_lng - prev_lng) * Math.cos((new_lat + prev_lat) / 2);
        double y = (new_lat - prev_lat);
        distance_temp = Math.sqrt(x * x + y * y) * R;
        Log.i("distance", String.valueOf(distance));
        return distance_temp;


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap = googleMap;
        Toast.makeText(MapsActivity.this, "map ready show", Toast.LENGTH_SHORT).show();
        Log.i("map ready", "map ready");
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    protected boolean isRouteDisplayed()
    {
        return true;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}
