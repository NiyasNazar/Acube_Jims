package com.acube.jims.presentation.Report.View.reports;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import com.acube.jims.BaseActivity;
import com.acube.jims.datalayer.models.Audit.AuditReportItems;
import com.acube.jims.datalayer.models.Audit.AuditSnapShot;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ActivityReportLocationmismatchBinding;
import com.acube.jims.presentation.Report.adapter.Missingadapter;

import java.util.List;

public class TotalStockReport extends BaseActivity {
    ActivityReportLocationmismatchBinding binding;
    Missingadapter reportadapter;
    String AuditID;
    int systemLocationID, storeID, categoryId, itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_report_locationmismatch);
        initToolBar(binding.toolbarApp.toolbar, "Total Stock", true);
        binding.recyvfound.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));


        AuditID = getIntent().getStringExtra("auditID");
        systemLocationID = getIntent().getIntExtra("systemLocationID", 0);
        storeID = getIntent().getIntExtra("storeID", 0);
        categoryId = getIntent().getIntExtra("categoryId", 0);
        itemID = getIntent().getIntExtra("itemID", 0);

        showProgressDialog();

        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().gettotalstock(AuditID, 0, 1, systemLocationID, categoryId, itemID, storeID).observe(this, new Observer<List<AuditSnapShot>>() {
            @Override
            public void onChanged(List<AuditSnapShot> auditReportItems) {
                hideProgressDialog();
                if (auditReportItems != null) {
                    binding.tvTotaldata.setText("Total Items : " + auditReportItems.size());
                    reportadapter = new Missingadapter(getApplicationContext(), auditReportItems, false);
                    binding.recyvfound.setAdapter(reportadapter);
                    if (reportadapter.getItemCount() == 0) {
                        binding.tvNotfound.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvNotfound.setVisibility(View.GONE);
                    }
                }
            }
        });


    }


}