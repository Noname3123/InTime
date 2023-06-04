package com.jakupovic.intime.dataBase;

import android.icu.util.TimeZone;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * this class represents entity for clocks and stores clock data in DB
 * */
@Entity
public class Clock {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="Description of location")
    public String location;

    @ColumnInfo(name="Time zone")
    /**
     * stores the ID of time zone, ID of timezone is string ex. Europe/Oslo. Then you can use TimeZone.getTimeZone("Europe/Oslo") to calculate the timezone
     * */
    public String timeZone;

}
