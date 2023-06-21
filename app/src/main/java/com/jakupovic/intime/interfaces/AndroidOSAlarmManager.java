package com.jakupovic.intime.interfaces;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.jakupovic.intime.alarmBroadcastReciever.AlarmBroadCastReceiver;
import com.jakupovic.intime.dataBase.Alarm;

import java.util.Calendar;

/**
 * this interface is responsible for registering/unregistering the alarm from the OS alarm system
 * */
public interface AndroidOSAlarmManager {

    /**
     * this method registers the alarm with the OS
     * @param alarmMgr - instance of alarm manager
     * @param alarm - alarm int the DB which will be activated
     * @param context - context of application
     * */
    static void RegisterAlarm(Alarm alarm, AlarmManager alarmMgr, Context context){
        Calendar cal=Calendar.getInstance();
        cal.setTimeInMillis(alarm.localStartTime);
        String toastNotifText="Alarm \""+alarm.alarmTitle+"\" is scheduled for: "+ cal.get(Calendar.HOUR_OF_DAY) + " : "+cal.get(Calendar.MINUTE)+"!";
        Intent intent=new Intent(context, AlarmBroadCastReceiver.class);
        intent.putExtra("ALARM_INSTANCE",alarm); // send the alarm instance which is registered
        Toast.makeText(context,toastNotifText,Toast.LENGTH_LONG).show();
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,alarm.alarmID,intent,PendingIntent.FLAG_IMMUTABLE);
        alarmMgr.setAlarmClock(new AlarmManager.AlarmClockInfo(alarm.localStartTime,alarmIntent),alarmIntent);
    }
    /**
     * this method unregisters the alarm from the OS
     * @param alarmMgr - instance of alarm manager
     * @param alarm - alarm int the DB which will be activated
     * @param context - context of application
     * */
    static void UnregisterAlarm(Alarm alarm,AlarmManager alarmMgr, Context context){
        if(alarmMgr!=null){
            Calendar cal=Calendar.getInstance();
            cal.setTimeInMillis(alarm.localStartTime);
            String toastNotifText="Alarm \""+alarm.alarmTitle+"\", at : "+ cal.get(Calendar.HOUR_OF_DAY) + " : "+cal.get(Calendar.MINUTE)+" is disabled!";

            Intent intent=new Intent(context, AlarmBroadCastReceiver.class);
            intent.putExtra("ALARM_INSTANCE",alarm);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context,alarm.alarmID,intent,PendingIntent.FLAG_IMMUTABLE);

            alarmMgr.cancel(alarmIntent);
            Toast.makeText(context,toastNotifText,Toast.LENGTH_LONG).show();

        }
    }
}
