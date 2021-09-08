package com.acube.jims.datalayer.remote.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.acube.jims.Presentation.ScanItems.ResponseItems;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.remote.dbmodel.CustomerHistory;

import java.util.List;


@Dao
public interface CustomerHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CustomerHistory items);

    @Delete
    void delete(CustomerHistory items);

    @Query("SELECT * FROM CustomerHistory where customerID=:customerID")
    List<CustomerHistory> getAll(String customerID);
  /*  @Query("UPDATE `CustomerHistory` SET Status=:status WHERE serialNumber = :serialnumber")
    void update(String status, String serialnumber);

    @Query("SELECT * FROM `CustomerHistory` where Status=:status")
    List<CustomerHistory> getAllbystatus(String status);*/

}
