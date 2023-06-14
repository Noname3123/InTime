package com.jakupovic.intime.fragments;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
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

import com.jakupovic.intime.ClockEditMenu.ClockEditActivity;
import com.jakupovic.intime.R;
import com.jakupovic.intime.dataBase.Clock;
import com.jakupovic.intime.interfaces.HandlerManager;

import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ClockFragment extends Fragment implements HandlerManager {

    private ClockViewModel mViewModel;
    private TextView localClock;
    private int clockUpdateInterval=1000; //clock update interval in ms
//clock card container contains all cards which represent clocks
    private ViewGroup clockCardContainer;

    private Button addClockEntryBTN;

    private Context clockFragmentContext;
    public static ClockFragment newInstance() {
        return new ClockFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragment= (View) inflater.inflate(R.layout.fragment_clock, container, false); //inflate fragment
        localClock=fragment.findViewById(R.id.ClockLocalTime);
        clockCardContainer= (ViewGroup) fragment.findViewById(R.id.ClockCardList);
        addClockEntryBTN=fragment.findViewById(R.id.buttonAddClock);
        clockFragmentContext=fragment.getContext();

        //register add btn funcs
        addClockEntryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //addClockCard(); TODO: remove this line
                Intent intent=new Intent(clockFragmentContext, ClockEditActivity.class);
                startActivity(intent);
            }
        });


        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ClockViewModel.class);

    }



    @Override
    //called when activity is stopped (becouse a new one is open), it unregisters the clock handler
    public void onStop(){
        super.onStop();
        HandlerManager.ResetHandler(mViewModel.getClockData().getValue().clockUpdateHandler);
    }
    @Override
    //re-registers clock handler when the activity resumes itself
    public void onResume(){
        super.onResume();
        HandlerManager.RegisterHandlerFunction(mViewModel.getClockData().getValue().clockUpdateHandler, new Runnable() {
            @Override
            public void run() {
                mViewModel.getClockData().getValue().time = new GregorianCalendar().getTime();
                localClock.setText(calculateClockFormat());

                mViewModel.getClockData().getValue().clockUpdateHandler.postDelayed(this, clockUpdateInterval);
            }
        });
    }
/**this method adds a card representing a clock in a timezone
 * @params clock (entity database type)
 * @return void
 * */
    public void addClockCard() {
        //TODO: this func will take a parameter which represents clock timezone data, and will instantiate a card instance in the scroll view, id of the created card must be equal to the id of the clock entry in the DB

        //inflate a card view with a context, from xml: clock_data_card and without a root/parent
        CardView card = (CardView) View.inflate(this.getContext(), R.layout.clock_data_card, null);
        card.findViewById(R.id.buttonDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clockCardContainer.removeView((View)(((View)v.getParent()).getParent()));
            }
        });
        //adds a cardview to the container (linear layout)
        clockCardContainer.addView(card);
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