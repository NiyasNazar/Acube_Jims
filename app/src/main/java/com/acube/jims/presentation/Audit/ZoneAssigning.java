package com.acube.jims.presentation.Audit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityZoneAssigningBinding;
import com.acube.jims.datalayer.api.ResponseLiveCategory;
import com.acube.jims.datalayer.api.ResponseLiveSubCategory;
import com.acube.jims.datalayer.api.ResponseLocationforreport;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Audit.ResponseLiveStore;
import com.acube.jims.datalayer.models.warehouse.ResponseItems;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.utils.OnSingleClickListener;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZoneAssigning extends BaseActivity {
    ActivityZoneAssigningBinding binding;
    List<ResponseLiveStore> storesdatset;
    int systemLocationID = 0;
    int categoryId = 0;
    int storeID = 0;
    int itemID = 0;
    List<ResponseLocationforreport> datasetloc;
    List<ResponseLiveCategory> datasetcat;
    List<ResponseLiveSubCategory> subCategoryList;
    int subcatcategoryId = 0;
    List<ResponseItems> datasetItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_zone_assigning);
        initToolBar(binding.toolbarApp.toolbar, "Assign Zones", true);
        getStore();
        getCategory();
        binding.btnAssignzone.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("categoryId", categoryId);
                jsonObject.addProperty("subCategoryId", subcatcategoryId);
                jsonObject.addProperty("itemId", itemID);
                jsonObject.addProperty("warehouseId", storeID);
                jsonObject.addProperty("locationId", systemLocationID);
                postdata(jsonObject);

            }
        });
        binding.spstore.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                storeID = storesdatset.get(position).getId();
                getLocation(storeID);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spzone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                systemLocationID = datasetloc.get(position).getId();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryId = datasetcat.get(position).getId();
                getSubcategory(categoryId);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spsubcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subcatcategoryId = subCategoryList.get(position).getId();
                getItems(subcatcategoryId);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spitems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemID = datasetItems.get(position).getId();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void postdata(JsonObject jsonObject) {
        showProgressDialog();
        RetrofitInstance.getApiService(getApplicationContext()).assignZone(LocalPreferences.getToken(getApplicationContext()), jsonObject).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    if (response.body()) {
                        showsuccess("Success");
                    }
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                showerror("Failed");
                hideProgressDialog();

            }
        });
    }

    private void getItems(int subcatcategoryId) {
        datasetItems = new ArrayList<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("subCategoryId", subcatcategoryId);
        RetrofitInstance.getApiService(getApplicationContext()).getItems(LocalPreferences.getToken(getApplicationContext()), jsonObject).enqueue(new Callback<List<ResponseItems>>() {
            @Override
            public void onResponse(Call<List<ResponseItems>> call, Response<List<ResponseItems>> response) {
                if (response.body() != null) {
                    List<ResponseItems> stores = response.body();
                    datasetItems = stores;
                    ArrayAdapter<ResponseItems> arrayAdapter = new ArrayAdapter<ResponseItems>(getApplicationContext(), android.R.layout.simple_spinner_item, stores);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spitems.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseItems>> call, Throwable t) {

            }
        });
    }

    private void getStore() {
        storesdatset = new ArrayList<>();
        RetrofitInstance.getApiService(getApplicationContext()).FetchLiveStore(LocalPreferences.getToken(getApplicationContext())).enqueue(new Callback<List<ResponseLiveStore>>() {
            @Override
            public void onResponse(Call<List<ResponseLiveStore>> call, Response<List<ResponseLiveStore>> response) {
                if (response.body() != null && response.code() == 200) {
                    List<ResponseLiveStore> stores = response.body();
                    storesdatset = stores;
                    ArrayAdapter<ResponseLiveStore> arrayAdapter = new ArrayAdapter<ResponseLiveStore>(getApplicationContext(), android.R.layout.simple_spinner_item, stores);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spstore.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseLiveStore>> call, Throwable t) {

            }
        });
    }

    private void getLocation(int storeID) {
        datasetloc = new ArrayList<>();
        RetrofitInstance.getApiService(ZoneAssigning.this).GetLocationbystore(LocalPreferences.getToken(getApplicationContext()), storeID).enqueue(new Callback<List<ResponseLocationforreport>>() {
            @Override
            public void onResponse(Call<List<ResponseLocationforreport>> call, Response<List<ResponseLocationforreport>> response) {
                if (response.body() != null && response.code() == 200) {
                    List<ResponseLocationforreport> loc = response.body();
                    datasetloc = loc;
                    ArrayAdapter<ResponseLocationforreport> arrayAdapter = new ArrayAdapter<ResponseLocationforreport>(getApplicationContext(), android.R.layout.simple_spinner_item, datasetloc);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spzone.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseLocationforreport>> call, Throwable t) {

            }
        });
    }

    private void getCategory() {
        datasetcat = new ArrayList<>();
        RetrofitInstance.getApiService(getApplicationContext()).FetchLiveCategory(LocalPreferences.getToken(getApplicationContext())).enqueue(new Callback<List<ResponseLiveCategory>>() {
            @Override
            public void onResponse(Call<List<ResponseLiveCategory>> call, Response<List<ResponseLiveCategory>> response) {
                if (response.body() != null && response.code() == 200) {
                    List<ResponseLiveCategory> stores = response.body();
                    datasetcat = stores;
                    ArrayAdapter<ResponseLiveCategory> arrayAdapter = new ArrayAdapter<ResponseLiveCategory>(getApplicationContext(), android.R.layout.simple_spinner_item, datasetcat);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spcat.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<ResponseLiveCategory>> call, Throwable t) {

            }
        });
    }

    private void getSubcategory(int categoryId) {
        subCategoryList = new ArrayList<>();
        RetrofitInstance.getApiService(getApplicationContext()).FetchLiveSubCategory(LocalPreferences.getToken(getApplicationContext()), categoryId).enqueue(new Callback<List<ResponseLiveSubCategory>>() {
            @Override
            public void onResponse(Call<List<ResponseLiveSubCategory>> call, Response<List<ResponseLiveSubCategory>> response) {
                if (response.body() != null && response.code() == 200) {
                    List<ResponseLiveSubCategory> dataset = response.body();
                    subCategoryList = dataset;
                    ArrayAdapter<ResponseLiveSubCategory> arrayAdapter = new ArrayAdapter<ResponseLiveSubCategory>(getApplicationContext(), android.R.layout.simple_spinner_item, subCategoryList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spsubcat.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ResponseLiveSubCategory>> call, Throwable t) {

            }
        });
    }
}