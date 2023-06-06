package com.jakupovic.intime;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.jakupovic.intime.alarmEditMenu.AlarmEditSettings;
import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.dataBase.InTimeDataBase;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;

/**
 * this class contains all tests which define an user flow
 * */
@RunWith(AndroidJUnit4.class)
public class UserFlowTests {

    @Rule
    public ActivityScenarioRule<AlarmEditSettings> mActivityRule =new ActivityScenarioRule<>(AlarmEditSettings.class);

    /**
     * this method simulates an user flow in which an user creates an alarm
     * */
    @Test
    public void UserCreatesANewAlarm(){
        //variables to type
        String alarmTitle ="This is an example alarm";
        String alarmDescr="Some Description";
        int hours=15;
        int minutes=25;
        String selectedTimezoneText="Tokyo Time (example)";
        boolean switchClicked=true;

        //init of scenario
        Context context= InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent=new Intent(context, AlarmEditSettings.class);
        intent.putExtra("THIS_IS_TEST", true);
        ActivityScenario<AlarmEditSettings> scenario = ActivityScenario.launch(intent); //launch activity with test intent, notifiying it that it is in a test environment

        onView(withId(R.id.alarmTitleInput)).perform(typeText(alarmTitle));
        onView(withId(R.id.alarmDescInput)).perform(typeText(alarmDescr));
        onView(withId(R.id.AlarmTimePicker)).perform(scrollTo()).perform(new ViewAction() {
            @Override
            public String getDescription() {
                return "Set passed time into timepicker";
            }

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(TimePicker.class);
            }

            @Override
            public void perform(UiController uiController, View view) {
                TimePicker tp=(TimePicker) view;
                tp.setHour(hours);
                tp.setMinute(minutes);
            }
        });
        onView(withId(R.id.TimezoneSelector)).perform(scrollTo()).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(selectedTimezoneText))).perform(click());
        onView(withId(R.id.TimezoneSelector)).check(matches(withSpinnerText(containsString(selectedTimezoneText))));
        onView(withId(R.id.switchEnabled)).perform(scrollTo()).perform(click());
        onView(withId(R.id.saveAlarm)).perform(click());
        //performances
        scenario.onActivity(activity -> {
            InTimeDataBase db=activity.getRoomDatabase();
            List<Alarm> alarms= db.alarmDAO().getAll();
            alarms.forEach(alarm -> {
                assertEquals(alarm.alarmTitle,alarmTitle);
                assertEquals(alarm.alarmDesc,alarmDescr);

                Calendar cal=Calendar.getInstance();
                cal.setTimeInMillis(alarm.localStartTime);

                assertEquals(cal.get(Calendar.HOUR_OF_DAY),hours);
                assertEquals(cal.get(Calendar.MINUTE),minutes);
                assertEquals(selectedTimezoneText,alarm.timeZoneID);
                assertEquals(switchClicked,alarm.enabled);
            });

        });



    }


}
