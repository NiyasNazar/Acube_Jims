package com.acube.jims.datalayer.remote.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.acube.jims.datalayer.models.Invoice.KaratPrice;
import com.acube.jims.datalayer.remote.dbmodel.ItemRequestEntry;
import com.acube.jims.presentation.Quotation.adapter.DiscountItem;
import com.acube.jims.presentation.ScanItems.ResponseItems;
import com.acube.jims.datalayer.models.Audit.AuditResults;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.LocateProduct.LocateItem;
import com.acube.jims.datalayer.remote.dbmodel.CustomerHistory;


@Database(entities = {HomeData.class, ResponseItems.class, CustomerHistory.class, KaratPrice.class,
        AuditResults.class, TemDataSerial.class,
        DiscountItem.class, LocateItem.class, ItemRequestEntry.class}, version = 6,exportSchema = false)
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

