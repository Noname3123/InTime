package com.jakupovic.intime.fragments;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.dataBase.Clock;
import com.jakupovic.intime.dataBase.dbDAO.AlarmDAO;
import com.jakupovic.intime.interfaces.AndroidOSAlarmManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;


/**
 * this class contains all the data necessary for the alarm fragment to function (such as DAO for accessing alarms in DB)
 * */
class alarmViewModelData{
    public AlarmDAO alarmDAO=MainActivity.database.alarmDAO();
    //public List<Alarm> allAlarms;
    /**
     * instance of the alarm gotten by ID query
     * */
    public Alarm alarmInstance;
/**
 * list of all clocks the user has created
 * */
    public List<Clock> allClockInstances=new ArrayList<Clock>();

}

public class FragmentAlarmViewModel extends ViewModel implements Serializable, AndroidOSAlarmManager {

    private  final MutableLiveData<alarmViewModelData> alarmViewModelData= new MutableLiveData(new alarmViewModelData());
    /**
     * a getter for alarmViewModelData class
     * */
    public LiveData<alarmViewModelData> getAlarmViewModelData() {return alarmViewModelData;}



    /**
     * this method loads all alarms in the background thread
     * @param funcToExecOnInstance - Consumer<Alarm> which will be called for every instance gotten by the query
     * */
    public void getAllAlarmsAsync(Consumer<Alarm> funcToExecOnInstance){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work
                List<Alarm> allAlarms=alarmViewModelData.getValue().alarmDAO.getAll();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work
                        allAlarms.forEach(alarm -> {
                            funcToExecOnInstance.accept(alarm);
                        });
                    }
                });
            }
        });


    }
    /**
     * this method Updates the alarm activity and registers the alarm with the OS alarm system
     * @param alarm - alarm instance from database whose activity status will be updated
     * @param alarMgr  - alarm manager instance
     * @param context - context
     * */
    public void updateAlarmActivityAsync(Alarm alarm, AlarmManager alarMgr, Context context){

        if(alarm.enabled==true){
            AndroidOSAlarmManager.RegisterAlarm(alarm,alarMgr,context);
        }
        else if(alarm.enabled==false){
            AndroidOSAlarmManager.UnregisterAlarm(alarm,alarMgr,context);
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                alarmViewModelData.getValue().alarmDAO.updateAlarm(alarm);
            }
        });
    }

    /**
     * this method deletes an alarm from database and unregisters it from the OS alarm manager
     * @param idOfAlarmToDelete - int representing the key of the alarm in database entry
     * @param context - context of the application
     * @return void
     * */
    public void deleteAlarmInstance(int idOfAlarmToDelete, Context context){


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());


        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work
                Alarm alarmToDelete= alarmViewModelData.getValue().alarmDAO.getAlarmByID(idOfAlarmToDelete);
                alarmViewModelData.getValue().alarmDAO.delete(alarmToDelete);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AndroidOSAlarmManager.UnregisterAlarm(alarmToDelete,MainActivity.alarmManager,context);
                    }
                });

            }
        });

    }

    /**
     * this method gets an alarm from database by its ID
     * @param idOfAlarm - int representing the key of the alarm in database entry
     * @return Future - object which represents the thread execution state
     * */
    public Future getAlarmByID(int idOfAlarm){

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        return executor.submit(new Runnable() {
            @Override
            public void run() {

                //Background work

                    alarmViewModelData.getValue().alarmInstance=alarmViewModelData.getValue().alarmDAO.getAlarmByID(idOfAlarm);



            }
        });

    }
    /**
     * this method gets all all user created clocks from the DB so that they can be loaded into the alarm edit interface
     * @return Future - object which represents the thread execution state
     * */
    public Future getAllUserCreatedClocks(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        return executor.submit(new Runnable() {
            @Override
            public void run() {

                //Background work
                alarmViewModelData.getValue().allClockInstances.clear();
                alarmViewModelData.getValue().allClockInstances.add(new Clock("Default", "timezone")); //add entry for default timezone
                alarmViewModelData.getValue().allClockInstances.addAll(MainActivity.database.clockDAO().getAll());



            }
        });
    }



}



