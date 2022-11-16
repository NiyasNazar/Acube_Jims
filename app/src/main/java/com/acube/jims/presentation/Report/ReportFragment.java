package com.acube.jims.presentation.Report;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;

import com.acube.jims.BaseActivity;
import com.acube.jims.presentation.Audit.ViewModel.AuditViewModel;
import com.acube.jims.presentation.Report.View.ExtraFragment;
import com.acube.jims.presentation.Report.View.FoundFragment;
import com.acube.jims.presentation.Report.View.LocationMismatchFragment;
import com.acube.jims.presentation.Report.View.MissingFragment;
import com.acube.jims.presentation.Report.View.ReportFilterBottomSheetFragment;
import com.acube.jims.presentation.Report.View.reports.MissmatchApprovedFragment;
import com.acube.jims.presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.R;
import com.acube.jims.utils.FilterPreference;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ReportFragmentBinding;
import com.acube.jims.datalayer.models.Audit.Found;
import com.acube.jims.datalayer.models.Audit.LocationMismatch;
import com.acube.jims.datalayer.models.Audit.LocationMismatchApproved;
import com.acube.jims.datalayer.models.Audit.Missing;
import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.models.Audit.ResponseReport;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportFragment extends BaseActivity implements ReportFilterBottomSheetFragment.ApplyFilter {

    private ReportViewModel mViewModel;
    private AuditViewModel auditViewModel;

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    List<Found> datsetfound;
    List<Missing> datsetmissing;
    List<ResponseAudit> datasetaudits;
    List<LocationMismatch> datasetlocationmismatch;
    List<LocationMismatchApproved> datasetlocationapproved;
    String vaSubCatID, vaCatID = "", vaKaratID = "", vaColorID = "", vaWeight = "", vapriceMax = "", vaPriceMin = "", vagender = "";

    ReportFragmentBinding binding;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String dbfromdate, dbtodate, mAuditId,companyID,warehouseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.report_fragment);
        initToolBar(binding.toolbarApp.toolbar,"Audit Report",true);

        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        auditViewModel = new ViewModelProvider(this).get(AuditViewModel.class);
        mViewModel.init();
        auditViewModel.init();
        datasetaudits = new ArrayList<>();
        datsetfound = new ArrayList<>();
        datsetmissing = new ArrayList<>();
        datasetlocationapproved = new ArrayList<>();
        datasetlocationmismatch = new ArrayList<>();
         companyID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "CompanyID");
         warehouseID = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "warehouseId");
        //String auditID = LocalPreferences.retrieveStringPreferences(getActivity(), "auditID");



        binding.edAuditid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LocalPreferences.storeStringPreference(getApplicationContext(), "AuditID", String.valueOf(parent.getItemAtPosition(position)));
                mAuditId = String.valueOf(parent.getItemAtPosition(position));
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("auditID", mAuditId);
                jsonObject.addProperty("companyID", companyID);
                jsonObject.addProperty("warehouseID", warehouseID);
                jsonObject.addProperty("locationID", 0);
                jsonObject.addProperty("categoryID", 0);
                jsonObject.addProperty("subCategoryID", 0);
                jsonObject.addProperty("karatID", 0);
                jsonObject.addProperty("tabType", "");
                jsonObject.addProperty("serialNo", "");
                mViewModel.FetchReports(LocalPreferences.getToken(getApplicationContext()), jsonObject,getApplicationContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("auditID", "");
                jsonObject.addProperty("companyID", companyID);
                jsonObject.addProperty("warehouseID", warehouseID);
                jsonObject.addProperty("locationID", 0);
                jsonObject.addProperty("fromDate", "");
                jsonObject.addProperty("toDate", "");
            //    auditViewModel.Audit(LocalPreferences.getToken(getApplicationContext()), jsonObject);
                binding.auditlayt.setVisibility(View.VISIBLE);
            }
        });

        binding.startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportFragment.this, android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String dateString = String.format("%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year);
                                binding.startbutton.setText(dateString);
                                dbfromdate = String.format("%02d/%02d/%d", year, (monthOfYear + 1), dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        binding.tabfound.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border));
        binding.tabmissing.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
        binding.tabextra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
        binding.tablocationmismatch.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
        binding.tabLocationApproved.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));

        binding.enddatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportFragment.this, android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String dateString = String.format("%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year);
                                dbtodate = String.format("%02d/%02d/%d", year, (monthOfYear + 1), dayOfMonth);
                                binding.enddatebtn.setText(dateString);


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        binding.laytfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportFilterBottomSheetFragment bottomSheet = new ReportFilterBottomSheetFragment(ReportFragment.this::applyfilter);
                bottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");
            }
        });

        mViewModel.getLiveData().observe(this, new Observer<ResponseReport>() {
            @Override
            public void onChanged(ResponseReport responseReport) {
                if (responseReport != null) {

                    datsetfound = responseReport.getFound();
                    datsetmissing = responseReport.getMissing();
                    datasetlocationmismatch = responseReport.getLocationMismatches();
                    datasetlocationapproved = responseReport.getLocationMismatchApprovedList();
                    setList("datsetfound", datsetfound);
                    setList("datsetmissing", datsetmissing);
                    replace(FoundFragment.newInstance(datsetfound));
                    binding.laytfilter.setVisibility(View.VISIBLE);
                }
            }
        });

        final int sdk = android.os.Build.VERSION.SDK_INT;

        binding.tabfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tabfound.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border));
                binding.tabmissing.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tabextra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tablocationmismatch.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tabLocationApproved.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                replace(FoundFragment.newInstance(datsetfound));
            }
        });

        binding.tabmissing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tabLocationApproved.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tabfound.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tabmissing.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border));
                binding.tabextra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tablocationmismatch.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));


                replace(new MissingFragment(datsetmissing));
            }
        });
        binding.tabextra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tabLocationApproved.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));

                binding.tabfound.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tabmissing.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tabextra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border));
                binding.tablocationmismatch.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                //   replace(new FoundFragment());

            }
        });
        binding.tablocationmismatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tabLocationApproved.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));

                binding.tabfound.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tabmissing.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tabextra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tablocationmismatch.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border));
                replace(LocationMismatchFragment.newInstance(datasetlocationmismatch));
            }
        });
        binding.tabextra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tabfound.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tabLocationApproved.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tabmissing.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tablocationmismatch.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tabextra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border));
                replace(ExtraFragment.newInstance());
            }
        });

        binding.tabLocationApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tabfound.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tabmissing.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tablocationmismatch.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tabextra.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border_unselected));
                binding.tabLocationApproved.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_border));
                replace(MissmatchApprovedFragment.newInstance(datasetlocationapproved));

            }
        });



    }

    public void replace(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment).commit();
    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(getApplicationContext(), key, json);
    }

    @Override
    public void applyfilter() {
        vaSubCatID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "subcatid");
        vaColorID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "colorid");
        vaKaratID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "karatid");
        vaCatID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "catid");
        vaWeight = FilterPreference.retrieveStringPreferences(getApplicationContext(), "weightid");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auditID", mAuditId);
        jsonObject.addProperty("companyID", companyID);
        jsonObject.addProperty("warehouseID", warehouseID);
        jsonObject.addProperty("locationID", 0);
        jsonObject.addProperty("categoryID", vaCatID);
        jsonObject.addProperty("subCategoryID", vaSubCatID);
        jsonObject.addProperty("karatID", vaKaratID);
        jsonObject.addProperty("tabType", "Found");
        jsonObject.addProperty("serialNo", "");
        mViewModel.FetchReports(LocalPreferences.getToken(getApplicationContext()), jsonObject,getApplicationContext());
    }
}