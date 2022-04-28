package com.acube.jims.datalayer.remote.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.acube.jims.presentation.ScanItems.ResponseItems;

import java.util.List;


@Dao
public interface ScannedItemsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ResponseItems items);

    @Query("DELETE FROM RESPONSEITEMS")
    void delete();

    @Query("UPDATE `ResponseItems` SET Status=:status WHERE serialNumber = :serialnumber")
    void update(String status, String serialnumber);

    @Query("SELECT * FROM `ResponseItems` where Status=:status")
    List<ResponseItems> getAllbystatus(String status);
}
