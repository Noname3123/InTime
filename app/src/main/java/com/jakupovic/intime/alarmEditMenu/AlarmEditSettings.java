package com.jakupovic.intime.alarmEditMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jakupovic.intime.R;
import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.dataBase.Clock;
import com.jakupovic.intime.dataBase.InTimeDataBase;
import com.jakupovic.intime.fragments.MainActivity;
import com.jakupovic.intime.interfaces.AndroidOSAlarmManager;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlarmEditSettings extends AppCompatActivity {

    private ImageButton alarmBackButton;
    private Button saveBtn;
    private TextView alarmTitleTextView;
    private TextView alarmDescTextView;
    private Switch alarmSwitch;
    private InTimeDataBase inTimeDataBase;
    private Spinner timeZoneSelector;

    private Alarm recievedAlarm;

    private ArrayList<Clock> listOfUserDefinedClocks=new ArrayList<>();

    TimePicker timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent(); // get intent which started the activity
        inTimeDataBase= MainActivity.database;
        setContentView(R.layout.activity_alarm_edit_settings); //"inflate", render the screen depending on defined layout
        alarmBackButton=findViewById(R.id.AlarmEditMenuBackButton);
        saveBtn=(Button) findViewById(R.id.saveAlarm);
        timePicker=(TimePicker)findViewById(R.id.AlarmTimePicker);
        timePicker.setIs24HourView(true); //make the time picker 24 hour
        alarmTitleTextView=(TextView)findViewById(R.id.alarmTitleInput);
        alarmDescTextView=(TextView)findViewById(R.id.alarmDescInput);
        alarmSwitch=(Switch)findViewById(R.id.switchEnabled);
        timeZoneSelector=(Spinner)findViewById(R.id.TimezoneSelector);
        //register for a click event for alarmBackButton
        alarmBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            BackButtonCLick(v);
            }
        });
        //register a click event for alarm save button
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SaveButtonClick(v);
            }
        });
        recievedAlarm=(Alarm) intent.getSerializableExtra("ALARM_TO_EDIT");
        listOfUserDefinedClocks= (ArrayList<Clock>) intent.getSerializableExtra("LIST_OF_CLOCKS");
        //incase it isnt in a test, load in the list of clocks
        if(intent.getBooleanExtra("THIS_IS_TEST",false)==false){
           try{
               if(listOfUserDefinedClocks.stream().count()>0){
                timeZoneSelector.setAdapter(new ArrayAdapter<Clock>(this, android.R.layout.simple_spinner_dropdown_item, listOfUserDefinedClocks));
               }
           }
           catch(Exception e){
               listOfUserDefinedClocks=new ArrayList<>();
           }
        }

        //NOTE: this is used for testing purposes, database usually isnt determined by intent
        if(intent.getBooleanExtra("THIS_IS_TEST",false)==true){
            inTimeDataBase= Room.inMemoryDatabaseBuilder(getApplicationContext(), InTimeDataBase.class).allowMainThreadQueries().build();
            listOfUserDefinedClocks.add(new Clock("Default","timezone"));
            timeZoneSelector.setAdapter(new ArrayAdapter<Clock>(this, android.R.layout.simple_spinner_dropdown_item, listOfUserDefinedClocks));

        }
        if(recievedAlarm!=null){
            populateWithData();
        }

    }
    /**this method is called when back button is pressed and it closes the activity. It takes view as parameter
     * @param v -view
     * @return void
     * */
    void BackButtonCLick(View v){
        finish(); //finish the current activity
    }
/**
 * this method gets the database in this activity, this method is used during testing
 * */
    public InTimeDataBase getRoomDatabase(){
        return inTimeDataBase;
    }
    /**
     * this method returns a void and takes no params, it "populates" the activity with the alarm data gotten from the database if edit button was pressed
     * */
    void populateWithData(){
       alarmTitleTextView.setText(recievedAlarm.alarmTitle);
       alarmDescTextView.setText(recievedAlarm.alarmDesc);
       alarmSwitch.setChecked(recievedAlarm.enabled);
       Calendar cal= Calendar.getInstance();
       cal.setTimeInMillis(recievedAlarm.timeToStartInTimezone);
       timePicker.setHour(cal.get(Calendar.HOUR_OF_DAY));
       timePicker.setMinute(cal.get(Calendar.MINUTE));
       listOfUserDefinedClocks.forEach(clock -> {
           if(clock.toString().contains(recievedAlarm.timeZoneID)){
               timeZoneSelector.setSelection(((ArrayAdapter)timeZoneSelector.getAdapter()).getPosition(clock));
               return;
           }
       });

    }

    /**
     * this method is called when save button is pressed. It adds the alarm into the DB and closes the current activity
     * @param v - view
     * @return void
     * */
    void SaveButtonClick(View v){

        //if default timezone selected (meaning local timezone)
        if(timeZoneSelector.getSelectedItemPosition()==0)
        {
            listOfUserDefinedClocks.get(timeZoneSelector.getSelectedItemPosition()).timeZone=TimeZone.getDefault().getID();
        }

        //get current date and time as selected by time picker
        LocalDate date= LocalDate.now();
        LocalTime time= LocalTime.of(timePicker.getHour(), timePicker.getMinute());

        //calculate local activation time when the selected time comes in the selected timezone and calculate time which would be in the selected timezone
        ZonedDateTime localStartTime= ZonedDateTime.of(date,time,ZoneId.of(listOfUserDefinedClocks.get(timeZoneSelector.getSelectedItemPosition()).timeZone));
        ZonedDateTime timeInTimeZone= localStartTime.withZoneSameLocal(TimeZone.getDefault().toZoneId());

        if(alarmTitleTextView.getText().toString().length()==0 || alarmDescTextView.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(),"Check your title and description fields before saving alarm!",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Alarm toInsert = new Alarm(recievedAlarm.alarmID,timeInTimeZone.toInstant().toEpochMilli(),localStartTime.toInstant().toEpochMilli() , alarmTitleTextView.getText().toString(), alarmDescTextView.getText().toString(), timeZoneSelector.getSelectedItem().toString(), alarmSwitch.isChecked());
            insertAlarmAsync(toInsert);
        }
        catch (Exception e){
            Alarm toInsert = new Alarm(timeInTimeZone.toInstant().toEpochMilli(), localStartTime.toInstant().toEpochMilli(), alarmTitleTextView.getText().toString(), alarmDescTextView.getText().toString(), timeZoneSelector.getSelectedItem().toString(), alarmSwitch.isChecked());
            insertAlarmAsync(toInsert);
        } finally {
            // if activity isnt in a test
            if(getIntent().getBooleanExtra("THIS_IS_TEST",false)==false)
            {
            finish();
            }
        }


    }

    /**
     * this method accesses the alarm DAO and inserts or updates the alarm in the database in background, depending on whether the recievedAlarm variable of this class is null or not
     * and it also activates the alarm depending on whether the user pressed the enable button.
     * @param alarm  - alarm to insert (class: Alarm)
     * */
    public void insertAlarmAsync(Alarm alarm){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        android.os.Handler handler = new Handler(Looper.getMainLooper());



        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work
               if(recievedAlarm==null) {
                   inTimeDataBase.alarmDAO().insert(alarm);
               }
               else{
                   inTimeDataBase.alarmDAO().updateAlarm(alarm);
               }
               if(alarm.enabled){
                   handler.post(new Runnable() {
                       @Override
                       public void run() {
                           AndroidOSAlarmManager.RegisterAlarm(alarm, MainActivity.alarmManager, getApplicationContext());

                       }
                   });
               }


            }
        });

    }


}