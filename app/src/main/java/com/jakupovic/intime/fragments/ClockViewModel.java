package com.jakupovic.intime.fragments;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.dataBase.Clock;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class ClockViewModel extends ViewModel implements Serializable {
    private  final MutableLiveData<ClockLocalData> clockData= new MutableLiveData(new ClockLocalData());
    /**method returns data from the stopwatch viewmodel
     * */
    public LiveData<ClockLocalData> getClockData (){
        return clockData;
    }


    /**
     * this method loads all clocks in the background thread
     * @param funcToExecOnInstance - Consumer<Clock> which will be called for every instance gotten by the query
     * */
    public void getAllClocksAsync(Consumer<Clock> funcToExecOnInstance){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work
                List<Clock> allClocks=clockData.getValue().clockDAO.getAll();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work
                        allClocks.forEach(clock -> {
                            funcToExecOnInstance.accept(clock);
                        });
                    }
                });
            }
        });


    }
    /**
     * this method deletes clock instance from DB
     * @param idOfClockToDelete - int representing the key of the clock in database entry
     * @return void
     * */
    public void deleteClockInstance(int idOfClockToDelete){
        //TODO: insert method for allowing or blocking clock deletion depending on alarms

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {

                //Background work
                Clock clockToDelete= clockData.getValue().clockDAO.getClockByID(idOfClockToDelete);
                clockData.getValue().clockDAO.delete(clockToDelete);

            }
        });

    }

    /**
     * this method gets a clock from database by its ID
     * @param idOfClock - int representing the key of the alarm in database entry
     * @return Future - object which represents the thread execution state
     * */
    public Future getClockByID(int idOfClock){


        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        return executor.submit(new Runnable() {
            @Override
            public void run() {

                //Background work

                clockData.getValue().clockInstance=clockData.getValue().clockDAO.getClockByID(idOfClock);



            }
        });

    }
}