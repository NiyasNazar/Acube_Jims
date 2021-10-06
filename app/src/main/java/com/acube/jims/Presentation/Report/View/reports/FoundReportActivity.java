package com.acube.jims.Presentation.Report.View.reports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.acube.jims.BaseActivity;
import com.acube.jims.Presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.Presentation.Report.adapter.Foundadapter;
import com.acube.jims.Presentation.Report.adapter.Reportadapter;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.ActivityFoundreportBinding;
import com.acube.jims.databinding.ActivityReportViewbycatBinding;
import com.acube.jims.datalayer.models.Audit.Found;
import com.acube.jims.datalayer.models.Audit.ResponseReport;
import com.google.gson.JsonObject;

import java.util.List;

public class FoundReportActivity extends BaseActivity {
    ActivityFoundreportBinding binding;
    private ReportViewModel mViewModel;
    Foundadapter reportadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_foundreport);
        binding.recyvfound.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        mViewModel.init();
        binding.tvNotfound.setVisibility(View.GONE);
        showProgressDialog();
        String locationid = getIntent().getStringExtra("locationid");
        String companyID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "CompanyID");
        String warehouseID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "warehouseId");
        String AuditID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "auditID");
        binding.backlayout.tvFragname.setText("Found");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auditID", AuditID);
        jsonObject.addProperty("companyID", companyID);
        jsonObject.addProperty("warehouseID", warehouseID);
        jsonObject.addProperty("locationID", Integer.parseInt(locationid));


        mViewModel.FetchInvoice(LocalPreferences.getToken(getApplicationContext()), jsonObject);
        mViewModel.getLiveData().observe(this, new Observer<ResponseReport>() {
            @Override
            public void onChanged(ResponseReport responseReport) {
                hideProgressDialog();
                if (responseReport != null) {
                    List<Found> datsetfound = responseReport.getFound();
                    binding.tvTotaldata.setText("Total Items : " + datsetfound.size());

                    reportadapter = new Foundadapter(getApplicationContext(), datsetfound);
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