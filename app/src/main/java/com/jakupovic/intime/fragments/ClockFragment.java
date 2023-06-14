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
import com.jakupovic.intime.alarmEditMenu.AlarmEditSettings;
import com.jakupovic.intime.dataBase.Clock;
import com.jakupovic.intime.interfaces.HandlerManager;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Future;

public class ClockFragment extends Fragment implements HandlerManager {

    private ClockViewModel mViewModel;
    private TextView localClock;
    private int clockUpdateInterval=1000; //clock update interval in ms
//clock card container contains all cards which represent clocks
    private ViewGroup clockCardContainer;

    private Button addClockEntryBTN;

    private Context clockFragmentContext;

    /**
     * dictionary containing links between clock times and corresponding timezones of the clock card taken from database
     * */
    private HashMap<TextView,String> listOfForeignClockViews=new HashMap<>(); //a dictionary, every time zone id is linked to appropriate TextView (which is the key)
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
    /**method called when onResume event happens( such as fragment temporarily closes),this method re-registers clock handler when the activity resumes itself*/
    public void onResume(){
        super.onResume();
        HandlerManager.RegisterHandlerFunction(mViewModel.getClockData().getValue().clockUpdateHandler, new Runnable() {
            @Override
            public void run() {
                mViewModel.getClockData().getValue().time = LocalDateTime.now();
                localClock.setText(calculateClockFormat(mViewModel.getClockData().getValue().time));
               //calculate timezone specific time for every clock instance inside DB
                if(!listOfForeignClockViews.isEmpty()){
                    listOfForeignClockViews.keySet().forEach(clock -> {
                        clock.setText(calculateClockFormat(LocalDateTime.now(TimeZone.getTimeZone(listOfForeignClockViews.get(clock)).toZoneId())));
                    });
                }

                mViewModel.getClockData().getValue().clockUpdateHandler.postDelayed(this, clockUpdateInterval);
            }
        });
        if(clockCardContainer.getChildCount()>0){
            clockCardContainer.removeAllViews();
        }
        mViewModel.getAllClocksAsync(this::addClockCard); // call the getAllAlarms method and send a consumer from this class (addAlarmCard)

    }
/**this method adds a card representing a clock in a timezone, it also adds a reference between clock card time entry and timezone for that clock card into the listOfForeignClockViews for
 * the purpose of refreshing appropriate clocks
 * @param clock - (entity database type)
 * @return void
 * */
    public void addClockCard(Clock clock) {


        //inflate a card view with a context, from xml: clock_data_card and without a root/parent
        CardView card = (CardView) View.inflate(this.getContext(), R.layout.clock_data_card, null);
        card.setId(clock.id);
        TextView clockCardTitle=(TextView) card.findViewById(R.id.clockCardTitle);
        clockCardTitle.setText(clock.location);
        TextView clockTimezoneID=(TextView)card.findViewById(R.id.ClockCardTimezoneName);
        clockTimezoneID.setText(clock.timeZone);
        card.findViewById(R.id.buttonDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewToRemove=(View)(((View)v.getParent()).getParent());
                mViewModel.deleteClockInstance(viewToRemove.getId());
                clockCardContainer.removeView(viewToRemove);
            }
        });

        card.findViewById(R.id.buttonEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View card=(View)(((View)v.getParent()).getParent());
                Future toExecute=mViewModel.getClockByID( card.getId());
                while(toExecute.isDone()==false){
                    continue; //wait while the getbyid method thread is finished
                }
                Intent intent=new Intent(clockFragmentContext, ClockEditActivity.class); //gets the intent for which the new activity/window will open
                intent.putExtra("CLOCK_TO_EDIT",mViewModel.getClockData().getValue().clockInstance);
                startActivity(intent); //create a new window which edits the alarm settings

            }
        });
        listOfForeignClockViews.put((TextView) card.findViewById(R.id.ClockCardTimezoneLocalTime),clock.timeZone); // add ref to the watch in the clock card and the corresponding timezone into the HashMap
        //adds a cardview to the container (linear layout)
        clockCardContainer.addView(card);

    }
    /**this generic method takes the date object stored in clock view model and formats its time entry
     * @param time - LocalDateTime or ZonedDateTime which will be formatted into string
     * @return String
     * */
    String calculateClockFormat(LocalDateTime time){

        return time.format(DateTimeFormatter.ofPattern("HH:mm",Locale.getDefault()));
    }

}