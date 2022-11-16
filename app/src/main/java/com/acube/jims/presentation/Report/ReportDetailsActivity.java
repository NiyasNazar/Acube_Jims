package com.acube.jims.presentation.Report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityReportDetailsBinding;
import com.acube.jims.datalayer.api.ResponseAuditReport;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Audit.ResponseReportList;
import com.acube.jims.presentation.Audit.adapter.AuditSummaryReportAdapter;
import com.acube.jims.presentation.Report.View.ItemWiseReport;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.utils.OnSingleClickListener;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDetailsActivity extends BaseActivity {
    String auditID;
    int storeID,categoryId,subcatcategoryId;
    int systemLocationID;
    ActivityReportDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report_details);
        auditID = getIntent().getStringExtra("auditId");
        storeID = getIntent().getIntExtra("storeID", 0);
        categoryId=getIntent().getIntExtra("categoryId",0);
        subcatcategoryId=getIntent().getIntExtra("subcatcategoryId",0);

        systemLocationID = getIntent().getIntExtra("systemLocationID", 0);

        callReportApi();
        initToolBar(binding.toolbarApp.toolbar, "Stock Check Report", true);
        binding.cdvmissing.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(getApplicationContext(), ItemWiseReport.class)
                        .putExtra("status","missing")
                        .putExtra("categoryId",categoryId)
                        .putExtra("subCategoryId",subcatcategoryId)

                        .putExtra("auditId", auditID).putExtra("storeID", storeID)
                        .putExtra("systemLocationID",systemLocationID));

            }
        });
        binding.cdvfound.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(getApplicationContext(), ItemWiseReport.class)
                        .putExtra("status","found")
                        .putExtra("categoryId",categoryId)
                        .putExtra("subCategoryId",subcatcategoryId)

                        .putExtra("auditId", auditID).putExtra("storeID", storeID)
                        .putExtra("systemLocationID",systemLocationID));

            }
        });
        binding.cdvunknown.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(getApplicationContext(), ItemWiseReport.class)
                        .putExtra("status","extra")
                        .putExtra("categoryId",categoryId)
                        .putExtra("subCategoryId",subcatcategoryId)

                        .putExtra("auditId", auditID).putExtra("storeID", storeID)
                        .putExtra("systemLocationID",systemLocationID));

            }
        });
        binding.cdvlocationmismatch.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(getApplicationContext(), ItemWiseReport.class)
                        .putExtra("status","mismatch")
                        .putExtra("categoryId",categoryId)
                        .putExtra("subCategoryId",subcatcategoryId)

                        .putExtra("auditId", auditID).putExtra("storeID", storeID)
                        .putExtra("systemLocationID",systemLocationID));

            }
        });
        binding.cdvtotalstock.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(getApplicationContext(), ItemWiseReport.class)
                        .putExtra("status","")
                        .putExtra("categoryId",categoryId)
                                .putExtra("subCategoryId",subcatcategoryId)

                        .putExtra("auditId", auditID).putExtra("storeID", storeID)
                        .putExtra("systemLocationID",systemLocationID));

            }
        });

    }

    private void callReportApi() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auditID", auditID);
        jsonObject.addProperty("fromDate", "");
        jsonObject.addProperty("toDate", "");
        jsonObject.addProperty("warehouseId", storeID);
        jsonObject.addProperty("categoryId", categoryId);
        jsonObject.addProperty("subCategoryId", subcatcategoryId);
        RetrofitInstance.getApiService(getApplicationContext()).GetAuditSummary(LocalPreferences.getToken(getApplicationContext()), jsonObject).enqueue(new Callback<ResponseAuditReport>() {
            @Override
            public void onResponse(Call<ResponseAuditReport> call, Response<ResponseAuditReport> response) {
                if (response.body() != null) {
                    ResponseAuditReport responseAuditReport = response.body();
                    List<ResponseReportList> dataset = responseAuditReport.getResult();

                    for (int i = 0; i < dataset.size(); i++) {
                        String auditID = dataset.get(i).getAuditID();
                        String warehouseName = dataset.get(i).getWarehouseName();
                        String remark = dataset.get(i).getRemark();
                        String assignedZones = String.valueOf(dataset.get(i).getAssignedZones());
                        String total = String.valueOf(dataset.get(i).getTotalStock());
                        String missing = String.valueOf(dataset.get(i).getMissing());
                        String found = String.valueOf(dataset.get(i).getFound());
                        String extra = String.valueOf(dataset.get(i).getUnknown());
                        String mismatch = String.valueOf(dataset.get(i).getLocationMismatch());
                        binding.auditID.setText(auditID);
                        binding.remark.setText(remark);
                        binding.assignedZones.setText(assignedZones);
                        binding.warehouseName.setText(warehouseName);
                        binding.tvtotalstock.setText("" + total);
                        binding.tvlocationmismatch.setText("" + mismatch);
                        binding.tvmissing.setText("" + missing);
                        binding.tvfound.setText("" + found);
                        binding.tvunknown.setText("" + extra);


                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAuditReport> call, Throwable t) {

            }
        });
    }

}