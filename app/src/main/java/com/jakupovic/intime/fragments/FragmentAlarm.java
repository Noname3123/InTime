package com.jakupovic.intime.fragments;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jakupovic.intime.R;
import com.jakupovic.intime.alarmEditMenu.AlarmEditSettings;

public class FragmentAlarm extends Fragment {

    private FragmentAlarmViewModel mViewModel;

    public static FragmentAlarm newInstance() {
        return new FragmentAlarm();
    }

    private Button addAlarm;

private ViewGroup alarmCardContainer;

private Context alarmFragment_Context;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragment= (View)inflater.inflate(R.layout.fragment_fragment_alarm, container, false); //view which will create itself
        alarmFragment_Context=fragment.getContext(); //get context of current view and save it for later use: context - layer of activity/ component which provides functionalities supported by application or Android framework. context exists as long as the activity/ component is active
        //find the scrollbox which contains alarm view
         alarmCardContainer=(ViewGroup) fragment.findViewById(R.id.ClockCardList);

         addAlarm = fragment.findViewById(R.id.buttonAddClock);
        //this executes the add alarm function when the ADD button is clicked
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarmCard(); //TODO: replace with a more appropriate function (create/edit alarm activity)
                Intent intent=new Intent(alarmFragment_Context, AlarmEditSettings.class); //gets the intent for which the new activity/window will open
                startActivity(intent); //create a new window which edits the alarm settings
            }
        });


        //TODO: create a function in onCreateView which reads alarm database and generates cards with addAlarmCard func
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FragmentAlarmViewModel.class);
        // TODO: Use the ViewModel
    }



    /**This method instantiates an alarm card containing data about alarms
     * @params
     * @return void
     * */
    public void addAlarmCard(){
        //TODO: this func will take a parameter which represents alarm data, and will instantiate a card instance in the scroll view

        //inflate a card view with a context, from xml: alarm_data_card and without a root/parent
        CardView card = (CardView) View.inflate(this.getContext(),R.layout.alarm_data_card,null);
        registerAlarmCardButtons(card);
        //adds a cardview to the container (linear layout)
        alarmCardContainer.addView(card);
    }
    /**this method registers buttons for cardview in alarm card so that the user can use them
     * @params CardView
     * @return void
     * */
    public void registerAlarmCardButtons(CardView card){
        //TODO: add event registers for other buttons (such as edit and enable switch)
        //get buttons
        Button editButton=card.findViewById(R.id.buttonEdit);
        Button enabledSwitch=card.findViewById(R.id.alarmSwitch);
        Button removeButton=card.findViewById(R.id.buttonDelete);
        //add listeners
        //adds remove card listener
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add remove from database (after building alarm database)
                alarmCardContainer.removeView((View)(((View)v.getParent()).getParent()));
            }

        });
    }
}