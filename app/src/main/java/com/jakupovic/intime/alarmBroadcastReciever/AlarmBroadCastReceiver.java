package com.jakupovic.intime.alarmBroadcastReciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;

import com.jakupovic.intime.dataBase.Alarm;

/**
 * the receiver of the Alarm
 * */
public class AlarmBroadCastReceiver extends BroadcastReceiver {

    //used to activate alarm with screen off.
    private static PowerManager powerManager;
    private static PowerManager.WakeLock wakeLock;
    /**
     * setter for wakelocks
     * @param wakelockTag - name of wakelock
     */

    public static void SetWakelock(String wakelockTag){
        wakeLock= powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,wakelockTag);
        wakeLock.acquire(10*60*1000L /*10 minutes*/); //the acquired wakelock lasts for 10min
    }

    /**
     * remover for wakelocks

     */

    public static void RemoveWakelock( ){
        //release the wakelock
        wakeLock.release();
        wakeLock=null;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
       powerManager=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
       SetWakelock("InTime::AlarmWakelock");
        startAlarmService(context,intent);
    }
    /**
     * this method starts the alarm service (and te alarm gets triggered)
     * @param context  - context of app
     * @param intent - intent which was sent by the alarm manager
     * */
    public void startAlarmService(Context context, Intent intent){
        Intent serviceIntent=new Intent(context,AlarmService.class);
        serviceIntent.putExtra("ALARM_INSTANCE", (Alarm) intent.getSerializableExtra("ALARM_INSTANCE"));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(serviceIntent); // if new android (8.0+) with different service management
        }
        else {
            context.startService(serviceIntent);
        }
    }
}

