package com.jakupovic.intime.fragments;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.jakupovic.intime.R;
import com.jakupovic.intime.alarmEditMenu.AlarmEditSettings;
import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.dataBase.Clock;

import java.util.ArrayList;
import java.util.concurrent.Future;

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
                //addAlarmCard(); //TODO: replace with a more appropriate function (create/edit alarm activity)
                Intent intent=new Intent(alarmFragment_Context, AlarmEditSettings.class); //gets the intent for which the new activity/window will open
                waitForLoadListOfClocks();
                intent.putExtra("LIST_OF_CLOCKS",new ArrayList<Clock>(mViewModel.getAlarmViewModelData().getValue().allClockInstances)); //send the list of all clock instances as arraylist<Clock>
                startActivity(intent); //create a new window which edits the alarm settings
            }
        });


        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FragmentAlarmViewModel.class);
       //TODO: remove this line insertAlarmAsync(new Alarm(Long.valueOf("1685980800000"),Long.valueOf("1685973600000"),"This is a title","This is an alarm Descr",true));
       //TODO: remove this line insertAlarmAsync(new Alarm(Long.valueOf("1685980800000"),Long.valueOf("1685973600000"),"This is a 2nd title","This is an alarm Descr",true));


    }

/**
 * method called when onResume event happens( such as fragment temporarily closes)
 * */
@Override
public void onResume(){
        super.onResume();
        if(alarmCardContainer.getChildCount()>0){
            alarmCardContainer.removeAllViews();
        }
    mViewModel.getAllAlarmsAsync(this::addAlarmCard); // call the getAllAlarms method and send a consumer from this class (addAlarmCard)

}

    /**This method instantiates an alarm card containing data about alarms
     * @param alarmInstance - instance of entity from database (class: Alarm)
     * @return void
     * */
    public void addAlarmCard(Alarm alarmInstance){

        //inflate a card view with a context, from xml: alarm_data_card and without a root/parent
        CardView card = (CardView) View.inflate(this.getContext(),R.layout.alarm_data_card,null);
        registerAlarmCardButtons(card);

        if(alarmInstance!=null){
            card.setId(alarmInstance.alarmID);
            TextView title= (TextView)card.findViewById(R.id.AlarmCardTItle);
            title.setText(alarmInstance.alarmTitle);

            TextView timezoneLocal= (TextView)card.findViewById(R.id.AlarmCardTimezoneLocalTime);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(alarmInstance.localStartTime);
            timezoneLocal.setText(new SimpleDateFormat("HH:mm").format(cal.getTime()));

            TextView timezoneTime= (TextView)card.findViewById(R.id.alarmCardTimezoneTime);
            cal.setTimeInMillis(alarmInstance.timeToStartInTimezone);
            timezoneTime.setText(timezoneTime.getText()+"\n"+new SimpleDateFormat("HH:mm").format(cal.getTime()));

            SwitchMaterial alarmSwitch = (SwitchMaterial) card.findViewById(R.id.alarmCardSwitch);
            alarmSwitch.setChecked(alarmInstance.enabled);


        }
        //adds a cardview to the container (linear layout)
        alarmCardContainer.addView(card);
    }
    /**this method registers buttons for cardview in alarm card so that the user can use them
     * @param card - CardView for which the buttons need to be registered
     * @return void
     * */
    public void registerAlarmCardButtons(CardView card){
        //TODO: add event registers for enabled switch
        //get buttons
        Button editButton=card.findViewById(R.id.buttonEdit);
        Button enabledSwitch=card.findViewById(R.id.alarmCardSwitch);
        Button removeButton=card.findViewById(R.id.buttonDelete);
        //add listeners
        //adds remove card listener
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View toDelete=(View)(((View)v.getParent()).getParent());
                mViewModel.deleteAlarmInstance(toDelete.getId());

                alarmCardContainer.removeView((View)(((View)v.getParent()).getParent()));
            }

        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardView card=(CardView)(((View)v.getParent()).getParent());
                Future toExecute=mViewModel.getAlarmByID( card.getId());
                while(toExecute.isDone()==false){
                    continue; //wait while the getbyid method thread is finished
                }
                Intent intent=new Intent(alarmFragment_Context, AlarmEditSettings.class); //gets the intent for which the new activity/window will open
                intent.putExtra("ALARM_TO_EDIT",mViewModel.getAlarmViewModelData().getValue().alarmInstance);
                waitForLoadListOfClocks();
                intent.putExtra("LIST_OF_CLOCKS",new ArrayList<Clock>(mViewModel.getAlarmViewModelData().getValue().allClockInstances)); //send the list of all clock instances as arraylist<Clock>
                startActivity(intent); //create a new window which edits the alarm settings

            }
        });
    }
    /**
     * this method makes the main thread wait until all user created clocks are recieved from the database
     * @param
     * @return void
     * */
    void waitForLoadListOfClocks(){
        Future toExecute= mViewModel.getAllUserCreatedClocks();
        while(toExecute.isDone()==false){
            continue;
        }

    }
    //TODO: add logic for registering/unregistering alarms with the Android OS alarm system - the logic will be implemented in the androidFragment view model
}

