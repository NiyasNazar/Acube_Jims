package com.acube.jims.datalayer.remote.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.acube.jims.datalayer.remote.dbmodel.Smarttool;
import com.acube.jims.presentation.ScanItems.ResponseItems;

import java.util.List;


@Dao
public interface ScannedItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ResponseItems items);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCheckboxSelection(Smarttool items);

    @Delete()
    void DeleteCheckboxSelection(Smarttool items);

    @Query("DELETE FROM RESPONSEITEMS")
    void delete();

    @Query("DELETE FROM smarttool")
    void deleteall();


    @Query("UPDATE `ResponseItems` SET Status=:status WHERE serialNumber = :serialnumber")
    void update(String status, String serialnumber);

    @Query("SELECT * FROM `ResponseItems` where Status=:status")
    List<ResponseItems> getAllbystatus(String status);

    @Query("SELECT serialNumber FROM `smarttool` ")
    LiveData<List<String>> getFromSmarttool();

    @Query("SELECT * FROM `smarttool` ")
    LiveData<List<Smarttool>> getFromSmarttoolList();

    @Query("SELECT COUNT(*) FROM smarttool")
    Integer getallcount();

}
