package com.acube.jims.datalayer.remote.db;


import android.content.Context;

import androidx.room.Room;

import com.acube.jims.datalayer.constants.AppConstants;

public class DatabaseClient {
    private Context mCtx;
    private static DatabaseClient mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;


        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, AppConstants.Menu_DB).build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
