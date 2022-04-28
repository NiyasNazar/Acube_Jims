package com.acube.jims.datalayer.remote.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

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

    @Query("SELECT DISTINCT  auditId ,toBeAuditedOn ,remark,systemLocationID,systemLocationName FROM InventoryAudit")
    LiveData<List<LocalAudit>> getDownloadedAudits();

    @Query("SELECT * from InventoryAudit where auditID=:auditID")
    LiveData<List<AuditResults>> getDownloadedAudits(String auditID);

    @Query("SELECT COUNT(*) FROM InventoryAudit WHERE auditID =:auditID AND status IN(:flagdownloaded,:flagfound)")
    LiveData<Integer> getallcount(String auditID, int flagdownloaded, int flagfound);

    @Query("SELECT COUNT(*) FROM InventoryAudit WHERE auditID =:auditID AND status=:flag")
    LiveData<Integer> getcount(String auditID,int flag);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTemporarySerialNos(List<TemDataSerial> data);

    @Query("UPDATE  InventoryAudit set status='1' where auditID=:auditID AND  status <>'2' AND  serialNumber IN(Select serialNumber from TemDataSerial)")
    void checkauditscan(String auditID);

    @RawQuery()
    boolean unkwowninsert(SimpleSQLiteQuery simpleSQLiteQuery);
}
