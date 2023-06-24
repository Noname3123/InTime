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
import android.widget.Button;
import android.widget.TextView;

import com.jakupovic.intime.R;
import com.jakupovic.intime.interfaces.HandlerManager;

import java.util.Locale;

public class FragmentStopwatch extends Fragment implements HandlerManager {

    private FragmentStopwatchViewModel mViewModel;
    //get text view
    private TextView stopwatchText;
    private TextView lapDataTextView;
    private Button buttonStart;
    private Button buttonStop;
    private Button buttonLap;

    private final int stopwatchHandlerDelay=100;

    /**
     * a getter function which returns the viewModel of this fragment. It is used primarily for testing
     * */
    public FragmentStopwatchViewModel getmViewModel(){
        return mViewModel;
    }
    public static FragmentStopwatch newInstance() {
        return new FragmentStopwatch();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragment= (View) inflater.inflate(R.layout.fragment_fragment_stopwatch, container, false); //inflate fragment
        stopwatchText= (TextView) fragment.findViewById(R.id.StopwatchTime);
        lapDataTextView = (TextView) fragment.findViewById(R.id.lapsData);
        buttonStart =(Button) fragment.findViewById(R.id.buttonStart);
        buttonStop =(Button) fragment.findViewById(R.id.buttonStop);
        buttonLap =(Button) fragment.findViewById(R.id.buttonLap);

        //these methods now add listeners to the buttons
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickStart(v);
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickStop(v);
            }
        });
        buttonLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLap(v);
            }
        });

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FragmentStopwatchViewModel.class);

        decideLapButtonVisibility();


    }

    @Override
    //method is called when new activity is called and it stops handlers
    public void onStop(){
        HandlerManager.ResetHandler(mViewModel.getStopwatchState().getValue().stopwatchCounter); //reset stopwatch counter if already the thread exists after another activity opened
        super.onStop();
    }
    @Override
    ///method pauses the stopwatch if the activity is not in focus (such as app is minimized)
    public void onPause(){
        super.onPause();
        mViewModel.getStopwatchState().getValue().wasRunning=mViewModel.getStopwatchState().getValue().running;
    }
    @Override
    ///stopwatch automatically resumes after user focuses into activity
    public void onResume(){
        super.onResume();
        HandlerManager.RegisterHandlerFunction(mViewModel.getStopwatchState().getValue().stopwatchCounter, new Runnable() {
            @Override
            public void run() {
                stopwatchText.setText(calculateTimeFormat());
                if (mViewModel.getStopwatchState().getValue().running){
                    mViewModel.getStopwatchState().getValue().seconds +=stopwatchHandlerDelay/1000.0f;
                }
                mViewModel.getStopwatchState().getValue().stopwatchCounter.postDelayed(this, stopwatchHandlerDelay);
            }

        });
        if(mViewModel.getStopwatchState().getValue().wasRunning){
            mViewModel.getStopwatchState().getValue().running=true;
        }
    }

    /**this method takes the View as an argument and is called when user clicks on start button in the stopwatch
     * @param view
     * @return void
     * */
    public void onClickStart(View view){
        mViewModel.getStopwatchState().getValue().running=true;
        decideLapButtonVisibility();
        buttonStop.setText(R.string.ButtonStop);

    }
    /**this method takes the View as an argument and is called when user clicks on stop button in the stopwatch
     * @param view
     * @return void
     * */
    public void onClickStop(View view){
        if(mViewModel.getStopwatchState().getValue().running==true)
        {
        mViewModel.getStopwatchState().getValue().running=false;
            decideLapButtonVisibility();
        if(mViewModel.getStopwatchState().getValue().seconds>0) {
         buttonStop.setText(R.string.ButtonReset);
        }
        }
         else if (mViewModel.getStopwatchState().getValue().running==false && (mViewModel.getStopwatchState().getValue().seconds>0 ||mViewModel.getStopwatchState().getValue().memorisedLaps.length()>0 )) {
            onClickReset(view);
            buttonStop.setText(R.string.ButtonStop);
            decideLapButtonVisibility();
        }

    }
/**
 * this method takes View as argument and is called when user click on lap button in the stopwatch and adds the lap data to the lapdata text view
 * @param view
 * @return void
 * */
    public void onClickLap(View view){
        //if seconds >=1
        if (mViewModel.getStopwatchState().getValue().seconds>=1){
            mViewModel.getStopwatchState().getValue().memorisedLaps += "lap " + ((mViewModel.getStopwatchState().getValue().lapCount++) + 1) + " " + calculateTimeFormat() + "\n";
            updateLapData();
            mViewModel.getStopwatchState().getValue().seconds = 0;
            decideLapButtonVisibility(); }

    }

    /**this method takes no params, it is called whenever user clicks the stopwatch start/ stop button and it is called on activity init
     * @param
     * @return void
     * */
    public void decideLapButtonVisibility(){
        if(mViewModel.getStopwatchState().getValue().running || mViewModel.getStopwatchState().getValue().memorisedLaps.length()>0 || mViewModel.getStopwatchState().getValue().seconds>0) {
            buttonLap.setVisibility(View.VISIBLE);
        }
        else {
            buttonLap.setVisibility(View.GONE);
        }

    }
    /**this method takes the View as an argument and is called when user clicks on reset button in the stopwatch
     * @param view
     * @return void
     * */
    public void onClickReset(View view){
        mViewModel.getStopwatchState().getValue().running=false;
        mViewModel.getStopwatchState().getValue().seconds=0;
        mViewModel.getStopwatchState().getValue().lapCount=0;
        mViewModel.getStopwatchState().getValue().memorisedLaps="";
        updateLapData();
    }
    /**this method takes the seconds stored in the stopwatch viewmodel and formats them in a string
     * @params none
     * @return String
     * */
    String calculateTimeFormat(){
        int hours= (int)mViewModel.getStopwatchState().getValue().seconds/3600;
        int minutes = (int)( mViewModel.getStopwatchState().getValue().seconds % 3600)/60;
        int secs= (int) mViewModel.getStopwatchState().getValue().seconds % 60;
        return String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, secs);
    }

    /**
     * method updates lap data inside fragment using the saved value in stopwatch view model
     * @param
     * @return
     * */
    public void updateLapData() {
        lapDataTextView.setText( mViewModel.getStopwatchState().getValue().memorisedLaps);

    }

}