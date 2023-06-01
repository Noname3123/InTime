package com.jakupovic.intime.dataBase.dbDAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.jakupovic.intime.dataBase.Clock;

/**
 * this interface serves as a database access object for the Clock entity
 * */
@Dao
public interface ClockDAO {
   @Insert
   void insert(Clock clock);

   //TODO: add @query, update and remove methods for clock database
}
