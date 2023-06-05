package com.jakupovic.intime.fragments;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
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


/**
 * this class contains all the data necessary for the alarm fragment to function (such as DAO for accessing alarms in DB)
 * */
class alarmViewModelData{
    public AlarmDAO alarmDAO=MainActivity.database.alarmDAO();
    public List<Alarm> allAlarms;

}

public class FragmentAlarmViewModel extends ViewModel {

    private  final MutableLiveData<alarmViewModelData> alarmViewModelData= new MutableLiveData(new alarmViewModelData());
    /**
     * a getter for alarmViewModelData class
     * */
    public LiveData<alarmViewModelData> getAlarmViewModelData() {return alarmViewModelData;}





}



