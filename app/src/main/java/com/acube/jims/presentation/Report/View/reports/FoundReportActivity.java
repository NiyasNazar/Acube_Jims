package com.acube.jims.presentation.Report.View.reports;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.acube.jims.BaseActivity;
import com.acube.jims.datalayer.models.Audit.AuditReportItems;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ActivityFoundreportBinding;
import com.acube.jims.presentation.Report.adapter.Missingadapter;

import java.util.List;

public class FoundReportActivity extends BaseActivity {
    ActivityFoundreportBinding binding;
    private ReportViewModel mViewModel;
    Missingadapter  reportadapter;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_foundreport);
        binding.recyvfound.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        initToolBar(binding.toolbarApp.toolbar, "Found", true);
        flag = getIntent().getIntExtra("flag", -1);
        binding.tvNotfound.setVisibility(View.GONE);
        if (flag==1){
            initToolBar(binding.toolbarApp.toolbar, "Found", true);

        }else{
            initToolBar(binding.toolbarApp.toolbar, "Unknown", true);

        }

        showProgressDialog();
        String locationid = getIntent().getStringExtra("locationid");
        String companyID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "CompanyID");
        String warehouseID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "warehouseId");
        String AuditID = getIntent().getStringExtra("auditID");


        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().auditDownloadDao().getMissing(AuditID, flag, 0).observe(this, new Observer<List<AuditReportItems>>() {
            @Override
            public void onChanged(List<AuditReportItems> responseReport) {
                hideProgressDialog();
                if (responseReport != null) {
                    binding.tvTotaldata.setText("Total Items : " + responseReport.size());

                    reportadapter = new Missingadapter(getApplicationContext(), responseReport,false);
                    binding.recyvfound.setAdapter(reportadapter);
                    if (reportadapter.getItemCount() == 0) {
                        binding.tvNotfound.setVisibility(View.VISIBLE);
                    } else {

                        binding.tvNotfound.setVisibility(View.GONE);
                    }
                } else {

                }
            }
        });
    }
}