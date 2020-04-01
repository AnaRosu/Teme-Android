package com.example.laborator2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class SensorsActivity extends AppCompatActivity{
    List<Sensor> sensors;
    SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        ListView list = (ListView) findViewById(R.id.list_sensors);
        list.setAdapter(new MySensorsAdapert(this, R.layout.row_sensor,sensors));
    }


    class MySensorsAdapert extends ArrayAdapter<Sensor>{
        private int textViewResourceId;
        MySensorsAdapert(Context context, int textViewResourceId, List<Sensor> items){
            super(context,textViewResourceId,items);
            this.textViewResourceId = textViewResourceId;
        }

        public View getView(int position, View contextView, ViewGroup parent){
            contextView = LayoutInflater.from(this.getContext()).inflate(textViewResourceId,parent,false);
            TextView textView_sensor = (TextView)contextView.findViewById(R.id.textView_sensor);

            Sensor item = getItem(position);
            if(item != null){
                textView_sensor.setText("Name: " + item.getName() + " | Int Type: " + item.getType() + " | Vendor: " + item.getVendor() + " | Version: " + item.getVersion()  + " | Power: " + item.getPower()  + " | Max range: " + item.getMaximumRange());
            }
            return contextView;
        }

    }
}
