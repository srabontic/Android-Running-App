package com.example.user.demo_gps;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class options_drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * Options Drawer: Rupesh
     * Provides a navigation drawer, which allows for the user to access the Trip Log and Statistics page
     * The navigation drawer is used in lieu of action bar buttons as buttons can be accidentally pressed
     * when the user is hiking or running.
     * The navigation drawer requires a deliberate action by the user to open.
     */


    /**
     * onCreate method: initializes the navigation drawer as well as helper .xml files
     * such as the navigation drawer header, action bar, and content.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.bringToFront();
        drawer.requestLayout();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // On back button press, navigation drawer closes
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onDrawerOpened(View drawerView) {
        drawerView.bringToFront();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Method handles the selection of the nav drawer options
    // Will open statistics or trip log activities when clicked
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.bringToFront();
        drawer.requestLayout();
        drawer.closeDrawer(GravityCompat.START);

        int id = item.getItemId();
        Toast.makeText(getApplicationContext(),
                "YOU TOUCHED THE NAV DRAWER", Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),
                item.toString(), Toast.LENGTH_LONG).show();

        if (id == R.id.Trip_Log) {
            Toast.makeText(getApplicationContext(),
                    "TRIP LOG", Toast.LENGTH_LONG).show();
            Intent tlActivity= new Intent(this, TripLog.class);
            startActivity(tlActivity);
            return true;
        } else if (id == R.id.Statistics) {
            Toast.makeText(getApplicationContext(),
                    "Statistics", Toast.LENGTH_LONG).show();
            Intent sActivity= new Intent(this, Statistics.class);
            startActivity(sActivity);
            return true;
        }


        return true;
    }
}
