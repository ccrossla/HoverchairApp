package com.example.carolinecrossland.hoverchairapp;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.*;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarDrawerToggle;



public class MainActivity extends AppCompatActivity {

    private TabLayout tablayout;
    private ConstraintLayout constraintlayout;
    private ViewPager viewpager;
    private Switch selfBalanceSwitch;


    DrawerLayout drawerLayout;
    NavigationView navigationView;


    ImageView battery;
    Handler handler;
    Runnable runnable;
    LocationService myService;
    static boolean status;
    LocationManager locationManager;
    static TextView dist, time, speed;
    Button start, pause, stop;
    static long startTime, endTime;
    ImageView image;
    static ProgressDialog locate;
    static int p = 0;

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            myService = binder.getService();
            status = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            status = false;
        }
    };

    void bindService() {
        if (status == true)
            return;
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        bindService(i, sc, BIND_AUTO_CREATE);
        status = true;
        startTime = System.currentTimeMillis();
    }

    void unbindService() {
        if (status == false)
            return;
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        unbindService(sc);
        status = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (status == true)
            unbindService();
    }

    @Override
    public void onBackPressed() {
        if (status == false)
            super.onBackPressed();
        else
            moveTaskToBack(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tablayout = findViewById(R.id.tab_layout_cs);
        constraintlayout = findViewById(R.id.constraint_layout_cs);
        viewpager = findViewById(R.id.view_pager_cs);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        start = findViewById(R.id.start);
        battery = findViewById(R.id.battery);
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                int level = (int) batteryLevel();

                if (level > 75) {
                    battery.setImageResource(R.drawable.battery_proto_full);
                }
                if (level > 50 && level <= 75) {
                    battery.setImageResource(R.drawable.battery_proto_half);
                }
                if (level < 50 && level >= 0) {
                    battery.setImageResource(R.drawable.battery_proto_low);
                }

                handler.postDelayed(runnable, 5000);
            }

        };

        handler.postDelayed(runnable, 0);


        //navigation drawer setup
//        drawerLayout = findViewById(R.id.drawer_layout);
//        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
//            @Override
//            public void onDrawerStateChanged(int newState) {
//                System.out.println("DRAWER STATE CHANGED: "+newState);
//            }
//
//        });
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                selectDrawerItem(menuItem);
                Intent intent = MenuManager.selectDrawerItem(MainActivity.this, menuItem);
                startActivity(intent);
                return true;
            }
        });




        //adding fragments
        adapter.addFragment(new FragmentCurrentTrip(), "Today");
        adapter.addFragment(new FragmentPrevTrip(), "Previous");
        adapter.addFragment(new FragmentCumulativeTrip(), "Cumulative");

        //adapter setup
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
    }





    public void selectDrawerItem(MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.bt:
                intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);
                break;
            case R.id.drive:
                intent = new Intent(MainActivity.this, Drive.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }











    public float batteryLevel(){
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if(level == -1 || scale == -1){
            return 50.0f;
        }

        return ((float) level / (float) scale) * 100.0f;
    }

    //This method leads you to the alert dialog box.
    void checkGps() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


            showGPSDisabledAlertToUser();
        }
    }

    //This method configures the Alert Dialog box.
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enable GPS to use application")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}
