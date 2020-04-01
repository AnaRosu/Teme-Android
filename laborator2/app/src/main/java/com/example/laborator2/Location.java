package com.example.laborator2;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Location extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //request permission for location access
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);


        TextView textView_long = (TextView) findViewById(R.id.testView_longitude);
        TextView textView_lat = (TextView) findViewById(R.id.textView_latitude);


        LocListener ll = new LocListener(getApplicationContext());
        android.location.Location l =ll.getLocation();
        if (l != null){
            double lat = l.getLatitude();
            double lon = l.getLongitude();
            textView_lat.setText("Latitudine: " + String.valueOf(lat));
            textView_long.setText("Longitudine: " +String.valueOf(lon));
        }
    }
}
