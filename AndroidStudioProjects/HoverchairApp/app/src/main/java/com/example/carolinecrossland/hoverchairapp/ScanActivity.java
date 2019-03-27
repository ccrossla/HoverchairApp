package com.example.carolinecrossland.hoverchairapp;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
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
    BluetoothDevice device;

    BluetoothService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        bluetooth = MyApp.app().bluetooth();

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
                device = devices.get(i);
                startBluetoothService();
            }
        });
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
            connectToDevice();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private void startBluetoothService() {
        Intent intent = new Intent(this, BluetoothService.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }

    private void connectToDevice() {
        mService.bindBluetooth(bluetooth, device, this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (connection != null) {
            unbindService(connection);
        }
    }

}

