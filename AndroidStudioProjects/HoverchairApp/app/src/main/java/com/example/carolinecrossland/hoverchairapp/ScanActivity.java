package com.example.carolinecrossland.hoverchairapp;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.*;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.BluetoothCallback;
import me.aflak.bluetooth.DeviceCallback;

public class ScanActivity extends AppCompatActivity {

    Bluetooth bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        bluetooth = MyApp.app().bluetooth();
        this.bluetooth.onStart();
        this.bluetooth.setCallbackOnUI(this);
        this.bluetooth.setBluetoothCallback(bluetoothCallback);

        final List<BluetoothDevice> devices = bluetooth.getPairedDevices();


        //Get list of paired devices
        System.out.println("num paired devices: " + devices.size());
        final List<HashMap<String, String>> pairedListItems = new ArrayList<>();
        for(BluetoothDevice device : devices) {
            System.out.print("device name: " + device.getName());
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("idNum", String.valueOf(pairedListItems.size()));
            hashMap.put("deviceName",device.getName());
            pairedListItems.add(hashMap);
        }
        //Set up list view
        ListView pairedListView = findViewById(R.id.activity_scan_paired_list);
        String[] from={"idNum","deviceName"};
        int[] to={R.id.deviceName,R.id.deviceName};
        SimpleAdapter adapter = new SimpleAdapter(this, pairedListItems, R.layout.list_view, from, to);
        pairedListView.setAdapter(adapter);

        //perform listView item click event
        pairedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("DEVICE NAME: " + pairedListItems.get(i).get("deviceName"));
                connectToDevice(devices.get(i));
            }
        });
    }

    private void connectToDevice(BluetoothDevice device) {
        bluetooth.onStart();
        bluetooth.connectToDevice(device);
        bluetooth.setDeviceCallback(communicationCallback);
        String isConnected = Boolean.toString(bluetooth.isConnected());
        System.out.println("isConnected: " + isConnected);
        if(bluetooth.isConnected()) {
            bluetooth.send("500");
        }
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

