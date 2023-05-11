package com.jakupovic.intime.fragments;

import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

//this class implements data related to the stopwatch
class StopwatchState{
 public int seconds=0;
 public boolean running=false;
 public boolean wasRunning=false;

 public String memorisedLaps="";
 public int lapCount=0;

}

public class FragmentStopwatchViewModel extends ViewModel {
    private  final MutableLiveData<StopwatchState> stopwatchState= new MutableLiveData(new StopwatchState());
    /**method returns data from the stopwatch viewmodel*/
    public LiveData<StopwatchState> getStopwatchState (){
        return stopwatchState;
    }



}