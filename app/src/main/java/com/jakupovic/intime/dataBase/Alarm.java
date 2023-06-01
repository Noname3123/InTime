package com.jakupovic.intime.dataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
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
    public Date timeToStartInTimezone;
    @ColumnInfo (name="local activation time")
    public Date localStartTime;
    @ColumnInfo(name = "Enabled")
    public boolean enabled;
}
