package com.acube.jims.presentation.ItemRequest.view;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ActivitySalesmanItemRequestBinding;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.ItemRequest.ResponseFetchPickList;
import com.acube.jims.presentation.ItemRequest.adapter.PickListAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesmanItemRequestActivity extends BaseActivity implements  PickListAdapter.FragmentTransition {
    ActivitySalesmanItemRequestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_salesman_item_request);
        initToolBar(binding.toolbarApp, "Pick List", true);
        binding.recyvpicklist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }

    private void fetchPickList() {
        showProgressDialog();
        RetrofitInstance.getApiService(getApplicationContext()).FetchPickList(LocalPreferences.getToken(getApplicationContext())).enqueue(new Callback<List<ResponseFetchPickList>>() {
            @Override
            public void onResponse(Call<List<ResponseFetchPickList>> call, Response<List<ResponseFetchPickList>> response) {
                showProgressDialog();
                if (response.body()!=null&&response.code()==200){
                    List<ResponseFetchPickList>dataset=response.body();
                    binding.recyvpicklist.setAdapter(new PickListAdapter(getApplicationContext(),dataset,SalesmanItemRequestActivity.this));



                }
            }

            @Override
            public void onFailure(Call<List<ResponseFetchPickList>> call, Throwable t) {
                showProgressDialog();
            }
        });
    }

    @Override
    public void replaceFragment(String picklistno) {
        startActivity(new Intent(getApplicationContext(), ItemRequestPickListDetailsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("picklistno",picklistno));

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchPickList();
    }
}