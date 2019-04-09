package com.example.carolinecrossland.hoverchairapp;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.HandlerThread;
import android.os.IBinder;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.BluetoothCallback;
import me.aflak.bluetooth.DeviceCallback;

import static java.lang.Thread.sleep;


public class BluetoothService extends Service {

    private final IBinder binder = new LocalBinder();
    public static Bluetooth bluetooth = null;
    public static BluetoothDevice device = null;

    public class LocalBinder extends Binder {
        BluetoothService getService() {
            // Return this instance of BluetoothService so clients can call public methods
            return BluetoothService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Bluetooth service started");
        return startId;
    }

    public synchronized boolean bindBluetooth(Bluetooth _bluetooth, BluetoothDevice _device, Activity activity) {
        bluetooth = _bluetooth;
        device = _device;
        System.out.println("Binding bluetooth");
        bluetooth = MyApp.app().bluetooth();
        this.bluetooth.onStart();
        this.bluetooth.setCallbackOnUI(activity);
        this.bluetooth.setBluetoothCallback(bluetoothCallback);
        bluetooth.connectToDevice(device);
        bluetooth.setDeviceCallback(communicationCallback);

        try {
            sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return bluetooth.isConnected();
    }

    public void send(String message) {
        if(device != null && bluetooth.isConnected()) {
            System.out.println(message);
            bluetooth.send(message);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return binder;
    }

    @Override
    public void onCreate() {

    }


    private BluetoothCallback bluetoothCallback = new BluetoothCallback() {
        @Override
        public void onBluetoothTurningOn() {

        }

        @Override
        public void onBluetoothOn() {

        }

        @Override
        public void onBluetoothTurningOff() {

        }

        @Override
        public void onBluetoothOff() {
        }

        @Override
        public void onUserDeniedActivation() {
        }
    };

    private DeviceCallback communicationCallback = new DeviceCallback() {
        @Override
        public void onDeviceConnected(BluetoothDevice device) {
        }

        @Override
        public void onDeviceDisconnected(BluetoothDevice device, String message) {
        }

        @Override
        public void onMessage(String message) {

        }

        @Override
        public void onError(String message) {

        }

        @Override
        public void onConnectError(final BluetoothDevice device, String message) {
            System.out.println("failed to connect");
        }

    };


}
