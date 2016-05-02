package com.example.user.demo_gps;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
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

import java.util.ArrayList;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

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
    private int start_stop_flag = 0; //start = 1, stop =0
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Creation of main activity toolbar
        // Must be linked in with Maps fragment
        Toolbar top = (Toolbar) findViewById(R.id.view);
        //top.setMenu();
        top.setLogo(R.drawable.ic_launcher);
        top.setTitle("Walk-A-Lot");
        top.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(),
                        "Contact Added", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();


        //MapFragment mapFragment = (MapFragment) getFragmentManager()
        //     .findFragmentById(R.id.map);
        //mMap = mapFragment
        //mapFragment.getMapAsync(this);
        //LatLng sydney = new LatLng(-34, 151);
        TextView t1 = (TextView) findViewById(R.id.textView_distance);
        t1.setText("       Distance: ");
        TextView t2 = (TextView) findViewById(R.id.textView_duration);
        t2.setText("       Duration: ");
        TextView t3 = (TextView) findViewById(R.id.textView_speed);
        t3.setText("       Speed: ");
        TextView t4 = (TextView) findViewById(R.id.textView_Avgspeed);
        t4.setText("       Average Speed: ");



        if (mMap != null) {
            //start stop button click
            Button button = (Button) findViewById(R.id.button_start_stop);
            button.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
                  Toast.makeText(MapsActivity.this, "button clicked", Toast.LENGTH_SHORT).show();
                  if (start_stop_flag == 0){
                      start_stop_flag =1;  //start
                      Toast.makeText(MapsActivity.this, "start", Toast.LENGTH_SHORT).show();
                  }
                  else {
                      start_stop_flag =0;
                      Toast.makeText(MapsActivity.this, "stop", Toast.LENGTH_SHORT).show();
                  }
            ImageButton tripButton = (ImageButton) findViewById(R.id.toTripLog);
            tripButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MapsActivity.this, "trip button", Toast.LENGTH_SHORT).show();
                }
            });
            ImageButton statsButton = (ImageButton) findViewById(R.id.toStatistics);
            statsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MapsActivity.this, "stats button", Toast.LENGTH_SHORT).show();
                }
            });


            Toast.makeText(MapsActivity.this, "before if", Toast.LENGTH_SHORT).show();
            if (start_stop_flag == 1) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Toast.makeText(MapsActivity.this, "location manager", Toast.LENGTH_SHORT).show();
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
                            Marker m = mMap.addMarker(new MarkerOptions().position(here).title("Here"));
                            prev_lat = new_lat;
                            prev_lng = new_lng;
                            firstPoint_flag = 1;
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(here, 17));
                        //draw trail
                    /*for (int z = 0; z < routePoints.size(); z++) {
                        LatLng point = routePoints.get(z);
                        pOptions.add(point);
                    }
                    //line = googleMap.addPolyline(pOptions);
                    line = mMap.addPolyline(pOptions);
                    routePoints.add(here);*/
                        PolylineOptions options = new PolylineOptions();
                        options.add(new LatLng(prev_lat, prev_lng));
                        options.add(new LatLng(new_lat, new_lng));
                        options.width(15);
                        options.color(Color.YELLOW);
                        mMap.addPolyline(options);
                        prev_lat = new_lat;
                        prev_lng = new_lng;

                    }

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

            }
              }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_maps, menu);
        return true;
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


}
