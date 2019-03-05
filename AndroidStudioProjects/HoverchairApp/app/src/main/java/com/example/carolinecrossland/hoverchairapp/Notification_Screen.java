package com.example.carolinecrossland.hoverchairapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Notification_Screen extends AppCompatActivity {

    String messBody = MainActivity.getMessage();
    String messTitle = MainActivity.getMessTitle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification__screen);

        //get reference of widgets
        final ListView lv = (ListView) findViewById(R.id.lv);
        final Button btn = (Button) findViewById(R.id.btn);

        //List to hold data
        List<Map<String,String>> info = new ArrayList<Map<String,String>>();

        String[] notifs = new String[] {};

        //List from String of notifs
        final List<String>notifs_list = new ArrayList<String>(Arrays.asList(notifs));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, notifs_list);

        lv.setAdapter(arrayAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                notifs_list.add(messBody);
                arrayAdapter.notifyDataSetChanged();
            }
        });

    }
}
