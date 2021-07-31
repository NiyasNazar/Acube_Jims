package com.acube.jims.datalayer.remote.db;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.remote.dbmodel.HomeMenu;

import java.util.List;
@Dao
public interface HomeMenuDao {
    @Query("SELECT * FROM HomeData")
    List<HomeData> getAll();

    @Insert
    void insert(List<HomeData> homeMenu);

    @Delete
    void delete(HomeData homeMenu);

    @Update
    void update(HomeData homeMenu);
}
