package com.jakupovic.intime.alarmBroadcastReciever;

import static com.jakupovic.intime.preActivityAppInit.PreActivityApp.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.jakupovic.intime.R;
import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.fragments.AlarmActiveActivity;
import com.jakupovic.intime.preActivityAppInit.PreActivityApp;

import java.io.IOException;

/**
 * this class represents an alarm service which will start the alarm notification and execute the alarm alert
 * */
public class AlarmService extends Service {
    //alarm ref
    private static Alarm alarmInstance;
    //alarm alert vars
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;



    @Override
    public void onCreate(){
        super.onCreate();

        //get ringtone and vibrator
        Uri currentlySelectedAlarmRingtone = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_ALARM);
        mediaPlayer=new MediaPlayer();
        try {
            //load ringtone
            mediaPlayer.setDataSource(this,currentlySelectedAlarmRingtone);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //use ALARM volume
        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()); //the media player should use alarm volume
        mediaPlayer.setLooping(true);
        try {
            //prepare the defined player
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        vibrator=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }
/**
 * override of the default service on start command method
 * */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        alarmInstance=(Alarm) intent.getSerializableExtra("ALARM_INSTANCE");
        //set the currently triggered alarm


        Intent notificationIntent = new Intent(this, AlarmActiveActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //set flag for this intent
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_IMMUTABLE);
        //notification which will be displayed
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(alarmInstance.alarmTitle)
                .setContentText(alarmInstance.alarmDesc)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)  //by clicking on the content, this pending intent is executed ==> open the alarm active class
                .build();
        startForeground(1,notification); //"throw" the notification to foreground

        mediaPlayer.start();
        long vibrPattern[]={1000,2000,1000};
        vibrator.vibrate(vibrPattern,0);
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //once the service stops, stop vibrating
        mediaPlayer.stop();
        vibrator.cancel();
        AlarmBroadCastReceiver.RemoveWakelock();
    }
/**
 * this method returns the current alarm from DB which activated the alarm
 * */
    public static Alarm getAlarmInstance(){
        return alarmInstance;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
