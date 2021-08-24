package com.acube.jims.datalayer.remote.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.acube.jims.datalayer.models.Favorites.ResponseFavorites;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.remote.dbmodel.HomeMenu;


@Database(entities = {HomeData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HomeMenuDao homeMenuDao();
    public abstract CompareItemsDao compareItemsDao();
}

