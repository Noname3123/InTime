package com.jakupovic.intime.fragments;

import androidx.cardview.widget.CardView;
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

import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ClockFragment extends Fragment {

    private ClockViewModel mViewModel;
    private TextView localClock;
    private int clockUpdateInterval=1000; //clock update interval in ms
//clock card container contains all cards which represent clocks
    private ViewGroup clockCardContainer;

    private Button addClockEntryBTN;

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

        //register add btn funcs
        addClockEntryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClockCard();
            }
        });


        runClock();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ClockViewModel.class);
    }

/**this method adds a card representing a clock in a timezone
 * @params clock (entity database type)
 * @return void
 * */
    public void addClockCard() {
        //TODO: this func will take a parameter which represents clock timezone data, and will instantiate a card instance in the scroll view

        //inflate a card view with a context, from xml: alarm_data_card and without a root/parent
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