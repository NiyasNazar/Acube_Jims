package com.acube.jims.datalayer.remote.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.acube.jims.datalayer.models.Audit.AuditResults;
import com.acube.jims.datalayer.remote.dbmodel.ItemRequestEntry;

import java.util.List;

@Dao
public interface ItemRequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ItemRequestEntry itemRequestEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<ItemRequestEntry> itemRequestEntry);
    @Query("Delete  FROM ItemRequestEntry")
    void DeleteAll();

    @Query("DELETE FROM ItemRequestEntry where itemID=:itemID")
    void delete(int itemID);

    @Query("Select * FROM ItemRequestEntry")
    LiveData<List<ItemRequestEntry>> gettall();
}
