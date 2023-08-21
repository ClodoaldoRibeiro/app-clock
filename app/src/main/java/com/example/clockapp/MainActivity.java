package com.example.clockapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewHolder.textHourMinutes = findViewById(R.id.text_hour_minute);
        mViewHolder.textSeconds = findViewById(R.id.text_seconds);
        mViewHolder.textBattery = findViewById(R.id.text_battery);
    }


    private static class ViewHolder {
        TextView textHourMinutes;
        TextView textSeconds;
        TextView textBattery;
    }
}