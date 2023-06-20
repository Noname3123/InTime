package com.jakupovic.intime.alarmBroadcastReciever;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.jakupovic.intime.dataBase.Alarm;

import java.security.Provider;

/**
 * the receiver of the Alarm
 * */
public class AlarmBroadCastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
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

