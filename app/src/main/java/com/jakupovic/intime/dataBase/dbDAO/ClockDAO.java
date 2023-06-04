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
   void insert(Clock clock);

   //TODO: add @query, update and remove methods for clock database
   @Query("SELECT * FROM Clock")
   List<Clock> getAll();
   @Query("SELECT * FROM Clock where Clock.id == :clockID")
   Clock getClockByID(int clockID);

   @Query("SELECT * FROM Clock where Clock.`Description of location` LIKE :locationDescription AND Clock.`Time zone` LIKE :timeZone ")
   List<Clock> getClockByDescAndTimeZone(String locationDescription, TimeZone timeZone);
   @Update
   void updateClock(Clock clock);
   @Delete
   void delete(Clock clock);
}
