package com.jakupovic.intime.fragments;

import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.os.Handler;

/**this class implements data related to the stopwatch*/
class StopwatchState{
 public float seconds=0;
 public boolean running=false;
 public boolean wasRunning=false;

 public String memorisedLaps="";
 public int lapCount=0;
/**handler which executes threads responsible for counting
 * */
 public Handler stopwatchCounter=new Handler();


}

public class FragmentStopwatchViewModel extends ViewModel {
    private  final MutableLiveData<StopwatchState> stopwatchState= new MutableLiveData(new StopwatchState());
    /**method returns data from the stopwatch viewmodel
     * */
    public LiveData<StopwatchState> getStopwatchState (){
        return stopwatchState;
    }



}