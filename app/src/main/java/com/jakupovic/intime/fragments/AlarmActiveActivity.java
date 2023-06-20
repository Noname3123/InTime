package com.jakupovic.intime.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.jakupovic.intime.R;
import com.jakupovic.intime.dataBase.InTimeDataBase;

/**
 * this activity represents the window which opens when an alarm activates itself
 * */
public class AlarmActiveActivity extends AppCompatActivity {

    //ui elements
    private TextView alarmTitle;
    private TextView alarmTime;
    private TextView alarmDesc;
    private Button alarmCancelButton;
    private Button alarmRescheduleButton;

    //database
    private InTimeDataBase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_active);
        this.database=MainActivity.database;
        if(database==null){
            database= Room.databaseBuilder(this.getApplicationContext(),InTimeDataBase.class,"InTime-db").build();
        }
    }

    /**
     * override of onBackPressed method, makes the activity ignore the back press button*/
    @Override
    public void onBackPressed(){

    }
}