package com.duyuqian.network;

import android.app.Application;

import androidx.room.Room;

public class MyApplication extends Application {
    public static MyApplication myApplication;
    public static LocalDataSource localDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        localDataSource = Room.databaseBuilder(getApplicationContext(),
                LocalDataSource.class, "dataBase")
                .build();
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    public LocalDataSource getLocalDataSource() {
        return localDataSource;
    }
}
