package com.example.clockapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Runnable mRunnable;
    private ViewHolder mViewHolder = new ViewHolder();
    final Handler mHandler = new Handler();
    private boolean mTicker;
    private boolean mHasLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mViewHolder.textHourMinutes = findViewById(R.id.text_hour_minute);
        mViewHolder.textSeconds = findViewById(R.id.text_seconds);
        mViewHolder.textBattery = findViewById(R.id.text_battery);
        mViewHolder.textNight = findViewById(R.id.text_night);


        mViewHolder.textHourMinutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSystemUI();
            }
        });

    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mTicker = true;
        this.startClock();
        this.registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        this.mHasLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mTicker = false;
        this.unregisterReceiver(mReceiver);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
            );
        }
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

            mViewHolder.textBattery.setText(String.format("Baterry level %s%%", level));
        }
    };

    private void startClock() {
        Calendar calendar = Calendar.getInstance();
        this.mRunnable = new Runnable() {
            @Override
            public void run() {
                if (!mTicker) {
                    return;
                }

                calendar.setTimeInMillis(System.currentTimeMillis());

                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);

                if (mHasLandscape) {
                    if (hour > 18) {
                        mViewHolder.textNight.setVisibility(View.VISIBLE);
                    } else {
                        mViewHolder.textNight.setVisibility(View.GONE);
                    }
                }

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
        TextView textNight;
    }
}