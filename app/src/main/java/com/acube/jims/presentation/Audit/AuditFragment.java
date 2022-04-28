package com.acube.jims.presentation.Audit;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.acube.jims.BaseFragment;
import com.acube.jims.presentation.Audit.ViewModel.AuditLocationDetailsModel;
import com.acube.jims.presentation.Audit.ViewModel.AuditLocationViewModel;
import com.acube.jims.presentation.Audit.ViewModel.AuditUploadViewModel;
import com.acube.jims.presentation.Audit.ViewModel.AuditViewModel;
import com.acube.jims.presentation.Audit.adapter.AuditAdapter;
import com.acube.jims.presentation.Audit.adapter.AuditLocationadapter;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.AuditFragmentBinding;
import com.acube.jims.datalayer.models.Audit.AuditResults;
import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.datalayer.remote.db.LocalAudit;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AuditFragment extends BaseFragment implements AuditLocationadapter.PassId,AuditAdapter.FragmentTransition {

    private AuditViewModel mViewModel;
    int totalscanneditems;

    public static AuditFragment newInstance() {
        return new AuditFragment();
    }

    AuditFragmentBinding binding;
    AuditLocationViewModel auditLocationViewModel;

    AuditLocationDetailsModel auditLocationViewModedetails;
    String auditid = "";
    AuditUploadViewModel auditUploadViewModel;
    int found, missing, locationmismatch, totalstock;
    String locationid = "0";
    List<ResponseAudit> dataset;
    int selectedpos = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.audit_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(AuditViewModel.class);
        auditLocationViewModel = new ViewModelProvider(this).get(AuditLocationViewModel.class);
        auditLocationViewModedetails = new ViewModelProvider(this).get(AuditLocationDetailsModel.class);
        auditUploadViewModel = new ViewModelProvider(this).get(AuditUploadViewModel.class);
        auditLocationViewModel.init();
        auditUploadViewModel.init();
        mViewModel.init();
        auditLocationViewModedetails.init();

        String companyID = LocalPreferences.retrieveStringPreferences(getActivity(), "CompanyID");
        String warehouseID = LocalPreferences.retrieveStringPreferences(getActivity(), "warehouseId");
        String Employeename = LocalPreferences.retrieveStringPreferences(getActivity(), "EmployeeName");
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("warehouseID", warehouseID);
        binding.recyvaduditlocations.setLayoutManager(new LinearLayoutManager(getActivity()));
        mViewModel.Audit(LocalPreferences.getToken(getActivity()), jsonObject);
        mViewModel.getLiveData().observe(requireActivity(), new Observer<List<ResponseAudit>>() {
            @Override
            public void onChanged(List<ResponseAudit> responseAudits) {
                if (responseAudits != null) {
                    dataset = new ArrayList<>();

                    dataset = responseAudits;
                    ArrayAdapter<ResponseAudit> arrayAdapter = new ArrayAdapter<ResponseAudit>(getActivity(), android.R.layout.simple_spinner_item, responseAudits);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.editaudit.setAdapter(arrayAdapter);
                }
            }
        });

        DatabaseClient.getInstance(requireActivity()).getAppDatabase().auditDownloadDao().getDownloadedAudits().observe(getActivity(), new Observer<List<LocalAudit>>() {
            @Override
            public void onChanged(List<LocalAudit> localAudits) {
                if (localAudits!=null){
                    binding.recyvaduditlocations.setAdapter(new AuditAdapter(getActivity(),localAudits,AuditFragment.this));

                }
            }
        });
        binding.editaudit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedpos = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AuditResults> auditResultsList = dataset.get(selectedpos).getAuditResultsList();
                AuditResults inventoryAudit;
                List<AuditResults> filteredaudit = new ArrayList<>();

                for (int i = 0; i < auditResultsList.size(); i++) {
                    inventoryAudit = new AuditResults();
                    inventoryAudit.setID(auditResultsList.get(i).getAuditID() + auditResultsList.get(i).getSerialNumber());
                    inventoryAudit.setAuditID(auditResultsList.get(i).getAuditID());
                    inventoryAudit.setSerialNumber(auditResultsList.get(i).getSerialNumber());
                    inventoryAudit.setToBeAuditedOn(auditResultsList.get(i).getToBeAuditedOn());
                    inventoryAudit.setSystemLocationID(auditResultsList.get(i).getSystemLocationID());
                    inventoryAudit.setSystemLocationName(auditResultsList.get(i).getSystemLocationName());
                    inventoryAudit.setRemark(auditResultsList.get(i).getRemark());
                    inventoryAudit.setStatus(0);
                    filteredaudit.add(inventoryAudit);
                }
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {

                        DatabaseClient.getInstance(requireActivity()).getAppDatabase().auditDownloadDao().insert(filteredaudit);
                    }
                });


                hideProgressDialog();
            }
        });


        return binding.getRoot();


    }


    @Override
    public void passid(String id) {

    }

    @Override
    public void replaceFragment(String menuname) {

    }

    @Override
    public void passlist(List<Integer> dataset) {

    }

    @Override
    public void scanaction(String auditID, String toBeAuditedOn, String remark) {
        startActivity(new Intent(getActivity(),AuditScanActivity.class).putExtra("auditID",auditID));

    }
}