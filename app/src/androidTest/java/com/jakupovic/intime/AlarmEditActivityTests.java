package com.jakupovic.intime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;


import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.jakupovic.intime.alarmEditMenu.AlarmEditSettings;
import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.dataBase.Clock;
import com.jakupovic.intime.dataBase.InTimeDataBase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * this class contains all tests which integrate the database with the application methods
 * */
@RunWith(AndroidJUnit4.class)
public class AlarmEditActivityTests {




    @Rule
    public ActivityScenarioRule<AlarmEditSettings> mActivityRule =new ActivityScenarioRule<>(AlarmEditSettings.class);







/**
 * this method test the insert alarm into DB method from the AlarmEditActivity
 * */
    @Test
    public void TestInsertIntoDBFUnction(){
        Context context= InstrumentationRegistry.getInstrumentation().getTargetContext();
        Alarm toInsert=new Alarm(Long.valueOf(2),Long.valueOf(1),"test alarm","This is a test alarm to be introduced in DB","someTimezone",true);
        Intent intent=new Intent(context, AlarmEditSettings.class);
        intent.putExtra("THIS_IS_TEST", true);
        List<Clock> clocks = new ArrayList<Clock>();
        clocks.add(new Clock("Default", "timezone"));
        clocks.add(new Clock("Something","else"));
        intent.putExtra("LIST_OF_CLOCKS", new ArrayList<Clock>(clocks));
        ActivityScenario<AlarmEditSettings> scenario = ActivityScenario.launch(intent); //launch activity with test intent, notifiying it that it is in a test environment
       scenario.onActivity(activity -> {

           activity.insertAlarmAsync(toInsert);

           List<Alarm> alarms=activity.getRoomDatabase().alarmDAO().getAll();
           alarms.forEach(alarm -> {
               assertEquals(toInsert.alarmTitle,alarm.alarmTitle);
               assertEquals(toInsert.alarmDesc,alarm.alarmDesc);
               assertEquals(toInsert.localStartTime,alarm.localStartTime);
               assertEquals(toInsert.timeToStartInTimezone,alarm.timeToStartInTimezone);
               assertEquals(toInsert.timeZoneID,alarm.timeZoneID);
               assertEquals(toInsert.enabled,alarm.enabled);
           });
       });




    }
    /**
     * this method TestsEditAlarmActivity after the alarm from DB is passed
     * */
    @Test
    public void TestEditAlarmFromDBFunction(){
        Context context= InstrumentationRegistry.getInstrumentation().getTargetContext();
        Alarm toEdit=new Alarm(Long.valueOf("1686074093000"),Long.valueOf("1686074093000"),"test alarm","This is a test alarm to be introduced in DB","Default (timezone)",true);
        Intent intent=new Intent(context, AlarmEditSettings.class);
        List<Clock> clocks = new ArrayList<>();
        clocks.add(new Clock("Default", "timezone"));
        clocks.add(new Clock("Something","else"));
        intent.putExtra("LIST_OF_CLOCKS", new ArrayList<Clock>(clocks));
        intent.putExtra("THIS_IS_TEST", true);
        intent.putExtra("ALARM_TO_EDIT", toEdit);
        ActivityScenario<AlarmEditSettings> scenario = ActivityScenario.launch(intent); //launch activity with test intent, notifiying it that it is in a test environment
        scenario.onActivity(activity -> {
           assertEquals(((TextView)activity.findViewById(R.id.alarmTitleInput)).getText().toString(),toEdit.alarmTitle);
           assertEquals(((TextView)activity.findViewById(R.id.alarmDescInput)).getText().toString(),toEdit.alarmDesc);
           assertEquals(((Switch)activity.findViewById(R.id.switchEnabled)).isChecked(),toEdit.enabled);
           assertEquals(((Spinner)activity.findViewById(R.id.TimezoneSelector)).getSelectedItem().toString(),toEdit.timeZoneID);
           TimePicker timePicker=(TimePicker)activity.findViewById(R.id.AlarmTimePicker);
            Calendar calendar=Calendar.getInstance();
            calendar.setTimeInMillis(toEdit.timeToStartInTimezone); //set calendar to the time defined in long value, saved in activity scenario

            assertEquals(timePicker.getHour(),calendar.get(Calendar.HOUR_OF_DAY));
            assertEquals(timePicker.getMinute(),calendar.get(Calendar.MINUTE));




        });

    }


}
