package com.jakupovic.intime.dataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Date;
/**
 * this class represents entity for alarms and stores alarm data in DB
 * */
@Entity
public class Alarm {
    @PrimaryKey (autoGenerate = true)
    public int alarmID;
    @ColumnInfo(name= "time zone time")
    /**
     * Convert date data object to long and vice-versa when storing time into DB
     * */
    public Long timeToStartInTimezone;
    @ColumnInfo (name="local activation time")
    /**
     * Convert date data object to long and vice-versa when storing time into DB
     * */
    public Long localStartTime;
    @ColumnInfo (name="Alarm title")
    public String alarmTitle;
    @ColumnInfo (name="alarm description")
    public String alarmDesc;
    @ColumnInfo(name = "Enabled")
    public boolean enabled;

    /**
     * constructor for alarm class
     * @param alarmID - id of the alarm (int), used when updating existing entry
     * @param timeToStartInTimezone - time of alarm activation, calculated in selected timezone (long)
     * @param localStartTime - time of alarm activation in timezone local to the phone
     * @param alarmTitle  - title of alarm (string)
     * @param alarmDesc  - description of alarm (string)
     * @param enabled - is the alarm active? (bool)
     * */@Ignore
    public Alarm(int alarmID,Long timeToStartInTimezone, Long localStartTime, String alarmTitle, String alarmDesc, boolean enabled){
        if(alarmID!=-1){this.alarmID=alarmID;}
        this.timeToStartInTimezone=timeToStartInTimezone;
        this.localStartTime=localStartTime;
        this.alarmTitle=alarmTitle;
        this.alarmDesc=alarmDesc;
        this.enabled=enabled;
    }
    /**
     * overriden constructor for alarm class, used when creating new entries not existing in DataBase
     * @param timeToStartInTimezone - time of alarm activation, calculated in selected timezone (long)
     * @param localStartTime - time of alarm activation in timezone local to the phone
     * @param alarmTitle  - title of alarm (string)
     * @param alarmDesc  - description of alarm (string)
     * @param enabled - is the alarm active? (bool)
     * */
    public Alarm(Long timeToStartInTimezone, Long localStartTime, String alarmTitle, String alarmDesc, boolean enabled){

        this(-1,timeToStartInTimezone,localStartTime,alarmTitle,alarmDesc,enabled);
    }
}
