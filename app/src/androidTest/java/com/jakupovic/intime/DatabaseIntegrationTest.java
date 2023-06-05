package com.jakupovic.intime;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.jakupovic.intime.dataBase.InTimeDataBase;
import com.jakupovic.intime.dataBase.dbDAO.AlarmDAO;
import com.jakupovic.intime.dataBase.dbDAO.ClockDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
/**
 * this class contains all tests which integrate the database with the application methods
 * */
@RunWith(AndroidJUnit4.class)
public class DatabaseIntegrationTest {
    private InTimeDataBase dataBase;
    private AlarmDAO alarmDAO;
    private ClockDAO clockDAO;

    @Before
    /**
     * this method prepares the in memory DB and fragments
     * @params none
     * @return void
     * */
    void InitFragmentsAndDB(){
        Context context= ApplicationProvider.getApplicationContext();
        dataBase= Room.inMemoryDatabaseBuilder(context, InTimeDataBase.class).build();
        alarmDAO= dataBase.alarmDAO();
        clockDAO= dataBase.clockDAO();
    }

    @After
    /**
     * this method closes the in memory DB and fragments
     * @params none
     * @return void
     * */
    void CloseFragmentsAndDB(){
        dataBase.close();
    }
}
