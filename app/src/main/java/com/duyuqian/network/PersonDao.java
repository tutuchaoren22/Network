package com.duyuqian.network;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PersonDao {

    @Query("SELECT * FROM person")
    List<Person> getAll();
}
