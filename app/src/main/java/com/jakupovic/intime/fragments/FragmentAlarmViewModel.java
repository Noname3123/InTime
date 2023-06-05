package com.jakupovic.intime.fragments;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.WorkRequest;
import androidx.work.Worker;

import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.dataBase.dbDAO.AlarmDAO;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * this class contains all the data necessary for the alarm fragment to function (such as DAO for accessing alarms in DB)
 * */
class alarmViewModelData{
    public AlarmDAO alarmDAO=MainActivity.database.alarmDAO();
    public List<Alarm> allAlarms;
    /**
     * id of the last added alarm
     * */
    public int alarmID;

}

public class FragmentAlarmViewModel extends ViewModel {

    private  final MutableLiveData<alarmViewModelData> alarmViewModelData= new MutableLiveData(new alarmViewModelData());
    /**
     * a getter for alarmViewModelData class
     * */
    public LiveData<alarmViewModelData> getAlarmViewModelData() {return alarmViewModelData;}

    /**
     * this method accesses the alarm DAO and inserts the alarm in the database in background
     * @param alarm  - alarm to insert (class: alarm)
     * */
    public void insertAlarmAsync(Alarm alarm){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work
                alarmViewModelData.getValue().alarmDAO.insert(alarm);


            }
        });

    }

    /**
     * this method loads all alarms in the background thread
     * @param funcToExecOnInstance - Function<Alarm,Void> which will be called for every instance gotten by the query
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
     * this method deletes an alarm from database
     * @param idOfAlarmToDelete - int representing the key of the database entry
     * @return void
     * */
    public void deleteAlarmInstance(int idOfAlarmToDelete){
        //TODO: insert method for "unregistering" a currently active alarm

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work
                Alarm alarmToDelete= alarmViewModelData.getValue().alarmDAO.getAlarmByID(idOfAlarmToDelete);
                alarmViewModelData.getValue().alarmDAO.delete(alarmToDelete);

               // handler.post(new Runnable() {
                 //   @Override
                   // public void run() {

                   // }
                //});
            }
        });

    }



}



