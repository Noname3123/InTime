package com.jakupovic.intime.ClockEditMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jakupovic.intime.R;
import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.dataBase.Clock;
import com.jakupovic.intime.dataBase.InTimeDataBase;
import com.jakupovic.intime.fragments.MainActivity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlin.collections.UArraySortingKt;

public class ClockEditActivity extends AppCompatActivity {

    private ImageButton backButton;
    private Button saveBtn;
    private TextView clockDescTextView;
    private InTimeDataBase inTimeDataBase;
    private AutoCompleteTextView timeZoneSelector;

    private String[] arrayOfAllTimeZoneIDs;
    private Clock recievedClock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        inTimeDataBase= MainActivity.database;
        setContentView(R.layout.activity_clock_edit);
        backButton=(ImageButton) findViewById(R.id.BackButtonClockEditActivity);
        saveBtn=(Button) findViewById(R.id.saveClock);
        timeZoneSelector=(AutoCompleteTextView) findViewById(R.id.AutoCompleteClockTimezoneSelector);
        arrayOfAllTimeZoneIDs=TimeZone.getAvailableIDs();
        timeZoneSelector.setAdapter(new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, arrayOfAllTimeZoneIDs)); //set the dropdown adapter of all timezones
        clockDescTextView=(TextView) findViewById(R.id.clockDescriptionInput);
        //register buttons
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackButtonClick(v);
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveButtonClick(v);
            }
        });

        recievedClock=(Clock) intent.getSerializableExtra("CLOCK_TO_EDIT");
        if(recievedClock!=null){
            populateWithData();
        }
        //NOTE: this is used when tasting integration and user flow with activities
        if(intent.getBooleanExtra("THIS_IS_TEST",false)==true){
            inTimeDataBase= Room.inMemoryDatabaseBuilder(getApplicationContext(),InTimeDataBase.class).allowMainThreadQueries().build();
        }


    }
    /**this method is called when back button is pressed and it closes the activity. It takes view as parameter
     * @param v -view
     * @return void
     * */
    void BackButtonClick(View v){
        finish(); //finish the current activity
    }

    /**
     * this method gets the database in this activity, this method is used during testing
     * */
    public InTimeDataBase getRoomDatabase(){
        return inTimeDataBase;
    }

    /**
     * this method returns a void and takes no params, it "populates" the activity with the clock data gotten from the database if edit button was pressed
     * */
    void populateWithData(){
        clockDescTextView.setText(recievedClock.location);
        timeZoneSelector.setText(recievedClock.timeZone);
    }



    /**
     * this method is called when save button is pressed. It adds the alarm into the DB and closes the current activity
     * @param v - view
     * @return void
     * */
    void SaveButtonClick(View v){
        if(clockDescTextView.getText().toString().length()==0 || Arrays.asList(arrayOfAllTimeZoneIDs).contains(timeZoneSelector.getText().toString())==false ){
            Toast.makeText(getApplicationContext(),"Check your description and timezone fields before saving clock!",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Clock toInsert = new Clock(recievedClock.id,clockDescTextView.getText().toString(),timeZoneSelector.getText().toString());
            insertClockAsync(toInsert);
        }
        catch (Exception e){
            Clock toInsert = new Clock(clockDescTextView.getText().toString(),timeZoneSelector.getText().toString());
            insertClockAsync(toInsert);
        } finally {
            // if activity isnt in a test
            if(getIntent().getBooleanExtra("THIS_IS_TEST",false)==false)
            {
                finish();
            }
        }


    }

    /**
     * this method accesses the clock DAO and inserts or updates the clock in the database in background, depending on whether the recievedClock variable of this class is null or not
     * @param clock  - alarm to insert (class: Clock)
     * */
    public void insertClockAsync(Clock clock){
        ExecutorService executor = Executors.newSingleThreadExecutor();


        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work
                if(recievedClock==null) {
                    inTimeDataBase.clockDAO().insert(clock);
                }
                else{
                    inTimeDataBase.clockDAO().updateClock(clock);
                }


            }
        });

    }


}