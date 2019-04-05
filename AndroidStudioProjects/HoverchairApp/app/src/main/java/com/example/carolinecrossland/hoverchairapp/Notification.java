package com.example.carolinecrossland.hoverchairapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Notification extends AppCompatActivity {

    String messBody = "Message body here.";
    String messTitle = "Message Title";

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //get reference of widgets
        final ListView lv = (ListView) findViewById(R.id.lv);


        //List to hold data
        List<Map<String,String>> info = new ArrayList<Map<String,String>>();

        String[] notifs = new String[] {};

        //List from String of notifs
        final List<String>notifs_list = new ArrayList<String>(Arrays.asList(notifs));


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, notifs_list){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(getResources().getColor(R.color.csTextColor));

                // Generate ListView Item using TextView
                return view;
            }

        };

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                notifs_list.remove(position);
                arrayAdapter.notifyDataSetChanged();
            }
        });

        //nav view listener
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent intent = MenuManager.selectDrawerItem(Notification.this, menuItem);
                startActivity(intent);
                return true;
            }
        });


        //initialize sample notifications
        for(int i = 0; i < 3; i++) {
            notifs_list.add(messBody);
            arrayAdapter.notifyDataSetChanged();
        }
    }
}