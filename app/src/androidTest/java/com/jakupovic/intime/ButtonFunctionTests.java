package com.jakupovic.intime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.dataBase.InTimeDataBase;
import com.jakupovic.intime.dataBase.dbDAO.AlarmDAO;
import com.jakupovic.intime.dataBase.dbDAO.ClockDAO;
import com.jakupovic.intime.fragments.FragmentAlarm;
import com.jakupovic.intime.fragments.FragmentAlarmViewModel;
import com.jakupovic.intime.fragments.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
/**
 * this class contains all tests which integrate the database with the application methods
 * */
@RunWith(AndroidJUnit4.class)
public class ButtonFunctionTests {
    //TODO: implement save alarm button functionality (medium) test, implement delete alarm button functionality (medium) test

}
