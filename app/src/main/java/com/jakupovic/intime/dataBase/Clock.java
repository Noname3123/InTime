package com.jakupovic.intime.dataBase;

import android.icu.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * this class represents entity for clocks and stores clock data in DB
 * */
@Entity
public class Clock implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="Description of location")
    public String location;

    @ColumnInfo(name="Time zone")
    /**
     * stores the ID of time zone, ID of timezone is string ex. Europe/Oslo. Then you can use TimeZone.getTimeZone("Europe/Oslo") to calculate the timezone
     * */
    public String timeZone;

    /**
     * constructor for the clock class
     * @param id - int id of the entity, used when updating said entity
     * @param location - string description of location
     * @param timeZone - string ID of timezone
     * */@Ignore
public Clock(int id,String location, String timeZone){
  if(id!=-1){ this.id=id;}
    this.location=location;
    this.timeZone=timeZone;
}

    /**
     * overriden constructor for the clock class, used when creating new entity entries in DB
     * @param location - string description of location
     * @param timeZone - string ID of timezone
     * */
    public Clock(String location, String timeZone){
        this(-1,location,timeZone);
    }

    /**
     * override of the toString method which is called when Clock.toString() is called. It returns the location and timezone description as the name of clock instance
     * @param
     * @return String
     * */
    @NonNull
    @Override
    public String toString() {
        return this.location + " ("+this.timeZone+")";
    }
}
