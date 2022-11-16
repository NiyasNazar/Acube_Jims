package com.acube.jims.presentation.Report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityMissingComparisonBinding;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.missingcomp.AuditDate;
import com.acube.jims.datalayer.models.missingcomp.MissinSerialNo;
import com.acube.jims.datalayer.models.missingcomp.MissingCompResult;
import com.acube.jims.datalayer.models.missingcomp.MissingDetail;
import com.acube.jims.datalayer.models.missingcomp.ResponseMissingComp;
import com.acube.jims.datalayer.models.report.ItemWiseReport;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.Report.adapter.MissingComparisonAdapter;
import com.acube.jims.presentation.Report.adapter.MissingDateAdapter;
import com.acube.jims.utils.LocalPreferences;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MissingComparison extends BaseActivity implements MissingComparisonAdapter.FragmentTransition {
    ActivityMissingComparisonBinding binding;
    int systemLocationID, storeID, categoryId, itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_missing_comparison);
        binding.recyvfound.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initToolBar(binding.toolbarApp.toolbar, "Missing Comparison", true);

        systemLocationID = getIntent().getIntExtra("systemLocationID", 0);
        storeID = getIntent().getIntExtra("storeID", 0);
        categoryId = getIntent().getIntExtra("categoryId", 0);
        showProgressDialog();
        binding.recyvcomp.setLayoutManager(new GridLayoutManager(getApplicationContext(), 5));

        DatabaseClient.getInstance(MissingComparison.this).getAppDatabase().auditDownloadDao().getItemwiseReport().observe(this, new Observer<List<ItemWiseReport>>() {
            @Override
            public void onChanged(List<ItemWiseReport> itemWiseReports) {
                List<String> datastring = new ArrayList<>();
                JSONArray jsonArray = new JSONArray();

                for (int i = 0; i < itemWiseReports.size(); i++) {
                    datastring.add(itemWiseReports.get(i).getSerialNumber());
                    jsonArray.put(itemWiseReports.get(i).getSerialNumber());


                }

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("warehouseId", storeID);
                    jsonObject.put("subCategoryId", categoryId);
                    jsonObject.put("locationId", systemLocationID);
                    jsonObject.put("serialList", jsonArray);
                    JsonParser jsonParser = new JsonParser();
                    JsonObject gsonObject = (JsonObject) jsonParser.parse(jsonObject.toString());

                    getDetails(gsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    private void getDetails(JsonObject jsonObject) {
        RetrofitInstance.getApiService(getApplicationContext()).GetMissingReport(LocalPreferences.getToken(getApplicationContext()), jsonObject).enqueue(new Callback<ResponseMissingComp>() {
            @Override
            public void onResponse(Call<ResponseMissingComp> call, Response<ResponseMissingComp> response) {
                hideProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ResponseMissingComp responseMissingComp = response.body();
                    List<MissinSerialNo> dataset = responseMissingComp.getMissinSerialNoList();
                    List<AuditDate> datasetdate = responseMissingComp.getAuditDates();
                    binding.recyvfound.setAdapter(new MissingComparisonAdapter(getApplicationContext(), dataset, MissingComparison.this));
                    binding.recyvcomp.setAdapter(new MissingDateAdapter(getApplicationContext(), datasetdate));
                } else {
                    showerror("Failed");
                }


            }

            @Override
            public void onFailure(Call<ResponseMissingComp> call, Throwable t) {
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

    }
}