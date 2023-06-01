package com.jakupovic.intime.dataBase.dbDAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.jakupovic.intime.dataBase.Alarm;

/**
 * this interface serves as a database acces object for the Alarm entity
 * */
@Dao
public interface AlarmDAO {
    @Insert
    void insert(Alarm alarm);

    //TODO: add @query, remove methods and data update methods for alarm database
}

