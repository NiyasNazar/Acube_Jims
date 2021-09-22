package com.acube.jims.Presentation.Report.View.reports;

import android.os.Bundle;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acube.jims.Presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.Presentation.Report.adapter.Foundadapter;
import com.acube.jims.Presentation.Report.adapter.Missingadapter;
import com.acube.jims.Presentation.Report.adapter.Reportadapter;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.ActivityReportViewbycatBinding;
import com.acube.jims.datalayer.models.Audit.Found;
import com.acube.jims.datalayer.models.Audit.Missing;
import com.acube.jims.datalayer.models.Audit.ResponseReport;
import com.google.gson.JsonObject;

import java.util.List;

public class MisiingReport extends AppCompatActivity implements Missingadapter.PassId {
    ActivityReportViewbycatBinding binding;
    private ReportViewModel mViewModel;
    Missingadapter reportadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_report_viewbycat);
        binding.recyvfound.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        mViewModel.init();
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
        binding.checkBoxselectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    reportadapter.selectAll();
                    binding.checkBoxselectall.setText("Unselect All");


                } else {
                    reportadapter.unselectall();
                    binding.checkBoxselectall.setText("Select All");
                }

            }
        });


        mViewModel.FetchInvoice(LocalPreferences.getToken(getApplicationContext()), jsonObject);
        mViewModel.getLiveData().observe(this, new Observer<ResponseReport>() {
            @Override
            public void onChanged(ResponseReport responseReport) {
                if (responseReport != null) {
                    List<Missing> datsetfound = responseReport.getMissing();
                    reportadapter = new Missingadapter(getApplicationContext(), datsetfound, MisiingReport.this::passid);
                    binding.recyvfound.setAdapter(reportadapter);
                }
            }
        });
    }

    @Override
    public void passid(String id,Integer locid) {

    }
}