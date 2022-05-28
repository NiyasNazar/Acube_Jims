package com.acube.jims.datalayer.remote.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.Invoice.KaratPrice;
import com.acube.jims.datalayer.remote.dbmodel.HomeMenu;

import java.util.List;

@Dao
public interface HomeMenuDao {
    @Query("SELECT * FROM HomeData")
    LiveData<List<HomeData> >getAll();

    @Query("SELECT * FROM KaratPrice")
    LiveData<List<KaratPrice>> getGoldRate();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<HomeData> homeMenu);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertgoldrate(List<KaratPrice> karatPrices);

    @Delete
    void delete(HomeData homeMenu);

    @Update
    void update(HomeData homeMenu);
}
