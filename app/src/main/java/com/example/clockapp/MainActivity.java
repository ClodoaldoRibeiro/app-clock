package com.example.clockapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ViewHolder mViewHolder = new ViewHolder();
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            mViewHolder.textBattery.setText(String.format("Baterry level %s%%", level));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewHolder.textHourMinutes = findViewById(R.id.text_hour_minute);
        mViewHolder.textSeconds = findViewById(R.id.text_seconds);
        mViewHolder.textBattery = findViewById(R.id.text_battery);


        this.registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }


    private static class ViewHolder {
        TextView textHourMinutes;
        TextView textSeconds;
        TextView textBattery;
    }
}