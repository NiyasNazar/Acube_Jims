package com.acube.jims.datalayer.remote.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.acube.jims.Presentation.ScanItems.ResponseItems;
import com.acube.jims.datalayer.models.Favorites.ResponseFavorites;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.remote.dbmodel.CustomerHistory;
import com.acube.jims.datalayer.remote.dbmodel.HomeMenu;


@Database(entities = {HomeData.class, ResponseItems.class, CustomerHistory.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HomeMenuDao homeMenuDao();

    public abstract CompareItemsDao compareItemsDao();

    public abstract ScannedItemsDao scannedItemsDao();

    public abstract CustomerHistoryDao customerHistoryDao();

}

