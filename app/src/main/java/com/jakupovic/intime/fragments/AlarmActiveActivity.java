package com.jakupovic.intime.fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jakupovic.intime.R;

/**
 * this activity represents the window which opens when an alarm activates itself
 * */
public class AlarmActiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_active);
    }
}