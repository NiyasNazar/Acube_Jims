package com.acube.jims.datalayer.remote.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.acube.jims.datalayer.models.Audit.AuditItem;
import com.acube.jims.datalayer.models.Audit.AuditLocation;
import com.acube.jims.datalayer.models.Audit.AuditSnapShot;
import com.acube.jims.datalayer.models.Audit.AuditSubCategory;
import com.acube.jims.datalayer.models.Audit.Store;
import com.acube.jims.datalayer.models.ConsignmentLine;
import com.acube.jims.datalayer.models.Filter.Colorresult;
import com.acube.jims.datalayer.models.Filter.FilterStore;
import com.acube.jims.datalayer.models.Filter.Karatresult;
import com.acube.jims.datalayer.models.Filter.Weight;
import com.acube.jims.datalayer.models.Invoice.KaratPrice;
import com.acube.jims.datalayer.models.OfflineConsignment;
import com.acube.jims.datalayer.models.SelectionHolder;
import com.acube.jims.datalayer.models.report.ItemWiseReport;
import com.acube.jims.datalayer.remote.dbmodel.ItemRequestEntry;
import com.acube.jims.datalayer.remote.dbmodel.Smarttool;
import com.acube.jims.presentation.Quotation.adapter.DiscountItem;
import com.acube.jims.presentation.ScanItems.ResponseItems;
import com.acube.jims.datalayer.models.Audit.AuditResults;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.LocateProduct.LocateItem;
import com.acube.jims.datalayer.remote.dbmodel.CustomerHistory;


@Database(entities = {HomeData.class, ResponseItems.class, CustomerHistory.class, KaratPrice.class, ConsignmentLine.class,
        AuditResults.class, TemDataSerial.class, Smarttool.class, ItemWiseReport.class, FilterStore.class,
        DiscountItem.class, LocateItem.class, ItemRequestEntry.class, SelectionHolder.class, Karatresult.class, Colorresult.class, Weight.class,
        AuditItem.class, AuditSnapShot.class}, version = 9, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HomeMenuDao homeMenuDao();

    public abstract CompareItemsDao compareItemsDao();

    public abstract ScannedItemsDao scannedItemsDao();

    public abstract CustomerHistoryDao customerHistoryDao();

    public abstract DiscountItemsDao discountItemsDao();

    public abstract LocateItemsDao locateItemsDao();

    public abstract AuditDownloadDao auditDownloadDao();

    public abstract ItemRequestDao itemRequestDao();

}

