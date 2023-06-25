package com.jakupovic.intime;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.jakupovic.intime.ClockEditMenu.ClockEditActivity;
import com.jakupovic.intime.alarmEditMenu.AlarmEditSettings;
import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.dataBase.Clock;
import com.jakupovic.intime.dataBase.InTimeDataBase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * this class contains all tests which integrate the database with the application methods (specifically the clock edit activity)
 * */
@RunWith(AndroidJUnit4.class)
public class ClockEditActivityTests {




    @Rule
    public ActivityScenarioRule<ClockEditActivity> mActivityRule =new ActivityScenarioRule<>(ClockEditActivity.class);







/**
 * this method test the insert clock into DB method from the ClockEditActivity
 * */
    @Test
    public void TestInsertClockIntoDBFUnction(){
        Context context= InstrumentationRegistry.getInstrumentation().getTargetContext();
        Clock toInsert=new Clock("Some Central USA location","US/Central");
        Intent intent=new Intent(context, ClockEditActivity.class);
        intent.putExtra("THIS_IS_TEST", true);
        ActivityScenario<ClockEditActivity> scenario = ActivityScenario.launch(intent); //launch activity with test intent, notifiying it that it is in a test environment
       scenario.onActivity(activity -> {
           InTimeDataBase dataBase=activity.getRoomDatabase();

           activity.insertClockAsync(toInsert);

           List<Clock> allClocks = dataBase.clockDAO().getAll();
           allClocks.forEach(clock -> {
               assertEquals(toInsert.location,clock.location);
               assertEquals(toInsert.timeZone,clock.timeZone);
           });
       });




    }
    /**
     * this method TestsEditClockActivity after the alarm from DB is passed (when user presses the edit button), since the data should be loaded
     * */
    @Test
    public void TestEditAlarmFromDBFunction(){
        Context context= InstrumentationRegistry.getInstrumentation().getTargetContext();
        Clock toEdit=new Clock("Some Central USA location","US/Central");
        Intent intent=new Intent(context, ClockEditActivity.class);
        intent.putExtra("THIS_IS_TEST", true);
        intent.putExtra("CLOCK_TO_EDIT", toEdit);
        ActivityScenario<ClockEditActivity> scenario = ActivityScenario.launch(intent); //launch activity with test intent, notifiying it that it is in a test environment
        scenario.onActivity(activity -> {
            AutoCompleteTextView timeZoneSelector=(AutoCompleteTextView) activity.findViewById(R.id.AutoCompleteClockTimezoneSelector);
            TextView clockDescTextView=(TextView) activity.findViewById(R.id.clockDescriptionInput);
            assertEquals(toEdit.timeZone,timeZoneSelector.getText().toString());
            assertEquals(toEdit.location,clockDescTextView.getText().toString());


        });

    }


}
