package com.jakupovic.intime.fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClockViewModel extends ViewModel {
    private  final MutableLiveData<ClockLocalData> clockData= new MutableLiveData(new ClockLocalData());
    /**method returns data from the stopwatch viewmodel
     * */
    public LiveData<ClockLocalData> getClockData (){
        return clockData;
    }
}