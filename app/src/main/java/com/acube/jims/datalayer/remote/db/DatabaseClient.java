package com.acube.jims.datalayer.remote.db;


import android.content.Context;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.acube.jims.datalayer.constants.AppConstants;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseClient {
    private Context mCtx;
    private static DatabaseClient mInstance;

  static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);
    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;


        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, AppConstants.Menu_DB)
                .setQueryCallback(queryCallback,databaseWriteExecutor)


                .fallbackToDestructiveMigration().build();

    }

    public static RoomDatabase.QueryCallback queryCallback = new RoomDatabase.QueryCallback() {
        @Override
        public void onQuery(@NonNull String sqlQuery, @NonNull List<Object> bindArgs) {
            Log.d("MYQUERY", "onQuery: "+sqlQuery);
        }
    };

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
