package com.example.carolinecrossland.hoverchairapp;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class Drive extends AppCompatActivity {

    public enum Engagement { disengaged, engaged }
    public enum Mode { directcontrol, selfbalance, zerodegree }

    Engagement engagement = Engagement.disengaged;
    Mode mode = Mode.directcontrol;

    private static final float MAX_BUG_SPEED_DP_PER_S = 100;
    private boolean engaged = true;


    private SeekBar velocityBar;
    private SeekBar rotationBar;
    int rotationMin = 0;
    int rotationMax = 100;
    int velocityMin = 0;
    int velocityMax = 100;

    int rotation = 0;
    int velocity = 0;

    TextView speed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        rotationBar = findViewById(R.id.turnSeekBar);
        rotationBar.setMax(rotationMax);
        rotationBar.setProgress(rotationMax/2);

        velocityBar = findViewById(R.id.driveSeekBar);
        velocityBar.setMax(velocityMax);
        velocityBar.setProgress(velocityMax/2);

        speed = findViewById(R.id.driveSpeedDisplay);

        rotationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rotation = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                rotationBar.setProgress(rotationMax/2);
            }
        });

        velocityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                velocity = progress;
                speed.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                rotationBar.setProgress(rotationMax/2);
            }
        });
    }
}
