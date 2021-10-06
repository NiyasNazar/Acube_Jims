package com.acube.jims.datalayer.remote.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.acube.jims.Presentation.Quotation.adapter.DiscountItem;
import com.acube.jims.datalayer.models.HomePage.HomeData;

import java.util.List;

@Dao
public interface DiscountItemsDao {
    @Query("SELECT SUM(discount) as discount FROM `DiscountItem`")
    Double getdiscountsum();
    @Query("SELECT SUM(amt) as amt FROM `DiscountItem`")
    Double totalwithouttax();
    @Query("SELECT SUM(totalwithtax) as totalwithtax FROM `DiscountItem`")
    Double gettotalpayable();
    @Query("SELECT SUM(totaltax) as totaltax FROM `DiscountItem`")
    Double gettotaltax();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DiscountItem discountItems);

    @Delete
    void delete(DiscountItem discountItems);

    @Query("DELETE  FROM `DiscountItem`")
    void deleteall();

    @Update
    void update(DiscountItem discountItems);
    @Query("SELECT * FROM DiscountItem")
    List<DiscountItem> getAll();

}
