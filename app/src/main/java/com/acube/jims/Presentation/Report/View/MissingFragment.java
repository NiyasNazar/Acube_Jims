package com.acube.jims.Presentation.Report.View;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.Presentation.Audit.ViewModel.AuditViewModel;
import com.acube.jims.Presentation.Report.adapter.Foundadapter;
import com.acube.jims.Presentation.Report.adapter.ItemHistoryadapter;
import com.acube.jims.Presentation.Report.adapter.Missingadapter;
import com.acube.jims.Presentation.Report.adapter.MissingadapterforFragment;
import com.acube.jims.Presentation.ScanHistory.ItemHistoryViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.FoundFragmentBinding;
import com.acube.jims.databinding.MissingFragmentBinding;
import com.acube.jims.datalayer.models.Audit.Found;
import com.acube.jims.datalayer.models.Audit.Missing;
import com.acube.jims.datalayer.models.ScanHistory.ResponseScanHistory;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MissingFragment extends Fragment implements MissingadapterforFragment.PassId {

    private ItemHistoryViewModel mViewModel;
    List<Missing> dataset;

    public static MissingFragment newInstance(List<Missing> dataset) {
        return new MissingFragment(dataset);
    }

    MissingFragmentBinding binding;
    MissingadapterforFragment adapter;

    public MissingFragment(List<Missing> dataset) {
        this.dataset = dataset;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(
                inflater, R.layout.missing_fragment, container, false);

        mViewModel = new ViewModelProvider(this).get(ItemHistoryViewModel.class);
        mViewModel.init();


        String companyID = LocalPreferences.retrieveStringPreferences(getActivity(), "CompanyID");
        String warehouseID = LocalPreferences.retrieveStringPreferences(getActivity(), "warehouseId");
        String auditID = LocalPreferences.retrieveStringPreferences(getActivity(), "auditID");
        mViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseScanHistory>>() {
            @Override
            public void onChanged(List<ResponseScanHistory> responseScanHistories) {
                if (responseScanHistories != null) {
                    showAddProductPopup(responseScanHistories);

                }
            }
        });

        binding.recyvfound.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MissingadapterforFragment(getActivity(), dataset, MissingFragment.this);
        binding.recyvfound.setAdapter(adapter);
        binding.tvTotaldata.setText("Total Items Missing : " + dataset.size());


        return binding.getRoot();
    }


    @Override
    public void passid(String id, Integer locid) {
        String companyID = LocalPreferences.retrieveStringPreferences(getActivity(), "CompanyID");
        String warehouseID = LocalPreferences.retrieveStringPreferences(getActivity(), "warehouseId");
        String auditID = LocalPreferences.retrieveStringPreferences(getActivity(), "auditID");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auditID", "");
        jsonObject.addProperty("companyID", companyID);
        jsonObject.addProperty("warehouseID", warehouseID);
        jsonObject.addProperty("locationID", locid);
        jsonObject.addProperty("serialNo", id);
        mViewModel.ItemHistory(LocalPreferences.getToken(getActivity()), jsonObject);


    }

    private void showAddProductPopup(List<ResponseScanHistory> responseScanHistories) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
        RecyclerView recyclerView = alertLayout.findViewById(R.id.recyvitemhistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ItemHistoryadapter(getActivity(), responseScanHistories));

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();

    }
}