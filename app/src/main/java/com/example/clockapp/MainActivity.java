package com.example.clockapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Runnable mRunnable;
    private ViewHolder mViewHolder = new ViewHolder();
    final Handler mHandler = new Handler();

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

        this.registerReceiver(this.mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        this.startClock();
    }

    private void startClock() {
        Calendar calendar = Calendar.getInstance();
        this.mRunnable = new Runnable() {
            @Override
            public void run() {
                calendar.setTimeInMillis(System.currentTimeMillis());

                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);

                mViewHolder.textHourMinutes.setText(
                        String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
                );

                mViewHolder.textSeconds.setText(
                        String.format(Locale.getDefault(), "%02d", second)
                );

                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - (now % 1000));
                mHandler.postAtTime(mRunnable, next);
            }
        };
        this.mRunnable.run();
    }


    private static class ViewHolder {
        TextView textHourMinutes;
        TextView textSeconds;
        TextView textBattery;
    }
}