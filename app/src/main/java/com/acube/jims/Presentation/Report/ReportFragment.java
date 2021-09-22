package com.acube.jims.Presentation.Report;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.acube.jims.Presentation.Audit.AuditFragment;
import com.acube.jims.Presentation.Audit.ViewModel.AuditViewModel;
import com.acube.jims.Presentation.Favorites.View.FavoritesFragment;
import com.acube.jims.Presentation.HomePage.View.HomeFragment;
import com.acube.jims.Presentation.Report.View.FoundFragment;
import com.acube.jims.Presentation.Report.View.MissingFragment;
import com.acube.jims.Presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.ReportFragmentBinding;
import com.acube.jims.datalayer.models.Audit.Found;
import com.acube.jims.datalayer.models.Audit.Missing;
import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.models.Audit.ResponseReport;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportFragment extends Fragment {

    private ReportViewModel mViewModel;
    private AuditViewModel auditViewModel;

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    List<ResponseAudit> datasetaudits;
    ReportFragmentBinding binding;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String dbfromdate, dbtodate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.report_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        auditViewModel = new ViewModelProvider(this).get(AuditViewModel.class);
        mViewModel.init();
        auditViewModel.init();
        datasetaudits = new ArrayList<>();
        String companyID = LocalPreferences.retrieveStringPreferences(getActivity(), "CompanyID");
        String warehouseID = LocalPreferences.retrieveStringPreferences(getActivity(), "warehouseId");
        //String auditID = LocalPreferences.retrieveStringPreferences(getActivity(), "auditID");

        auditViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseAudit>>() {
            @Override
            public void onChanged(List<ResponseAudit> responseAudits) {
                if (responseAudits != null) {
                    datasetaudits = responseAudits;
                    ArrayAdapter<ResponseAudit> arrayAdapter = new ArrayAdapter<ResponseAudit>(getActivity(), android.R.layout.simple_spinner_item, responseAudits);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.edAuditid.setAdapter(arrayAdapter);
                }
            }
        });
        binding.edAuditid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LocalPreferences.storeStringPreference(getActivity(),"AuditID",String.valueOf(parent.getItemAtPosition(position)));
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("auditID", String.valueOf(parent.getItemAtPosition(position)));
                jsonObject.addProperty("companyID", companyID);
                jsonObject.addProperty("warehouseID", warehouseID);
                jsonObject.addProperty("locationID", 0);
                mViewModel.FetchInvoice(LocalPreferences.getToken(getContext()), jsonObject);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("auditID", "");
                jsonObject.addProperty("companyID", companyID);
                jsonObject.addProperty("warehouseID", warehouseID);
                jsonObject.addProperty("locationID", 0);
                jsonObject.addProperty("fromDate", dbfromdate);
                jsonObject.addProperty("toDate", dbtodate);
                auditViewModel.Audit(LocalPreferences.getToken(getActivity()), jsonObject);
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog,
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

        binding.enddatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String dateString = String.format("%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year);
                                dbtodate = String.format("%02d/%02d/%d", year, (monthOfYear + 1), dayOfMonth);
                                binding.enddatebtn.setText(dateString);
                                binding.edAuditid.setVisibility(View.VISIBLE);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        mViewModel.getLiveData().observe(getActivity(), new Observer<ResponseReport>() {
            @Override
            public void onChanged(ResponseReport responseReport) {
                if (responseReport != null) {
                    List<Found> datsetfound = responseReport.getFound();
                    List<Missing> datsetmissing = responseReport.getMissing();
                    setList("datsetfound", datsetfound);
                    setList("datsetmissing", datsetmissing);
                    replace(new FoundFragment());
                }
            }
        });


        binding.toolbar.tvFragname.setText("Report");
        binding.tabfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replace(new FoundFragment());
            }
        });

        binding.tabmissing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new MissingFragment());
            }
        });
        binding.tabextra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //   replace(new FoundFragment());

            }
        });

        return binding.getRoot();


    }

    public void replace(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment).commit();
    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(getActivity(), key, json);
    }
}