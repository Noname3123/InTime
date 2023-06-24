package com.jakupovic.intime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.jakupovic.intime.alarmBroadcastReciever.AlarmBroadCastReceiver;
import com.jakupovic.intime.alarmBroadcastReciever.AlarmService;
import com.jakupovic.intime.alarmEditMenu.AlarmEditSettings;
import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.dataBase.Clock;
import com.jakupovic.intime.fragments.MainActivity;
import com.jakupovic.intime.interfaces.AndroidOSAlarmManager;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * this class contains all tests which test alarm activation
 * */
@RunWith(AndroidJUnit4.class)
public class AlarmActivationTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule =new ActivityScenarioRule<>(MainActivity.class);


    /**
     * register an alarm for activation that is set "before" the current time
     */

    @Test
    public void RegisterALocalAlarmBeforeCurrentTime(){
        //activity preparation
        Context context= InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent=new Intent(context, MainActivity.class);
        intent.putExtra("THIS_IS_TEST", true);


        //prepare data
        Calendar cal=Calendar.getInstance();
        //set alarm 2hrs earlier than current time
        cal.set(Calendar.HOUR_OF_DAY,cal.get(Calendar.HOUR_OF_DAY)-2);
        Alarm alarm=new Alarm(5,cal.getTimeInMillis(), cal.getTimeInMillis(), "Test alarm","Alarm description","Default",true);

        ActivityScenario<MainActivity> scenario=ActivityScenario.launch(intent);

        scenario.onActivity(activity -> {
            AlarmManager alarmManager=(AlarmManager) activity.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            AndroidOSAlarmManager.RegisterAlarm(alarm,alarmManager,activity.getApplicationContext());

            //is the alarm set (create an identical copy of the Pending intent set by AndroidOSAlarmManager.RegisterAlarm )
            boolean isAlarmIntentSet= ( PendingIntent.getBroadcast(activity.getApplicationContext(),alarm.alarmID, new Intent(activity.getApplicationContext(), AlarmBroadCastReceiver.class),PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_NO_CREATE)!=null);

            //if this assert passes, the alarm is already set becouse of the DO NOT CREATE flag

            assertTrue(isAlarmIntentSet);







        });

        //wait for 15 seconds before relaunching scenario (since the service may start by then - if the alarm is to activate)
        SystemClock.sleep(15000);
        //recreate the scenario
        scenario.recreate();
        //check the service activity
        scenario.onActivity(activity -> {
            AlarmManager alarmManager=(AlarmManager) activity.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            //since the alarm time is a bit later than the current device time, the OS will trigger the alarm
            //if this assert passes, it means that the alarm is triggered (since the pending intent launches the broadcast which starts the alarm service)
            assertFalse(isTheAlarmServiceRunning(activity.getApplicationContext(), AlarmService.class));

            //stop the alarm service
            activity.getApplicationContext().stopService(new Intent(activity.getApplicationContext(),AlarmService.class));
            AndroidOSAlarmManager.UnregisterAlarm(alarm,alarmManager,activity.getApplicationContext());
        });
        //close the scenario
        scenario.close();



    }


    /**
     * register an alarm for activation that is set "before" the current time
     */

    @Test
    public void RegisterAForeignAlarmAfterCurrentTime() throws InterruptedException {
        //activity preparation
        Context context= InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent=new Intent(context, MainActivity.class);
        intent.putExtra("THIS_IS_TEST", true);


        //prepare data
        Calendar cal=Calendar.getInstance();
        //set clock one minute later (so that it activates the same day)
        cal.set(Calendar.MINUTE,cal.get(Calendar.MINUTE)+1);

        Calendar calToActivate=Calendar.getInstance(TimeZone.getTimeZone("US/Eastern"));
        Alarm alarm=new Alarm(5,calToActivate.getTimeInMillis(), cal.getTimeInMillis(), "Test alarm","Alarm description","US/Eastern",true);

        ActivityScenario<MainActivity> scenario=ActivityScenario.launch(intent);

        scenario.onActivity(activity -> {
            AlarmManager alarmManager=(AlarmManager) activity.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            AndroidOSAlarmManager.RegisterAlarm(alarm,alarmManager,activity.getApplicationContext());

            //is the alarm set (create an identical copy of the Pending intent set by AndroidOSAlarmManager.RegisterAlarm )
            boolean isAlarmIntentSet= ( PendingIntent.getBroadcast(activity.getApplicationContext(),alarm.alarmID, new Intent(activity.getApplicationContext(), AlarmBroadCastReceiver.class),PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_NO_CREATE)!=null);

            //if this assert passes, the alarm is already set becouse of the DO NOT CREATE flag

            assertTrue(isAlarmIntentSet);






        });
        //wait for 1 minute and 15 seconds before relaunching scenario (since the alarm will activate and the service will start by then)
        SystemClock.sleep(75000);
        //recreate the scenario
        scenario.recreate();
        //check the service activity
        scenario.onActivity(activity -> {
            AlarmManager alarmManager=(AlarmManager) activity.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            //since the alarm time is a bit later than the current device time, the OS will trigger the alarm
            //if this assert passes, it means that the alarm is triggered (since the pending intent launches the broadcast which starts the alarm service)
            assertTrue(isTheAlarmServiceRunning(activity.getApplicationContext(), AlarmService.class));

            //stop the alarm service
            activity.getApplicationContext().stopService(new Intent(activity.getApplicationContext(),AlarmService.class));
            AndroidOSAlarmManager.UnregisterAlarm(alarm,alarmManager,activity.getApplicationContext());
        });
        //close the scenario
        scenario.close();

    }
/**
 * this method returns true if the target service is running
 * @param context -context of app
 * @param serviceClass - class representing service
 * @return  boolean
 * */
    boolean isTheAlarmServiceRunning(Context context,Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
