package com.example.carolinecrossland.hoverchairapp;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


public class MainActivity extends AppCompatActivity {

    private TabLayout tablayout;
    private ConstraintLayout constraintlayout;
    private ViewPager viewpager;
    private Switch selfBalanceSwitch;

    ImageView battery;
    Handler handler;
    Runnable runnable;
    static boolean status;
    LocationManager locationManager;
    static TextView dist, time, speed;
    Button start, pause, stop;
    static long startTime, endTime;
    ImageView image;
    static ProgressDialog locate;
    static int p = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tablayout = findViewById(R.id.tab_layout_cs);
        constraintlayout = findViewById(R.id.constraint_layout_cs);
        viewpager = findViewById(R.id.view_pager_cs);
        selfBalanceSwitch = findViewById(R.id.self_ballance_switch);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        speed = findViewById(R.id.speedText);
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


        //adding fragments
        adapter.addFragment(new FragmentCurrentTrip(), "Today");
        adapter.addFragment(new FragmentPrevTrip(), "Previous");
        adapter.addFragment(new FragmentCumulativeTrip(), "Cumulative");

        //adapter setup
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
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
}
