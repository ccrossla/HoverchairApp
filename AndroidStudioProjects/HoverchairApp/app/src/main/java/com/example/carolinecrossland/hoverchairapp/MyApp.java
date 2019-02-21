package com.example.carolinecrossland.hoverchairapp;

import android.app.Application;
import me.aflak.bluetooth.Bluetooth;
import android.util.Log;

public class MyApp extends Application {
    private static MyApp app;
    private Bluetooth bluetooth;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        this.bluetooth = new Bluetooth(this);
        bluetooth.onStart();
        bluetooth.enable();
    }

    public static MyApp app() {
        return app;
    }

    public Bluetooth bluetooth() {
        return bluetooth;
    }
}