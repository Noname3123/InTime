package com.jakupovic.intime.alarmEditMenu;

import androidx.appcompat.app.AppCompatActivity;

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
import com.jakupovic.intime.dataBase.InTimeDataBase;
import com.jakupovic.intime.fragments.MainActivity;

import java.util.Calendar;
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
            //TODO: add logic here for registering/unregistering alarms with the Android OS alarm system
            @Override
            public void onClick(View v) {
                SaveButtonClick(v);
            }
        });
        recievedAlarm=(Alarm) intent.getSerializableExtra("ALARM_TO_EDIT");
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
       timeZoneSelector.setSelection(((ArrayAdapter)timeZoneSelector.getAdapter()).getPosition(recievedAlarm.timeZoneID));
    }

    //TODO: add save alarm logic for save button, use the intent for sending along class instances of data to be edited in case the alarm settings are changed
    /**
     * this method is called when save button is pressed. It adds the alarm into the DB and closes the current activity
     * @param v - view
     * @return void
     * */
    void SaveButtonClick(View v){
        Calendar calendar=Calendar.getInstance(); //TODO: instantiate this calendar in the selected timezone, and then change the calendar into local timezone later
        calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
        calendar.set(Calendar.MINUTE,timePicker.getMinute());
        if(alarmTitleTextView.getText().toString().length()==0 || alarmDescTextView.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(),"Check your title and description fields before saving alarm!",Toast.LENGTH_SHORT).show();
            return;
        }
        Alarm toInsert= new Alarm(calendar.getTimeInMillis(),calendar.getTimeInMillis(),alarmTitleTextView.getText().toString(),alarmDescTextView.getText().toString(),timeZoneSelector.getSelectedItem().toString(),alarmSwitch.isChecked()); //TODO: for the second calendar do recalculations into local timezone, NOTE: timeZoneSelector will select object of type clock which contain timezone IDs - which will be added to the alarm info
        insertAlarmAsync(toInsert);
        finish();
    }

    /**
     * this method accesses the alarm DAO and inserts the alarm in the database in background
     * @param alarm  - alarm to insert (class: alarm)
     * */
    public void insertAlarmAsync(Alarm alarm){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work
                inTimeDataBase.alarmDAO().insert(alarm);


            }
        });

    }


}