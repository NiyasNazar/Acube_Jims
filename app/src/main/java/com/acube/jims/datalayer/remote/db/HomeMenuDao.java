package com.acube.jims.datalayer.remote.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.acube.jims.datalayer.models.ConsignmentLine;
import com.acube.jims.datalayer.models.Filter.Colorresult;
import com.acube.jims.datalayer.models.Filter.FilterStore;
import com.acube.jims.datalayer.models.Filter.Karatresult;
import com.acube.jims.datalayer.models.Filter.Weight;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.Invoice.KaratPrice;
import com.acube.jims.datalayer.models.OfflineConsignment;
import com.acube.jims.datalayer.remote.dbmodel.HomeMenu;

import java.util.List;

@Dao
public interface HomeMenuDao {
    @Query("SELECT * FROM HomeData where menuName!='Mobile'")
    LiveData<List<HomeData>> getAll();


    @Query("SELECT * FROM FilterStore")
    LiveData<List<FilterStore>> getAllFilterStore();

    @Query("SELECT * FROM Karatresult")
    LiveData<List<Karatresult>> getAllKaratresultStore();


    @Query("SELECT * FROM Colorresult")
    LiveData<List<Colorresult>> getAllColorresult();

    @Query("SELECT * FROM Karatresult where isSelected='1'")
    LiveData<List<Karatresult>> getselectedkarat();

    @Query("SELECT * FROM KaratPrice")
    LiveData<List<KaratPrice>> getGoldRate();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<HomeData> homeMenu);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertkaratresults(List<Karatresult> karatresults);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertcolorresults(List<Colorresult> colorresults);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertweights(List<Weight> weights);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertfilterStores(List<FilterStore> filterStores);

    @Query("UPDATE filterstore SET isselected = (CASE id WHEN :id THEN 1 ELSE 0 END) WHERE isselected = 1 OR id = :id")
    void update(int id);

    @Query("UPDATE Karatresult SET isselected =:flag where id=:id ")
    void updatekarat(int flag, int id);

    @Query("UPDATE Colorresult SET isselected =:flag where id=:id ")
    void updateColor(int flag, int id);

    @Query("UPDATE Colorresult SET isselected =:flag")
    void updateColor(int flag);

    @Query("UPDATE Karatresult SET isselected =:flag")
    void updateKaratresult(int flag);

    @Query("UPDATE filterstore SET isselected =:flag")
    void updatefilterstore(int flag);


    @Query("SELECT isselected FROM FilterStore where id=:ID")
    Boolean selection(int ID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertgoldrate(List<KaratPrice> karatPrices);

    @Delete
    void delete(HomeData homeMenu);

    @Update
    void update(HomeData homeMenu);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOfflineConsignment(List<ConsignmentLine> offlineConsignment);

    @Query("SELECT DISTINCT consignmentId FROM ConsignmentLine")
    LiveData<List<OfflineConsignment>> getofflineconsignment();

    @Query("SELECT * FROM ConsignmentLine where consignmentId=:consignmentID")
    LiveData<List<ConsignmentLine>> getconsignmentId(String consignmentID);

    @Query("UPDATE  ConsignmentLine set status=:status  where consignmentId=:consignmentId AND   status <>'60' AND  status <>'70' AND  serialNumber IN(Select SerialNo from TemDataSerial)")
    void updateconsignment(String consignmentId, int status);

    @Query("UPDATE  ConsignmentLine set status=:status  where consignmentId=:consignmentId AND   status <>'60' AND  status <>'70' AND  serialNumber=:serial")
    void updatesalestatus(String consignmentId, int status, String serial);

    @Query("UPDATE  ConsignmentLine set status=:status  where consignmentId=:consignmentId AND   status <>'60' AND  status <>'70'")
    void resetconsigmnment(String consignmentId, int status);

    @Query("SELECT COUNT(*) FROM ConsignmentLine WHERE consignmentId =:consignmentId AND status IN(:flagdownloaded,:flagfound,:sold,:created) ")
    LiveData<Integer> getallcountbycat(String consignmentId, int flagdownloaded, int flagfound, int sold, int created);

    @Query("SELECT COUNT(*) FROM ConsignmentLine WHERE consignmentId =:consignmentId AND status=:flag ")
    LiveData<Integer> getcount(String consignmentId, int flag);

    @Query("Delete from ConsignmentLine where  consignmentId =:consignmentId")
    void deleteconsignmentId(String consignmentId);

    @Query("Delete from HomeData")
    void deletehomemenus();
}
