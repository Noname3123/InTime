package com.jakupovic.intime.dataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
/**
 * this class represents entity for alarms and stores alarm data in DB
 * */
@Entity
public class Alarm implements Serializable {
    @PrimaryKey (autoGenerate = true)
    public int alarmID;
    /**
     * start time (center of alarm card), represents the actual time the alarm will activate in DEFINED/SELECTED timezone
     * */
    @ColumnInfo(name= "time zone time")
    /**
     * Convert date data object to long and vice-versa when storing time into DB
     * */
    public Long timeToStartInTimezone;
    /**
     * local start time (center of alarm card), represents the actual time the alarm will activate in YOUR timezone
     * */
    @ColumnInfo (name="local activation time")
    /**
     * Convert date data object to long and vice-versa when storing time into DB
     * */
    public Long localStartTime;
    @ColumnInfo (name="Alarm title")
    public String alarmTitle;
    @ColumnInfo (name="alarm description")
    public String alarmDesc;

    @ColumnInfo (name="time zone id")
    public String timeZoneID;
    @ColumnInfo(name = "Enabled")
    public boolean enabled;

    /**
     * constructor for alarm class
     * @param alarmID - id of the alarm (int), used when updating existing entry
     * @param timeToStartInTimezone - time of alarm activation, calculated in selected timezone (long)
     * @param localStartTime - time of alarm activation in timezone local to the phone
     * @param alarmTitle  - title of alarm (string)
     * @param alarmDesc  - description of alarm (string)
     * @param timeZoneID  - id of alarm timezone(string)
     * @param enabled - is the alarm active? (bool)
     * */@Ignore
    public Alarm(int alarmID,Long timeToStartInTimezone, Long localStartTime, String alarmTitle, String alarmDesc,String timeZoneID, boolean enabled){
        if(alarmID!=-1){this.alarmID=alarmID;}
        this.timeToStartInTimezone=timeToStartInTimezone;
        this.localStartTime=localStartTime;
        this.alarmTitle=alarmTitle;
        this.alarmDesc=alarmDesc;
        this.timeZoneID=timeZoneID;
        this.enabled=enabled;
    }
    /**
     * overriden constructor for alarm class, used when creating new entries not existing in DataBase
     * @param timeToStartInTimezone - time of alarm activation, calculated in selected timezone (long)
     * @param localStartTime - time of alarm activation in timezone local to the phone
     * @param alarmTitle  - title of alarm (string)
     * @param alarmDesc  - description of alarm (string)
     * @param timeZoneID  - id of alarm timezone (string)
     * @param enabled - is the alarm active? (bool)
     * */
    public Alarm(Long timeToStartInTimezone, Long localStartTime, String alarmTitle, String alarmDesc,String timeZoneID , boolean enabled){

        this(-1,timeToStartInTimezone,localStartTime,alarmTitle,alarmDesc,timeZoneID,enabled);
    }
}
