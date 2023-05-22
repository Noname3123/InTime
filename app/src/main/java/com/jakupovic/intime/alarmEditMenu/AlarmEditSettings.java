package com.jakupovic.intime.alarmEditMenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.jakupovic.intime.R;

public class AlarmEditSettings extends AppCompatActivity {

    public ImageButton alarmBackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent(); // get intent which started the activity
        setContentView(R.layout.activity_alarm_edit_settings); //"inflate", render the screen depending on defined layout
        alarmBackButton=findViewById(R.id.AlarmEditMenuBackButton);
        //register for a click event for alarmBackButton
        alarmBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            BackButtonCLick(v);
            }
        });

    }
    /**this method is called when back button is pressed and it closes the activity. It takes view as parameter
     * @param v
     * @return void
     * */
    void BackButtonCLick(View v){
        finish(); //finish the current activity
    }

    //TODO: add save alarm logic for save button, use the intent for sending along class instances of data to be edited in case the alarm settings are changed
}