package com.jakupovic.intime.fragments;

import android.os.Handler;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * class in the clock view model which contains the ref to the calendar and seconds (time)
 * */
public class ClockLocalData {
//public GregorianCalendar calendarRef; //TODO: calendar which supports timezones, probably will remain unused here and used only wtih custom clocks saved inside of database
public Date time;
/**handler responsible for updating clock inside clock menu
 * */
public Handler clockUpdateHandler=new Handler();


}
