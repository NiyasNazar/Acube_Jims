package com.acube.jims.Presentation.Report.View.reports;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acube.jims.BaseActivity;
import com.acube.jims.Presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.Presentation.Report.adapter.LocationMismatchAdapter;
import com.acube.jims.Presentation.Report.adapter.Reportadapter;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.ActivityReportLocationmismatchBinding;
import com.acube.jims.databinding.ActivityReportViewbycatBinding;
import com.acube.jims.datalayer.models.Audit.Found;
import com.acube.jims.datalayer.models.Audit.LocationMismatch;
import com.acube.jims.datalayer.models.Audit.ResponseReport;
import com.google.gson.JsonObject;

import java.util.List;

public class LocationMistmatchReport extends BaseActivity {
    ActivityReportLocationmismatchBinding binding;
    private ReportViewModel mViewModel;
    LocationMismatchAdapter locationMismatchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_report_locationmismatch);
        binding.recyvfound.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        mViewModel.init();
        binding.backlayout.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        String locationid = getIntent().getStringExtra("locationid");
        String companyID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "CompanyID");
        String warehouseID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "warehouseId");
        String AuditID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "auditID");
        binding.backlayout.tvFragname.setText("Location Mismatch");
        showProgressDialog();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auditID", AuditID);
        jsonObject.addProperty("companyID", companyID);
        jsonObject.addProperty("warehouseID", warehouseID);
        jsonObject.addProperty("locationID", Integer.parseInt(locationid));




        mViewModel.FetchReports(LocalPreferences.getToken(getApplicationContext()), jsonObject);
        mViewModel.getLiveData().observe(this, new Observer<ResponseReport>() {
            @Override
            public void onChanged(ResponseReport responseReport) {
                hideProgressDialog();
                if (responseReport != null) {

                    List<LocationMismatch> datsetfound = responseReport.getLocationMismatches();
                    binding.tvTotaldata.setText("Total Items : " + datsetfound.size());
                    locationMismatchAdapter = new LocationMismatchAdapter(getApplicationContext(), datsetfound);
                    binding.recyvfound.setAdapter(locationMismatchAdapter);
                    if (locationMismatchAdapter.getItemCount() == 0) {
                        binding.tvNotfound.setVisibility(View.VISIBLE);
                    } else {

                        binding.tvNotfound.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}