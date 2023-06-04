package com.jakupovic.intime;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.dataBase.Clock;
import com.jakupovic.intime.dataBase.InTimeDataBase;
import com.jakupovic.intime.dataBase.dbDAO.AlarmDAO;
import com.jakupovic.intime.dataBase.dbDAO.ClockDAO;
import com.jakupovic.intime.fragments.FragmentStopwatch;

import java.util.List;

/**
 * Contains tests related to database management
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTests {
    private InTimeDataBase dataBase;
    private AlarmDAO alarmDAO;
    private ClockDAO clockDAO;

    @Before
    /**
     * this void method prepares the in memory database before executing test and stores the necessary DAO objects
     * @params none
     * @return void
     * */
    public void PrepareDB(){
        Context context= ApplicationProvider.getApplicationContext();
        dataBase= Room.inMemoryDatabaseBuilder(context, InTimeDataBase.class).build();
        alarmDAO= dataBase.alarmDAO();
        clockDAO= dataBase.clockDAO();
    }


    @After
    /**
     * this void method closes the database after all the tests have been executed
     * @params none
     * @return void
     * */
    public void RemoveDB(){
        dataBase.close();
    }
    @Test
    /**
     * this void method creates an alarm instance and saves it in DB
     * @params none
     * @return void
     * */
    public void AddAlarm() throws Exception{
        Alarm alarm=new Alarm(Long.valueOf(2),Long.valueOf(1),"test alarm","This is a test alarm to be introduced in DB",true);
       //values written into database
       //alarm.alarmDesc="This is a test alarm to be introduced into DB";
        //alarm.alarmTitle="test alarm";
        //alarm.localStartTime=Long.valueOf(1) ;
        //alarm.timeToStartInTimezone=Long.valueOf(2);
        //alarm.enabled=true;

        alarmDAO.insert(alarm);
        //get inserted alarm
        List<Alarm> inserted= alarmDAO.getAlarmByTitleAndDescr(alarm.alarmTitle,alarm.alarmDesc);
        inserted.forEach( queriedAlarm -> {
            assertEquals(alarm.alarmTitle,queriedAlarm.alarmTitle);
            assertEquals(alarm.alarmDesc,queriedAlarm.alarmDesc);
            assertEquals(alarm.localStartTime,queriedAlarm.localStartTime);
            assertEquals(alarm.timeToStartInTimezone,queriedAlarm.timeToStartInTimezone);
            assertEquals(alarm.enabled,queriedAlarm.enabled);
        });

    }

    @Test
    /**
     * this void method creates an alarm instance, saves it in DB and updates it
     * @params none
     * @return void
     * */
    public void UpdateAlarm() throws Exception{
        Alarm alarm=new Alarm(Long.valueOf(2),Long.valueOf(1),"test alarm","This is a test alarm to be introduced in DB",true);
        int insertedID = (int)alarmDAO.insert(alarm); //first insert the "original" version of alarm entry and get its ID


        //get inserted alarm
        Alarm alarmModified=new Alarm(insertedID,Long.valueOf(2),Long.valueOf(5),"test alarm","This is ",false); //create a new alarm instance with ID modified for update



            alarmDAO.updateAlarm(alarmModified);


        Alarm updated= alarmDAO.getAlarmByID(insertedID);

            assertEquals(alarmModified.alarmTitle,updated.alarmTitle);
            assertEquals(alarmModified.alarmDesc,updated.alarmDesc);
            assertEquals(alarmModified.localStartTime,updated.localStartTime);
            assertEquals(alarmModified.timeToStartInTimezone,updated.timeToStartInTimezone);
            assertEquals(alarmModified.enabled,updated.enabled);


    }
/**
 * this method tests the get alarm by ID method
 * */@Test
    public void GetAlarmByID(){
        Alarm alarm=new Alarm(Long.valueOf(2),Long.valueOf(1),"test alarm","This is a test alarm to be introduced in DB",true);
        int insertedAlarmID=(int)alarmDAO.insert(alarm); //first insert the "original" version of alarm entry and get its ID



        Alarm alarmQueriedByID=alarmDAO.getAlarmByID(insertedAlarmID); //get alarm by id

        assertNotNull(alarmQueriedByID);
        //test field values
        assertEquals(alarm.alarmTitle,alarmQueriedByID.alarmTitle);
        assertEquals(alarm.alarmDesc,alarmQueriedByID.alarmDesc);
        assertEquals(alarm.localStartTime,alarmQueriedByID.localStartTime);
        assertEquals(alarm.timeToStartInTimezone,alarmQueriedByID.timeToStartInTimezone);
        assertEquals(alarm.enabled,alarmQueriedByID.enabled);

    }

    /**
     * delete a created alarm from db
     * */@Test
    public void DeleteAlarmFromDB(){
        Alarm alarm=new Alarm(Long.valueOf(2),Long.valueOf(1),"test alarm","This is a test alarm to be introduced in DB",true);
        int insertedAlarmID=(int)alarmDAO.insert(alarm); //first insert the "original" version of alarm entry and then get its ID



        Alarm alarmQueriedByID=alarmDAO.getAlarmByID(insertedAlarmID); //get alarm by id gotten from the insert

        assertNotNull(alarmQueriedByID);
        alarmDAO.delete(alarmQueriedByID); //delete alarm
        assertNull(alarmDAO.getAlarmByID(insertedAlarmID)); //get the deleted alarm from DB


    }

    @Test
    /**
     * this void method creates a clock instance and saves it in DB
     * @params none
     * @return void
     * */
    public void AddClock() throws Exception{

        Clock clock = new Clock("example location", "example time zone");

        clockDAO.insert(clock);
        //get inserted alarm
        List<Clock> inserted= clockDAO.getClockByDescAndTimeZone(clock.location, clock.timeZone);
        inserted.forEach( queriedClock -> {
            assertEquals(clock.location,queriedClock.location);
            assertEquals(clock.timeZone,queriedClock.timeZone);

        });

    }

    @Test
    /**
     * this void method creates a clock instance, saves it in DB and updates it
     * @params none
     * @return void
     * */
    public void UpdateClock() throws Exception{
        Clock clock = new Clock("example location", "example time zone");
        int insertedID = (int)clockDAO.insert(clock); //first insert the "original" version of clock entry and get its ID


        //get inserted alarm
        Clock clockModified=new Clock(insertedID,"example", "example time zone"); //create a new clock instance with ID modified for update



        clockDAO.updateClock(clockModified);


        Clock updated= clockDAO.getClockByID(insertedID);

        assertEquals(clockModified.location,updated.location);
        assertEquals(clockModified.timeZone,updated.timeZone);



    }
    /**
     * this method tests the get clock by ID method
     * */@Test
    public void GetClockByID(){
        Clock clock = new Clock("example location", "example time zone");
        int insertedID = (int)clockDAO.insert(clock); //first insert the "original" version of clock entry and get its ID



        Clock clockQueriedByID= clockDAO.getClockByID(insertedID);

        assertNotNull(clockQueriedByID);
        //test field values
        assertEquals(clock.location,clockQueriedByID.location);
        assertEquals(clock.timeZone,clockQueriedByID.timeZone);


    }

    /**
     * delete a created clock from db
     * */@Test
    public void DeleteClockFromDB(){
        Clock clock = new Clock("example location", "example time zone");
        int insertedID = (int)clockDAO.insert(clock); //first insert the "original" version of clock entry and get its ID



        Clock clockQueriedByID= clockDAO.getClockByID(insertedID);

        assertNotNull(clockQueriedByID);
        clockDAO.delete(clockQueriedByID); //delete clock
        assertNull(alarmDAO.getAlarmByID(insertedID)); //get the deleted clock from DB


    }


}