package com.acube.jims.datalayer.remote.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.acube.jims.datalayer.models.Audit.AuditItem;
import com.acube.jims.datalayer.models.Audit.AuditLocation;
import com.acube.jims.datalayer.models.Audit.AuditReportItems;
import com.acube.jims.datalayer.models.Audit.AuditResults;
import com.acube.jims.datalayer.models.Audit.AuditSnapShot;
import com.acube.jims.datalayer.models.Audit.AuditSubCategory;
import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.models.Audit.Store;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.models.LocateProduct.LocateItem;

import java.util.HashSet;
import java.util.List;

@Dao
public interface AuditDownloadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<AuditResults> items);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(List<AuditItem> dataset);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubCategory(List<AuditSubCategory> datasetAuditSubCategory);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLocation(List<AuditLocation> datasetAuditLocation);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSnapShot(List<AuditSnapShot> datasetAuditSnapShot);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStore(List<Store> datasetstore);

    @Query("DELETE FROM LocateItem")
    void delete();

   /* @Query("UPDATE `ResponseItems` SET Status=:status WHERE serialNumber = :serialnumber")
    void update(String status, String serialnumber);*/

    @Query("SELECT * FROM `LocateItem` ")
    List<LocateItem> getAll();

    @Query("DELETE  FROM `LocateItem` ")
    void deleteall();

    @Query("SELECT DISTINCT  auditId ,toBeAuditedOn ,remark FROM auditsnapshot")
    LiveData<List<LocalAudit>> getDownloadedAudits();

    @Query("SELECT * FROM TemDataSerial")
    LiveData<List<TemDataSerial>> getTempSerials();


    @Query("SELECT * FROM AuditLocation where warehouseId=:storeID")
    LiveData<List<AuditLocation>> getDownloadedLocation(int storeID);

    @Query("SELECT * FROM Store")
    LiveData<List<Store>> getDownloadedStore();

    @Query("SELECT *  FROM AuditSubCategory")
    LiveData<List<AuditSubCategory>> getDownloadedCategory();

    @Query("SELECT *  FROM AuditItem where subCategoryId=:ID")
    LiveData<List<AuditItem>> getItems(int ID);

    @Query("SELECT * from InventoryAudit where auditID=:auditID")
    LiveData<List<AuditResults>> getDownloadedAuditss(String auditID);

    @Query("SELECT COUNT(*) FROM InventoryAudit WHERE auditID =:auditID AND status IN(:flagdownloaded,:flagfound) AND systemLocationID=:locationID")
    LiveData<Integer> getallcount(String auditID, int flagdownloaded, int flagfound, int locationID);

    /*@Query("SELECT COUNT(*) FROM AuditSnapShot WHERE auditID =:auditID AND status IN(:flagdownloaded,:flagfound) AND (subCategoryId=:catID OR '0'=:catID) AND (locationId=:locID OR '0'=:locID) AND  (itemId=:itemID OR '0'=:itemID) AND  (warehouseId=:warehouseId OR '0'=:warehouseId)")
    LiveData<Integer> getallcountbycat(String auditID, int flagdownloaded, int flagfound, int locID, int catID,int itemID,int warehouseId);
*/


    @Query("SELECT COUNT(*) FROM AuditSnapShot WHERE auditID =:auditID AND status IN(:flagdownloaded,:flagfound) AND (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (itemId=:itemID OR '0'='0') AND  (warehouseId=:StoreId OR '0'='0')")
    LiveData<Integer> getallcountbycat(String auditID, int flagdownloaded, int flagfound, int locID, int catID, int itemID, int StoreId);

    @Query("SELECT COUNT(*) FROM AuditSnapShot WHERE auditID =:auditID AND status=:flag AND (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (itemId=:itemID OR '0'='0') AND  (warehouseId=:warehouseId OR '0'='0')")
    LiveData<Integer> getcount(String auditID, int flag, int locID, int catID, int itemID, int warehouseId);

    @Query("SELECT COUNT(*) FROM AuditSnapShot WHERE auditID =:auditID")
    LiveData<Integer> getcountbycat(String auditID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTemporarySerialNos(List<TemDataSerial> data);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTemporarySerialNosAudit(TemDataSerial data);


    @Delete()
    void DeleteTemporarySerialNosAudit(TemDataSerial data);

    @Query("UPDATE  InventoryAudit set status='1', scanLocationID=:locID where auditID=:auditID AND   status <>'2' AND  serialNumber IN(Select SerialNo from TemDataSerial)AND systemLocationID=:locID")
    void checkauditscan(String auditID, int locID);

    @Query("UPDATE  InventoryAudit set status='1', scanLocationID=:locID where auditID=:auditID AND   status <>'2' AND  serialNumber IN(Select SerialNo from TemDataSerial) AND categoryID=:catID")
    void checkauditscanwithcat(String auditID, int locID, int catID);

    @Query("UPDATE  AuditSnapShot set status='1', scanLocationID=:locID where auditID=:auditID AND   status <>'2' AND  serialNumber IN(Select SerialNo from TemDataSerial) AND (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (itemId=:itemID OR '0'='0') AND  (warehouseId=:warehouseId OR '0'='0')")
    void updateAudit(String auditID, int locID, int catID, int itemID, int warehouseId);

    @Query("UPDATE  AuditSnapShot set status='1', scanLocationID=:locID where auditID=:auditID AND   status <>'2' AND  serialNumber =:serial AND (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (itemId=:itemID OR '0'='0') AND  (warehouseId=:warehouseId OR '0'='0')")
    void updateFindmissing(String auditID, int locID, int catID, int itemID, int warehouseId, String serial);

    @Query("UPDATE  AuditSnapShot set status='1', scanLocationID=:locID,remark=:remark where auditID=:auditID AND   status <>'2' AND  serialNumber IN(Select SerialNo from TemDataSerial)AND  (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (itemId=:itemID OR '0'='0') AND  (warehouseId=:warehouseId OR '0'='0')")
    void markasFound(String auditID, int locID, int catID, int itemID, int warehouseId, String remark);


    @RawQuery()
    boolean unkwowninsert(SimpleSQLiteQuery simpleSQLiteQuery);

    @RawQuery()
    Integer check(SimpleSQLiteQuery simpleSQLiteQuery);

    @Query("SELECT * FROM AuditSnapShot  WHERE auditID =:auditID AND status = :flagfound  AND (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (itemId=:itemID OR '0'='0') AND  (warehouseId=:StoreId OR '0'='0')")
    LiveData<List<AuditSnapShot>> getMissing(String auditID, int flagfound, int locID, int catID, int itemID, int StoreId);

    @Query("SELECT * FROM AuditSnapShot WHERE auditID =:auditID AND status IN(:flagdownloaded,:flagfound) AND (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (itemId=:itemID OR '0'='0') AND  (warehouseId=:StoreId OR '0'='0')")
    LiveData<List<AuditSnapShot>> gettotalstock(String auditID, int flagdownloaded, int flagfound, int locID, int catID, int itemID, int StoreId);


    @Query("SELECT COUNT(*) FROM InventoryAudit where auditId=:Id AND status='1' AND scanLocationID =:locationID AND scanLocationID !='0'AND systemLocationID<>scanLocationID")
    LiveData<Integer> checklocmismatchscanned(int Id, int locationID);


}
