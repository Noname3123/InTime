package com.jakupovic.intime.fragments;

import android.os.Handler;

import com.jakupovic.intime.dataBase.Clock;
import com.jakupovic.intime.dataBase.dbDAO.AlarmDAO;
import com.jakupovic.intime.dataBase.dbDAO.ClockDAO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * class in the clock view model which contains the ref to the calendar and seconds (time)
 * */
public class ClockLocalData {
public LocalDateTime time;
/**handler responsible for updating clock inside clock menu
 * */
public Handler clockUpdateHandler=new Handler();

public ClockDAO clockDAO=MainActivity.database.clockDAO();
/**
 * represents an instance of a clock gotten from database (by the GetClockByID method)
 * */
public Clock clockInstance;



}
