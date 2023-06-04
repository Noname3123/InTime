package com.jakupovic.intime.dataBase.dbDAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jakupovic.intime.dataBase.Alarm;
import com.jakupovic.intime.dataBase.Clock;

import java.util.List;
import java.util.TimeZone;

/**
 * this interface serves as a database acces object for the Alarm entity
 * */
@Dao
public interface AlarmDAO {
    @Insert
    void insert(Alarm alarm);

    //TODO: add @query, remove methods and data update methods for alarm database
    @Query("SELECT * FROM Alarm")
    List<Alarm> getAll();
    @Query("SELECT * FROM Alarm where Alarm.alarmID == :alarmID")
    Alarm getAlarmByID(int alarmID);

    @Query("SELECT * FROM Alarm where Alarm.`Alarm title` LIKE :alarmTitle AND Alarm.`alarm description` LIKE :alarmDesc ")
    List<Alarm> getAlarmByTitleAndDescr(String alarmTitle, String alarmDesc);
    @Update
    void updateClock(Alarm alarm);
    @Delete
    void delete(Alarm alarm);
}

