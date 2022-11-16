package com.acube.jims.presentation.Report;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acube.jims.BaseFragment;
import com.acube.jims.R;
import com.acube.jims.databinding.FragmentCategoryReportBinding;
import com.acube.jims.databinding.FragmentStoreReportBinding;
import com.acube.jims.datalayer.api.ResponseAuditReport;
import com.acube.jims.datalayer.api.ResponseLiveCategory;
import com.acube.jims.datalayer.api.ResponseLiveSubCategory;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Audit.ResponseLiveStore;
import com.acube.jims.datalayer.models.Audit.ResponseReportList;
import com.acube.jims.presentation.Audit.adapter.AuditSummaryReportAdapter;
import com.acube.jims.presentation.Report.adapter.GraphAuditItemReportAdapter;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.utils.OnSingleClickListener;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryReportFragment extends BaseFragment implements AuditSummaryReportAdapter.FragmentTransition {
    String AuidtFilter = "NA";
    int systemLocationID = 0;
    int categoryId = 0;
    int storeID = 0;
    int itemID = 0;
    GraphAuditItemReportAdapter adapter;
    List<ResponseLiveCategory> datasetcat;
    List<ResponseLiveSubCategory> subCategoryList;
    private int year, month, day;
    private DatePicker datePicker;
    Calendar calendar;
    String fromdate, todate;
    FragmentCategoryReportBinding binding;
    int subcatcategoryId = 0;
    public CategoryReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_category_report, container, false);


        getCategory();
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        binding.recyvauditsummary.setLayoutManager(new LinearLayoutManager(requireActivity()));

        DatePickerDialog datePickerDialogfrom = new DatePickerDialog(requireActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        binding.edfromdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        fromdate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                    }
                }, year, month, day);

        DatePickerDialog datePickerDialogto = new DatePickerDialog(requireActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        binding.edtodate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        todate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }
                }, year, month, day);

        binding.edfromdate.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                datePickerDialogfrom.show();
            }
        });
        binding.edtodate.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                datePickerDialogto.show();
            }
        });
        binding.btngo.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                callReportApi();
            }
        });
        binding.spincategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = datasetcat.get(position).getId();
                getSubcategory(categoryId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinsubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subcatcategoryId = subCategoryList.get(position).getId();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return binding.getRoot();
    }

    private void getSubcategory(int categoryId) {
        subCategoryList = new ArrayList<>();
        RetrofitInstance.getApiService(requireActivity()).FetchLiveSubCategory(LocalPreferences.getToken(getActivity()), categoryId).enqueue(new Callback<List<ResponseLiveSubCategory>>() {
            @Override
            public void onResponse(Call<List<ResponseLiveSubCategory>> call, Response<List<ResponseLiveSubCategory>> response) {
                if (response.body() != null && response.code() == 200) {
                    List<ResponseLiveSubCategory> dataset = response.body();
                    subCategoryList = dataset;
                    ArrayAdapter<ResponseLiveSubCategory> arrayAdapter = new ArrayAdapter<ResponseLiveSubCategory>(requireActivity(), android.R.layout.simple_spinner_item, subCategoryList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spinsubcategory.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ResponseLiveSubCategory>> call, Throwable t) {

            }
        });
    }

    private void getCategory() {
        datasetcat = new ArrayList<>();
        RetrofitInstance.getApiService(requireActivity()).FetchLiveCategory(LocalPreferences.getToken(requireActivity())).enqueue(new Callback<List<ResponseLiveCategory>>() {
            @Override
            public void onResponse(Call<List<ResponseLiveCategory>> call, Response<List<ResponseLiveCategory>> response) {
                if (response.body() != null && response.code() == 200) {
                    List<ResponseLiveCategory> stores = response.body();
                    datasetcat = stores;
                    ArrayAdapter<ResponseLiveCategory> arrayAdapter = new ArrayAdapter<ResponseLiveCategory>(requireActivity(), android.R.layout.simple_spinner_item, datasetcat);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spincategory.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseLiveCategory>> call, Throwable t) {

            }
        });
    }


    private void callReportApi() {
        showProgressDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("auditID", "");
        jsonObject.addProperty("fromDate", fromdate);
        jsonObject.addProperty("toDate", todate);
        jsonObject.addProperty("warehouseId", storeID);
        jsonObject.addProperty("categoryId", categoryId);
        jsonObject.addProperty("subCategoryId", subcatcategoryId);
        jsonObject.addProperty("locationId", systemLocationID);

        RetrofitInstance.getApiService(requireActivity()).GetAuditSummary(LocalPreferences.getToken(requireActivity()), jsonObject).enqueue(new Callback<ResponseAuditReport>() {
            @Override
            public void onResponse(Call<ResponseAuditReport> call, Response<ResponseAuditReport> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    ResponseAuditReport responseAuditReport = response.body();
                    List<ResponseReportList> dataset = responseAuditReport.getResult();
                    if (dataset!=null&&dataset.size()!=0){
                        binding.recyvauditsummary.setAdapter(new AuditSummaryReportAdapter(requireActivity(), dataset, CategoryReportFragment.this));

                    }else{
                        showerror("No Records Found");
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseAuditReport> call, Throwable t) {
                hideProgressDialog();
                showerror("Failed");
            }
        });
    }


    @Override
    public void replaceFragment(String menuname) {

    }

    @Override
    public void passlist(List<Integer> dataset) {

    }

    @Override
    public void scanaction(String auditId, String toBeAuditedOn, String remark) {
        startActivity(new Intent(requireActivity(), ReportDetailsActivity.class)
                .putExtra("categoryId", categoryId)
                .putExtra("subcatcategoryId",subcatcategoryId)
                .putExtra("auditId", auditId).putExtra("storeID", storeID));


    }
}