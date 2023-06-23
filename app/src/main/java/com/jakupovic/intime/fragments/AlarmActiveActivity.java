package com.jakupovic.intime.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jakupovic.intime.R;
import com.jakupovic.intime.alarmBroadcastReciever.AlarmBroadCastReceiver;
import com.jakupovic.intime.alarmBroadcastReciever.AlarmService;
import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.dataBase.InTimeDataBase;
import com.jakupovic.intime.databinding.ActivityAlarmActiveBinding;
import com.jakupovic.intime.interfaces.AndroidOSAlarmManager;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    public Alarm alarmReference=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_active);

        //get resources
        alarmReference=AlarmService.getAlarmInstance();
        alarmTitle = (TextView) findViewById(R.id.alarmTitleDisplayer);
        alarmTime = (TextView) findViewById(R.id.alarmTimeDisplayer);
        alarmDesc = (TextView) findViewById(R.id.alarmDescDisplayer);
        alarmCancelButton = (Button) findViewById(R.id.cancelAlarmButton);
        alarmRescheduleButton = (Button) findViewById(R.id.reRegisterAlarmButton);



        this.database = MainActivity.database;
        if (database == null) {
            database = Room.databaseBuilder(this.getApplicationContext(), InTimeDataBase.class, "InTime-db").build();
        }
        alarmCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm(v);
                finish();

            }
        });

        alarmRescheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarmForTomorrow(v);
                finish();
            }
        });


        PopulateWithData(alarmReference);
    }

    /**
     * method queries the DB and populates the screen with the alarmReference
     * @param alarmReference  - ref to alarm from DB
     * */
    void PopulateWithData(Alarm alarmReference){


        if(alarmReference!=null) {
            //fill the UI fields
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(alarmReference.localStartTime);
            alarmTitle.setText(alarmReference.alarmTitle);
            alarmTime.setText(new SimpleDateFormat("HH:mm").format(cal.getTime()));
            alarmDesc.setText(alarmReference.alarmDesc);
        }
    }
    /**
     * override of onBackPressed method, makes the activity ignore the back press button
     */
    @Override
    public void onBackPressed() {

    }

    void cancelAlarm(View v){
        alarmReference.enabled=false;
        CancelAlarmAsync(alarmReference);
        //get intent and stop alarm service
        Intent AlarmServiceIntent =new Intent(getApplicationContext(),AlarmService.class);
        getApplicationContext().stopService(AlarmServiceIntent);
    }

    void setAlarmForTomorrow(View v)
    {
        //get intent and stop alarm service
        Intent AlarmServiceIntent =new Intent(getApplicationContext(),AlarmService.class);
        getApplicationContext().stopService(AlarmServiceIntent);
        //register alarm for tomorrow
        AndroidOSAlarmManager.UnregisterAlarm(alarmReference,(AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE),getApplicationContext());
        AndroidOSAlarmManager.RegisterAlarm(alarmReference,(AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE),getApplicationContext());
    }
/**
 * method takes an alarm and changes its active state in the DB to false and unregisters it from the alarm manager
 * @param alarm  - alarm to cancel
 * */
    public void CancelAlarmAsync(Alarm alarm){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());


        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work


                    database.alarmDAO().updateAlarm(alarm);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            AndroidOSAlarmManager.UnregisterAlarm(alarm, (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE),getApplicationContext());

                        }
                    });



            }
        });

    }
}