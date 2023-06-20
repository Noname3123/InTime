package com.jakupovic.intime.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.jakupovic.intime.R;
import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.dataBase.InTimeDataBase;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

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
    //ref to alarm
    private Alarm alarmReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get resources
        alarmTitle=(TextView)findViewById(R.id.alarmTitleDisplayer);
        alarmTime=(TextView)findViewById(R.id.alarmTimeDisplayer);
        alarmDesc=(TextView)findViewById(R.id.alarmDescDisplayer);
        alarmCancelButton=(Button)findViewById(R.id.cancelAlarmButton);
        alarmRescheduleButton=(Button)findViewById(R.id.reRegisterAlarmButton);
        //TODO: register alarm functions


        setContentView(R.layout.activity_alarm_active);
        Intent intent =getIntent();
        alarmReference=(Alarm) intent.getSerializableExtra("ALARM_INSTANCE");
        this.database=MainActivity.database;
        if(database==null){
            database= Room.databaseBuilder(this.getApplicationContext(),InTimeDataBase.class,"InTime-db").build();
        }
        Calendar cal=Calendar.getInstance();
        cal.setTimeInMillis(alarmReference.localStartTime);
        alarmTitle.setText(alarmReference.alarmTitle);
        alarmTime.setText(new SimpleDateFormat("HH:mm").format(cal.getTime()));
        alarmDesc.setText(alarmReference.alarmDesc);
    }

    /**
     * override of onBackPressed method, makes the activity ignore the back press button*/
    @Override
    public void onBackPressed(){

    }
}