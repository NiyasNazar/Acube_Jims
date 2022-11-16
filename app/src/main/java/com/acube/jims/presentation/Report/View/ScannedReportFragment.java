package com.acube.jims.presentation.Report.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.acube.jims.presentation.Audit.ViewModel.AuditUploadViewModel;
import com.acube.jims.presentation.Audit.ViewModel.AuditViewModel;
import com.acube.jims.presentation.Report.View.reports.MissmatchApprovedFragment;
import com.acube.jims.presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ScannedReportFragmentBinding;
import com.acube.jims.datalayer.models.Audit.Found;
import com.acube.jims.datalayer.models.Audit.LocationMismatch;
import com.acube.jims.datalayer.models.Audit.LocationMismatchApproved;
import com.acube.jims.datalayer.models.Audit.Missing;
import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.models.Audit.ResponseReport;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class ScannedReportFragment extends Fragment {

    private ReportViewModel mViewModel;
    private AuditViewModel auditViewModel;
    AuditUploadViewModel auditUploadViewModel;

    public static ScannedReportFragment newInstance() {
        return new ScannedReportFragment();
    }

    List<Found> datsetfound;
    List<Missing> datsetmissing;
    List<ResponseAudit> datasetaudits;
    List<LocationMismatch> datasetlocationmismatch;
    List<LocationMismatchApproved> datasetlocationapproved;

    ScannedReportFragmentBinding binding;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String dbfromdate, dbtodate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.scanned_report_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        auditViewModel = new ViewModelProvider(this).get(AuditViewModel.class);
        auditUploadViewModel = new ViewModelProvider(this).get(AuditUploadViewModel.class);
        auditUploadViewModel.init();
        mViewModel.init();
        auditViewModel.init();
        datasetaudits = new ArrayList<>();
        datsetfound = new ArrayList<>();
        datsetmissing = new ArrayList<>();
        datasetlocationapproved = new ArrayList<>();
        datasetlocationmismatch = new ArrayList<>();
        String companyID = LocalPreferences.retrieveStringPreferences(getActivity(), "CompanyID");
        String warehouseID = LocalPreferences.retrieveStringPreferences(getActivity(), "warehouseId");
        //String auditID = LocalPreferences.retrieveStringPreferences(getActivity(), "auditID");
        String auditID = LocalPreferences.retrieveStringPreferences(getActivity(), "auditID");
        String locationID = LocalPreferences.retrieveStringPreferences(getActivity(), "locationID");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auditID", auditID);
        jsonObject.addProperty("companyID", companyID);
        jsonObject.addProperty("warehouseID", warehouseID);
        jsonObject.addProperty("locationID", locationID);
        mViewModel.FetchReports(LocalPreferences.getToken(getContext()), jsonObject,requireActivity());


        binding.tabfound.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border));
        binding.tabmissing.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
        binding.tabextra.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
        binding.tablocationmismatch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
        binding.tabLocationApproved.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));


        final int sdk = android.os.Build.VERSION.SDK_INT;

        binding.tabfound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tabfound.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border));
                binding.tabmissing.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tabextra.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tablocationmismatch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tabLocationApproved.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));

                replace(FoundFragment.newInstance(datsetfound));
            }
        });

        binding.tabmissing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tabLocationApproved.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));

                binding.tabfound.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tabmissing.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border));
                binding.tabextra.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tablocationmismatch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));


                replace(new MissingFragment(datsetmissing));
            }
        });
        binding.tabextra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tabLocationApproved.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));

                binding.tabfound.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tabmissing.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tabextra.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border));
                binding.tablocationmismatch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                //   replace(new FoundFragment());

            }
        });
        binding.tablocationmismatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tabLocationApproved.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));

                binding.tabfound.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tabmissing.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tabextra.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tablocationmismatch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border));
                replace(LocationMismatchFragment.newInstance(datasetlocationmismatch));
            }
        });
        binding.tabextra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tabfound.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tabLocationApproved.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tabmissing.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tablocationmismatch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tabextra.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border));
                replace(ExtraFragment.newInstance());
            }
        });

        binding.tabLocationApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tabfound.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tabmissing.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tablocationmismatch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tabextra.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border_unselected));
                binding.tabLocationApproved.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.tab_border));
                replace(MissmatchApprovedFragment.newInstance(datasetlocationapproved));

            }
        });
        mViewModel.getLiveData().observe(getActivity(), new Observer<ResponseReport>() {
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
                }
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