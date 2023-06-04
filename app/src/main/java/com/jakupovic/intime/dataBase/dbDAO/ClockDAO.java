package com.jakupovic.intime.dataBase.dbDAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jakupovic.intime.dataBase.Clock;

import java.util.List;
import java.util.TimeZone;

/**
 * this interface serves as a database access object for the Clock entity
 * */
@Dao
public interface ClockDAO {
   @Insert
   /**
    * insert the object into DB and return ID value
    *
    * */
   long insert(Clock clock);

   @Query("SELECT * FROM Clock")
   List<Clock> getAll();
   @Query("SELECT * FROM Clock where Clock.id == :clockID")
   /**
    * this method returns clock instance from database by ID
    * @params int clock ID
    * @return Clock
    * */
   Clock getClockByID(int clockID);

   @Query("SELECT * FROM Clock where Clock.`Description of location` LIKE :locationDescription AND Clock.`Time zone` LIKE :timeZone ")
   /**
    * this method returns clock instances from database with appropriate location and timezone descriptions
    * @params String locationDescription, String timeZone
    * @return List<Clock>
    * */
   List<Clock> getClockByDescAndTimeZone(String locationDescription, String timeZone);
   @Update
   /**
    * this method updates clock instance inside database by matching id of object sent to the DAO
    * @params Clock clock
    * @return Void
    * */
   void updateClock(Clock clock);
   @Delete
   /**
    * this method deletes clock instance from database by matching id of object sent to the DAO
    * @params Clock clock
    * @return Void
    * */
   void delete(Clock clock);
}
