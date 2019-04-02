package com.example.carolinecrossland.hoverchairapp;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Drive extends AppCompatActivity {

    BluetoothService mService;
    boolean mBound = false;

    public enum Mode { directcontrol, selfbalance, zerodegree }

    Mode mode = Mode.directcontrol;

    private static final float MAX_BUG_SPEED_DP_PER_S = 100;
    private boolean engaged = false;


    private SeekBar velocityBar;
    private SeekBar rotationBar;
    int rotationMin = 0;
    int rotationMax = 100;
    int velocityMin = 0;
    int velocityMax = 100;

    int rotation = 0;
    int velocity = 0;

    TextView speed;
    TextView turn;

    RadioGroup radioGroup;
    ToggleButton toggleButton;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        //bind bluetooth service
        Intent intent = new Intent(this, BluetoothService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        rotationBar = findViewById(R.id.turnSeekBar);
        rotationBar.setMax(rotationMax);
        rotationBar.setProgress(rotationMax/2);

        velocityBar = findViewById(R.id.driveSeekBar);
        velocityBar.setMax(velocityMax);
        velocityBar.setProgress(velocityMax/2);

        speed = findViewById(R.id.driveSpeedDisplay);
        turn = findViewById(R.id.turnTextView);

        rotationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rotation = progress - rotationMax/2;
                turn.setText(Integer.toString(rotation) + "°");
                sendData();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                rotationBar.setProgress(rotationMax/2);
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                rotationBar.setProgress(rotationMax/2);
            }
        });

        velocityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                velocity = progress - velocityMax/2;
                speed.setText(Integer.toString(velocity));
                sendData();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                velocityBar.setProgress(velocityMax/2);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                velocityBar.setProgress(velocityMax/2);
            }
        });


        //handle control radio button group
        radioGroup = findViewById(R.id.radioControlModes);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioDirectControl:
                        mode = Mode.directcontrol;
                        break;
                    case R.id.radioSelfBalance:
                        mode = Mode.selfbalance;
                        break;
                    case R.id.radio0DegreeTurn:
                        mode = Mode.zerodegree;
                        break;
                }
                sendData();
            }
        });

        //handle toggle button
        toggleButton = findViewById(R.id.toggleEngagement);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    engaged = true;
                } else {
                    engaged = false;
                }
                sendData();
            }
        });

        //nav view listener
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent = MenuManager.selectDrawerItem(Drive.this, menuItem);
                startActivity(intent);
                return true;
            }
        });

    }

    String constructDataMessage() {
        String message = "";
        //engagement
        if(engaged) {
            message += "1";
        } else {
            message += "0";
        }
        //control mode
        if(mode == Mode.directcontrol) {
            message += "0";
        } else if(mode == Mode.selfbalance) {
            message += "1";
        } else {
            message += "2";
        }
        //left and right triggers (velocity)
        if(velocity >= 0) {
            message += String.format("%03d", Math.abs(velocity));
            message += "000";
        } else {
            message += "000";
            message += String.format("%03d", Math.abs(velocity));
        }
        //y and x joystick positioning
        message += "+000";
        if(rotation > 0) {
            message += "+";
        } else {
            message += "-";
        }
        message += String.format("%03d", Math.abs(rotation));
        return message;
    }

    private void sendData() {
        String message = constructDataMessage();
        mService.send(message);
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            System.out.println("onServiceConnected");
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BluetoothService.LocalBinder binder = (BluetoothService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
