package com.jakupovic.intime.dataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.jakupovic.intime.dataBase.dbDAO.AlarmDAO;
import com.jakupovic.intime.dataBase.dbDAO.ClockDAO;

/**
 * this abstract class represents the entire database of the InTime application
 * */
@Database(entities = {Alarm.class,Clock.class}, version = 1) //entities (classes) inside of DB
public abstract class InTimeDataBase extends RoomDatabase {
    public abstract AlarmDAO alarmDAO();
    public abstract ClockDAO clockDAO();
}
