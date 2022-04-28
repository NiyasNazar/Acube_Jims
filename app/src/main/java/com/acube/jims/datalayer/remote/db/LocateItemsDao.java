package com.acube.jims.datalayer.remote.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.acube.jims.datalayer.models.LocateProduct.LocateItem;

import java.util.List;


@Dao
public interface LocateItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LocateItem items);

    @Query("DELETE FROM LocateItem")
    void delete();

   /* @Query("UPDATE `ResponseItems` SET Status=:status WHERE serialNumber = :serialnumber")
    void update(String status, String serialnumber);*/

    @Query("SELECT * FROM `LocateItem` ")
    List<LocateItem> getAll();

    @Query("DELETE  FROM `LocateItem` ")
    void deleteall();
}
