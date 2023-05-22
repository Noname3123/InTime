package com.jakupovic.intime.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakupovic.intime.R;

import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ClockFragment extends Fragment {

    private ClockViewModel mViewModel;
    private TextView localClock;
    private int clockUpdateInterval=1000; //clock update interval in ms

    public static ClockFragment newInstance() {
        return new ClockFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragment= (View) inflater.inflate(R.layout.fragment_clock, container, false); //inflate fragment
        localClock=fragment.findViewById(R.id.ClockLocalTime);
        runClock();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ClockViewModel.class);
    }

    /**this method takes the date object stored in clock view model and formats its time entry
     * @params none
     * @return String
     * */
    String calculateClockFormat(){

        return DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.getDefault()).format(mViewModel.getClockData().getValue().time);
    }

    /**
     * This method is responsible for checking time when the clock activity runs, it is called constantly, since the activity created in onCreate and it will call itself every clock update interval to update the clock
     * @params
     * @return void
     * */
    private void runClock(){


        //Handler
        final Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {


                mViewModel.getClockData().getValue().time = new GregorianCalendar().getTime();
                localClock.setText(calculateClockFormat());

                handler.postDelayed(this, clockUpdateInterval);
            }

        });
    }

}