package com.duyuqian.network;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Person.class}, version = 1)
public abstract class LocalDataSource extends RoomDatabase {
    public abstract PersonDao personDao();
}
