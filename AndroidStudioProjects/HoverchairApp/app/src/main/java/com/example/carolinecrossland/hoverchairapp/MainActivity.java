package com.example.carolinecrossland.hoverchairapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
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

//added
import android.app.NotificationManager;
import android.app.PendingIntent;



public class MainActivity extends AppCompatActivity {

    private TabLayout tablayout;
    private ConstraintLayout constraintlayout;
    private ViewPager viewpager;
    private Switch selfBalanceSwitch;

    //Here
    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

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


    //Here
    NotificationCompat.Builder notification;
    private static final int uniqueID = 45612; //phone needs id to keep track of all notif


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
        selfBalanceSwitch = findViewById(R.id.self_ballance_switch);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        speed = findViewById(R.id.speedText);
        start = findViewById(R.id.start);
        battery = findViewById(R.id.battery);
        handler = new Handler();


        //Here
        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true); //need this to make notification go away


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

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //The method below checks if Location is enabled on device or not. If not, then an alert dialog box appears with option
                //to enable gps.
                checkGps();
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    return;
                }


                if (status == false)
                    //Here, the Location Service gets bound and the GPS Speedometer gets Active.
                    bindService();
                locate = new ProgressDialog(MainActivity.this);
                locate.setIndeterminate(true);
                locate.setCancelable(false);
                locate.setMessage("Getting Location...");
                locate.show();
                start.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                pause.setText("Pause");
                stop.setVisibility(View.VISIBLE);
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

    public float batteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if (level == -1 || scale == -1) {
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

    public void testNotifyMe(View view){
        notifyMe(view, "meesooge", "teetle");
    }

    //Here
    public void notifyMe(View view, String message, String title) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notifyId = 1;
        String channelId = "some_channel_id";
        CharSequence channelName = "Some Channel";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://hoverchair.com/main"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            notificationManager.createNotificationChannel(notificationChannel);

            Notification notification = new Notification.Builder(MainActivity.this)
                    .setContentTitle(message)
                    .setContentText(title)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setChannelId(channelId)
                    .setContentIntent(pendingIntent)
                    .build();

            notificationManager.notify(notifyId, notification);
        }



    }
}
