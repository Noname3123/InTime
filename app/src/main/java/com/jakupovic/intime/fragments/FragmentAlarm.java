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

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakupovic.intime.R;
import com.jakupovic.intime.alarmEditMenu.AlarmEditSettings;
import com.jakupovic.intime.dataBase.Alarm;

import java.util.List;

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
        GetAllAlarms();

    }

    /**
     * this method loads all alarms in the background thread
     * */
    public void GetAllAlarms(){
        //handler for the query execute thread
        Handler QueryHandler= new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle messageBundle = msg.getData();
                List<Alarm> allAlarms=new Gson().fromJson(messageBundle.getString("ALL_ALARMS"), new TypeToken<List<Alarm>>(){}.getType());
                //update the view model because the query has executedđ
                mViewModel.getAlarmViewModelData().getValue().allAlarms=allAlarms;



            }
        };

        // executable which contains a query
        Runnable alarmQuery = new Runnable() {
            Message queryHandlerMessage=QueryHandler.obtainMessage(); //get old message if there was one
            Bundle messageBundle=new Bundle(); //create a new bundle for the message
            List<Alarm> allAlarms;
            @Override
            public void run() {
                allAlarms=MainActivity.database.alarmDAO().getAll();
                messageBundle.putString("ALL_ALARMS",new Gson().toJson(allAlarms)); // turn List<> into JSON (string) and add it as a message to handler
                queryHandlerMessage.setData(messageBundle); // put bundle into message
                QueryHandler.sendMessage(queryHandlerMessage);
            }

        };
        Thread queryExecution=new Thread(alarmQuery); // put the query into bg thread and execute
        queryExecution.start();


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

            Switch alarmSwitch = (Switch) card.findViewById(R.id.alarmCardSwitch);
            alarmSwitch.setEnabled(alarmInstance.enabled);


        }
        //adds a cardview to the container (linear layout)
        alarmCardContainer.addView(card);
    }
    /**this method registers buttons for cardview in alarm card so that the user can use them
     * @param card - CardView for which the buttons need to be registered
     * @return void
     * */
    public void registerAlarmCardButtons(CardView card){
        //TODO: add event registers for other buttons (such as edit and enable switch)
        //get buttons
        Button editButton=card.findViewById(R.id.buttonEdit);
        Button enabledSwitch=card.findViewById(R.id.alarmCardSwitch);
        Button removeButton=card.findViewById(R.id.buttonDelete);
        //add listeners
        //adds remove card listener
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add remove from database (after building alarm database)
                View toDelete=(View)(((View)v.getParent()).getParent());
                Alarm alarmToDelete= mViewModel.getAlarmViewModelData().getValue().alarmDAO.getAlarmByID(toDelete.getId());
                mViewModel.getAlarmViewModelData().getValue().alarmDAO.delete(alarmToDelete);
                alarmCardContainer.removeView((View)(((View)v.getParent()).getParent()));
            }

        });
    }
}

