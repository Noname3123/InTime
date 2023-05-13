package com.jakupovic.intime.fragments;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jakupovic.intime.R;

public class FragmentAlarm extends Fragment {

    private FragmentAlarmViewModel mViewModel;

    public static FragmentAlarm newInstance() {
        return new FragmentAlarm();
    }

    private Button addAlarm;
private ViewGroup alarmCardContainer;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragment= (View)inflater.inflate(R.layout.fragment_fragment_alarm, container, false);
        //find the scrollbox which contains alarm view
         alarmCardContainer=(ViewGroup) fragment.findViewById(R.id.AlarmCardList);
        addAlarm = fragment.findViewById(R.id.buttonAddAlarm);
        //this executes the add alarm function when the ADD button is clicked
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarmCard(); //TODO: replace with a more appropriate function (create/edit alarm activity)
            }
        });
//TODO: create a function which reads alarm database and generates cards with addAlarmCard func
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