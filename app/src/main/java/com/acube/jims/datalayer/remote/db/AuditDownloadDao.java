package com.acube.jims.datalayer.remote.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.acube.jims.datalayer.models.Audit.AuditReportItems;
import com.acube.jims.datalayer.models.Audit.AuditResults;
import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.models.LocateProduct.LocateItem;

import java.util.List;

@Dao
public interface AuditDownloadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<AuditResults> items);

    @Query("DELETE FROM LocateItem")
    void delete();

   /* @Query("UPDATE `ResponseItems` SET Status=:status WHERE serialNumber = :serialnumber")
    void update(String status, String serialnumber);*/

    @Query("SELECT * FROM `LocateItem` ")
    List<LocateItem> getAll();

    @Query("DELETE  FROM `LocateItem` ")
    void deleteall();

    @Query("SELECT DISTINCT  auditId ,toBeAuditedOn ,remark FROM InventoryAudit where itemname is Not Null")
    LiveData<List<LocalAudit>> getDownloadedAudits();

    @Query("SELECT DISTINCT  auditId ,toBeAuditedOn ,remark ,systemLocationID,systemLocationName FROM InventoryAudit where itemname is Not Null")
    LiveData<List<LocalAuditLocation>> getDownloadedLocation();

    @Query("SELECT DISTINCT  categoryID,categoryName FROM InventoryAudit where itemname is Not Null")
    LiveData<List<LocalCategory>> getDownloadedCategory();

    @Query("SELECT * from InventoryAudit where auditID=:auditID")
    LiveData<List<AuditResults>> getDownloadedAuditss(String auditID);

    @Query("SELECT COUNT(*) FROM InventoryAudit WHERE auditID =:auditID AND status IN(:flagdownloaded,:flagfound) AND systemLocationID=:locationID")
    LiveData<Integer> getallcount(String auditID, int flagdownloaded, int flagfound, int locationID);

    @Query("SELECT COUNT(*) FROM InventoryAudit WHERE auditID =:auditID AND status IN(:flagdownloaded,:flagfound) AND systemLocationID=:locationID AND categoryID=:catID")
    LiveData<Integer> getallcountbycat(String auditID, int flagdownloaded, int flagfound, int locationID, int catID);

    @Query("SELECT COUNT(*) FROM InventoryAudit WHERE auditID =:auditID AND status=:flag AND systemLocationID=:locationID")
    LiveData<Integer> getcount(String auditID, int flag, int locationID);

    @Query("SELECT COUNT(*) FROM InventoryAudit WHERE auditID =:auditID AND status=:flag AND systemLocationID=:locationID AND categoryID=:catID")
    LiveData<Integer> getcountbycat(String auditID, int flag, int locationID, int catID);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTemporarySerialNos(List<TemDataSerial> data);

    @Query("UPDATE  InventoryAudit set status='1', scanLocationID=:locID where auditID=:auditID AND   status <>'2' AND  serialNumber IN(Select SerialNo from TemDataSerial)AND systemLocationID=:locID")
    void checkauditscan(String auditID, int locID);

    @Query("UPDATE  InventoryAudit set status='1', scanLocationID=:locID where auditID=:auditID AND   status <>'2' AND  serialNumber IN(Select SerialNo from TemDataSerial) AND categoryID=:catID")
    void checkauditscanwithcat(String auditID, int locID, int catID);

    @RawQuery()
    boolean unkwowninsert(SimpleSQLiteQuery simpleSQLiteQuery);

    @Query("SELECT * FROM InventoryAudit  WHERE auditID =:auditID AND status = :flagfound AND categoryID=:catID OR  auditID =:auditID AND status = :flagfound")
    LiveData<List<AuditReportItems>> getMissing(String auditID, int flagfound, int catID);

    @Query("SELECT * FROM InventoryAudit WHERE auditID =:auditID AND status IN(:flagdownloaded,:flagfound)")
    LiveData<List<AuditReportItems>> gettotalstock(String auditID, int flagdownloaded, int flagfound);


    @Query("SELECT COUNT(*) FROM InventoryAudit where auditId=:Id AND status='1' AND scanLocationID =:locationID AND scanLocationID !='0'AND systemLocationID<>scanLocationID")
    LiveData<Integer> checklocmismatchscanned(int Id, int locationID);


}
