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
import com.acube.jims.datalayer.models.SelectionHolder;
import com.acube.jims.datalayer.models.report.ItemWiseReport;
import com.acube.jims.presentation.Report.adapter.GraphReportCount;
import com.rscja.team.qcom.deviceapi.S;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

@Dao
public interface AuditDownloadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<AuditResults> items);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(List<AuditItem> dataset);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSnapShot(List<AuditSnapShot> datasetAuditSnapShot);


    @Query("DELETE FROM LocateItem")
    void delete();

    @Query("DELETE FROM AuditSnapShot where auditID=:auidtID")
    void closeAudit(String auidtID);

   /* @Query("UPDATE `ResponseItems` SET Status=:status WHERE serialNumber = :serialnumber")
    void update(String status, String serialnumber);*/

    @Query("SELECT * FROM `LocateItem` ")
    List<LocateItem> getAll();

    @Query("SELECT * FROM `AuditSnapShot`  where auditID=:auditID AND scannedBy  is not NULL")
    LiveData<List<AuditSnapShot>> getallauditforupload(String auditID);

    @Query("DELETE  FROM `LocateItem` ")
    void deleteall();

    @Query("DELETE  FROM `AuditSnapShot` where auditID=:auditID ")
    void deleteaudit(String auditID);


    @Query("DELETE  FROM `TemDataSerial` ")
    void deleteTemp();

    @Query("SELECT DISTINCT  auditId ,toBeAuditedOn ,remark FROM auditsnapshot where remark is not NULL")
    LiveData<List<LocalAudit>> getDownloadedAudits();

    @Query("SELECT DISTINCT  auditID FROM auditsnapshot where ('NA'=:AuditID OR auditID=:AuditID) ")
    LiveData<List<LocalAudit>> getGraphReportCount(String AuditID);

    @Query("SELECT * FROM SelectionHolder")
    LiveData<List<SelectionHolder>> getTempSerials();


    @Query("SELECT DISTINCT locationId,locationName FROM AuditSnapShot where auditID =:auditID AND locationName is not null ")
    LiveData<List<AuditLocation>> getDownloadedLocation(String auditID);

    @Query("SELECT DISTINCT warehouseName FROM AuditSnapShot where auditID=:ID AND warehouseId is not null")
    LiveData<List<Store>> getDownloadedStore(String ID);

    @Query("SELECT * FROM ItemWiseReport where isSelected=1")
    LiveData<List<ItemWiseReport>> getItemwiseReport();


    @Query("SELECT DISTINCT subCategoryName,subCategoryId  FROM AuditSnapShot where auditID=:ID AND categoryId=:catID AND  categoryId IS NOT NULL")
    LiveData<List<AuditSubCategory>> getDownloadedSubcatCategory(String ID, int catID);

    @Query("SELECT DISTINCT categoryId,categoryName  FROM AuditSnapShot where auditID=:ID AND categoryId IS NOT NULL")
    LiveData<List<AuditCate>> getCategory(String ID);

    @Query("SELECT * from InventoryAudit where auditID=:auditID")
    LiveData<List<AuditResults>> getDownloadedAuditss(String auditID);

    @Query("SELECT COUNT(*) FROM InventoryAudit WHERE auditID =:auditID AND status IN(:flagdownloaded,:flagfound) AND systemLocationID=:locationID")
    LiveData<Integer> getallcount(String auditID, int flagdownloaded, int flagfound, int locationID);

    /*@Query("SELECT COUNT(*) FROM AuditSnapShot WHERE auditID =:auditID AND status IN(:flagdownloaded,:flagfound) AND (subCategoryId=:catID OR '0'=:catID) AND (locationId=:locID OR '0'=:locID) AND  (itemId=:itemID OR '0'=:itemID) AND  (warehouseId=:warehouseId OR '0'=:warehouseId)")
    LiveData<Integer> getallcountbycat(String auditID, int flagdownloaded, int flagfound, int locID, int catID,int itemID,int warehouseId);
*/
    @Query("SELECT COUNT(*) FROM AuditSnapShot WHERE auditID =:auditID AND status IN(:flagdownloaded,:flagfound) AND (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (itemId=:itemID OR '0'='0') AND  (warehouseId=:StoreId OR '0'='0')")
    Integer totalcount(String auditID, int flagdownloaded, int flagfound, int locID, int catID, int itemID, int StoreId);

    @Query("SELECT COUNT(*) FROM AuditSnapShot WHERE auditID =:auditID AND status=:flag AND (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (itemId=:itemID OR '0'='0') AND  (warehouseId=:warehouseId OR '0'='0')")
    Integer getcountgraph(String auditID, int flag, int locID, int catID, int itemID, int warehouseId);


    @Query("SELECT COUNT(*) FROM AuditSnapShot WHERE auditID =:auditID AND status IN(:flagdownloaded,:flagfound) AND(:catID=0 OR categoryId=:catID) AND (:locID=0 OR locationId=:locID ) AND (:SubcatID=0 OR subCategoryId=:SubcatID) AND  (:StoreId =0 OR warehouseId=:StoreId)")
    LiveData<Integer> getallcountbycat(String auditID, int flagdownloaded, int flagfound, int locID, int catID, int SubcatID, int StoreId);

    @Query("SELECT SUM(weight)FROM AuditSnapShot WHERE auditID =:auditID AND status IN(:flagdownloaded,:flagfound) AND (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (itemId=:itemID OR '0'='0') AND  (warehouseId=:StoreId OR '0'='0')")
    LiveData<Double> getSum(String auditID, int flagdownloaded, int flagfound, int locID, int catID, int itemID, int StoreId);

    @Query("SELECT IFNULL (SUM(weight),0) FROM AuditSnapShot WHERE auditID =:auditID AND status=:flag AND (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (itemId=:itemID OR '0'='0') AND  (warehouseId=:warehouseId OR '0'='0')")
    LiveData<Double> getSum(String auditID, int flag, int locID, int catID, int itemID, int warehouseId);

    @Query("SELECT IFNULL (SUM(weight),0) FROM AuditSnapShot WHERE auditID =:auditID AND status IN(:flagdownloaded,:flagfound)  AND (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (subCategoryId=:subcatcategoryId OR '0'='0') AND  (warehouseId=:warehouseId OR '0'='0')")
    LiveData<Double> gettotalAgainistaudit(String auditID, int locID, int catID, int flagdownloaded, int flagfound, int subcatcategoryId, int warehouseId);


    @Query("SELECT COUNT(*) FROM AuditSnapShot WHERE auditID =:auditID AND status=:flag AND (categoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (subCategoryId=:subcatcategoryId OR '0'='0') AND  (warehouseId=:warehouseId OR '0'='0')")
    LiveData<Integer> getcount(String auditID, int flag, int locID, int catID, int subcatcategoryId, int warehouseId);

    @Query("SELECT COUNT(*) FROM AuditSnapShot WHERE auditID =:auditID")
    LiveData<Integer> getcountbycat(String auditID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTemporarySerialNos(List<TemDataSerial> data);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTemporarySerialNosAudit(TemDataSerial data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMissingselection(List<SelectionHolder> data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMissingselection(SelectionHolder data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItemWiseReport(ItemWiseReport data);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItemWiseReport(List<ItemWiseReport> data);

    @Query("DELETE  FROM `SelectionHolder` ")
    void deleteSelectionHolder();

    @Query("DELETE  FROM `ItemWiseReport` ")
    void deleteItemWiseReport();


    @Delete()
    void DeleteTemporarySerialSelectionHolder(SelectionHolder data);

    @Delete()
    void DeleteTemporarySerialSelectionHolder(ItemWiseReport data);

    @Query("Delete from ItemWiseReport")
    int deleteallitemwise();

    @Delete()
    void DeleteTemporarySerialNosAudit(TemDataSerial data);

    @Query("UPDATE  InventoryAudit set status='1', scanLocationID=:locID where auditID=:auditID AND   status <>'2' AND  serialNumber IN(Select SerialNo from TemDataSerial)AND systemLocationID=:locID")
    void checkauditscan(String auditID, int locID);

    @Query("UPDATE  InventoryAudit set status='1', scanLocationID=:locID where auditID=:auditID AND   status <>'2' AND  serialNumber IN(Select SerialNo from TemDataSerial) AND categoryID=:catID")
    void checkauditscanwithcat(String auditID, int locID, int catID);

/*
    @Query("UPDATE  AuditSnapShot set status='1', scanLocationID=:locID ,scanneddate=:date,scannedBy=:scanneduser where auditID=:auditID AND   status <>'2' AND  serialNumber IN(Select SerialNo from TemDataSerial) AND (:catID=0 OR categoryId=:catID) AND (:locID=0 OR locationId=:locID ) AND (:SubcatID=0 OR subCategoryId=:SubcatID) AND  (:warehouseId =0 OR warehouseId=:warehouseId)")
*/
    /*
    @Query("UPDATE  AuditSnapShot set status='1', scanLocationID=:locID ,scanneddate=:date,scannedBy=:scanneduser where auditID=:auditID AND   status <>'2' AND  serialNumber IN(Select SerialNo from TemDataSerial) AND (:catID=0 OR categoryId=:catID) AND (:locID=0 OR locationId=:locID ) AND (:SubcatID=0 OR subCategoryId=:SubcatID) AND  (:warehouseId =0 OR warehouseId=:warehouseId)")
*/

    @Query("UPDATE  AuditSnapShot set status='1', scanLocationID=:locID ,scanneddate=:date,scannedBy=:scanneduser where auditID=:auditID AND   status <>'2' AND  serialNumber IN(Select SerialNo from TemDataSerial)")
    int updateAudit(String auditID, int locID,String date, String scanneduser);

    @Query("UPDATE  AuditSnapShot set status='1', scanLocationID=:locID ,scanneddate=:date,scannedBy=:scanneduser where auditID=:auditID AND   status <>'2' AND  serialNumber IN(Select serialNumber from SelectionHolder where serialNumber=:serial) AND (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (itemId=:itemID OR '0'='0') AND  (warehouseId=:warehouseId OR '0'='0')")
    int updateFindmissing(String auditID, int locID, int catID, int itemID, int warehouseId, String serial, String date, String scanneduser);

    @Query("UPDATE  SelectionHolder set status='1' where serialNumber=:serial")
    int updateSelectionHolder(String serial);


    @Query("UPDATE  AuditSnapShot set status='1', scanLocationID=:locID,Remarks=:remark,scanneddate=:date,scannedBy=:scanneduser where auditID=:auditID AND   status <>'2' AND  serialNumber IN(Select serialNumber from SelectionHolder)AND  (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (itemId=:itemID OR '0'='0') AND  (warehouseId=:warehouseId OR '0'='0')")
    void markasFound(String auditID, int locID, int catID, int itemID, int warehouseId, String remark, String date, String scanneduser);


    @RawQuery()
    boolean unkwowninsert(SimpleSQLiteQuery simpleSQLiteQuery);

    @RawQuery()
    Integer check(SimpleSQLiteQuery simpleSQLiteQuery);

    @Query("SELECT * FROM AuditSnapShot  WHERE auditID =:auditID AND status = :flagfound  AND (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (itemId=:itemID OR '0'='0') AND  (warehouseId=:StoreId OR '0'='0')")
    LiveData<List<SelectionHolder>> getMissing(String auditID, int flagfound, int locID, int catID, int itemID, int StoreId);

    @Query("SELECT * FROM AuditSnapShot WHERE auditID =:auditID AND status IN(:flagdownloaded,:flagfound) AND (subCategoryId=:catID OR '0'='0') AND (locationId=:locID OR '0'='0') AND  (itemId=:itemID OR '0'='0') AND  (warehouseId=:StoreId OR '0'='0')")
    LiveData<List<SelectionHolder>> gettotalstock(String auditID, int flagdownloaded, int flagfound, int locID, int catID, int itemID, int StoreId);


    @Query("SELECT COUNT(*) FROM InventoryAudit where auditId=:Id AND status='1' AND scanLocationID =:locationID AND scanLocationID !='0'AND systemLocationID<>scanLocationID")
    LiveData<Integer> checklocmismatchscanned(int Id, int locationID);

    @Query("SELECT * FROM AuditSnapShot where auditId=:id  ORDER BY scanneddate DESC")
    LiveData<List<AuditSnapShot>> getDownloadedAuditsbyAuditId(String id);

    @Query("UPDATE  ItemWiseReport set isSelected=:flag")
    void updateselection(int flag);

    @Query("UPDATE  ItemWiseReport set isSelected=:flag where serialNumber=:serial")
    void updateselection(int flag, String serial);
}
