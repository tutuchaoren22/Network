package com.duyuqian.network;

import android.app.Application;

import androidx.room.Room;

public class MyApplication extends Application {
    public static LocalDataSource localDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        localDataSource = Room.databaseBuilder(getApplicationContext(),
                LocalDataSource.class, "dataBase")
                .build();
    }

    public LocalDataSource getLocalDataSource() {
        return localDataSource;
    }
}
