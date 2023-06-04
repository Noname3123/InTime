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
    /**
     * this method inserts alarm instance into DB
     * @params Alarm alarm
     * @return long (id of the inserted entity)
     * */
    long insert(Alarm alarm);

    @Query("SELECT * FROM Alarm")
    /**
     * this method returns all alarm instances
     * @params none
     * @return List<Alarm>
     * */
    List<Alarm> getAll();
    @Query("SELECT * FROM Alarm where Alarm.alarmID == :alarmID")
    /**
     * this methods gets alarm instance by using its ID
     * @params int alarmID
     * @return Alarm
     * */
    Alarm getAlarmByID(int alarmID);

    @Query("SELECT * FROM Alarm where Alarm.`Alarm title` LIKE :alarmTitle AND Alarm.`alarm description` LIKE :alarmDesc ")
    /**
     * this methods gets alarm instances by comparing with alarm title and alarm description
     * @params String alarmTitle, String alarmDesc
     * @return List<Alarm>
     * */
    List<Alarm> getAlarmByTitleAndDescr(String alarmTitle, String alarmDesc);
    @Update
    /**
     * this method updates alarm instance inside database by matching id of object sent to the DAO
     * @params Alarm alarm
     * @return Void
     * */
    void updateAlarm(Alarm alarm);
    @Delete
    /**
     * this method deletes alarm instance from database by matching id of object sent to the DAO
     * @params Alarm alarm
     * @return Void
     * */
    void delete(Alarm alarm);
}

