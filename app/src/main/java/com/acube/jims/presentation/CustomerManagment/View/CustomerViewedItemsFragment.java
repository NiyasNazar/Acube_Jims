package com.acube.jims.presentation.CustomerManagment.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.acube.jims.BaseActivity;
import com.acube.jims.presentation.CustomerManagment.ViewModel.CustomerHistoryViewModel;
import com.acube.jims.presentation.CustomerManagment.adapter.LastViewedAdapterAll;
import com.acube.jims.presentation.ProductDetails.View.ProductDetailsFragment;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.FragmentCustomerViewedItemsBinding;
import com.acube.jims.datalayer.models.CustomerManagment.ItemViewHistory;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerHistory;

import java.util.List;


public class CustomerViewedItemsFragment extends BaseActivity implements LastViewedAdapterAll.ReplaceFragment {




    FragmentCustomerViewedItemsBinding binding;
    CustomerHistoryViewModel customerHistoryViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(
                this, R.layout.fragment_customer_viewed_items );
        showProgressDialog();
        String Customername = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "custname");
        String CustomerCode = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "custcode");

        initToolBar(binding.toolbarApp.toolbar,Customername,true);


        customerHistoryViewModel = ViewModelProviders.of(this).get(CustomerHistoryViewModel.class);
        customerHistoryViewModel.init();
        int GuestCustomerID = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        customerHistoryViewModel.CustomerHistory(LocalPreferences.getToken(getApplicationContext()), GuestCustomerID);
        customerHistoryViewModel.getCustomerLiveData().observe(this, new Observer<ResponseCustomerHistory>() {
            @Override
            public void onChanged(ResponseCustomerHistory responseCustomerHistory) {
                hideProgressDialog();
                if (responseCustomerHistory != null) {
                    List<ItemViewHistory> itemViewHistory = responseCustomerHistory.getItemViewHistory();
                    binding.recyvvieweditems.setAdapter(new LastViewedAdapterAll(getApplicationContext(), itemViewHistory, CustomerViewedItemsFragment.this));
                }
            }
        });
        binding.recyvvieweditems.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));



    }

    @Override
    public void replacefragments(String Id) {
        startActivity(new Intent(getApplicationContext(),ProductDetailsFragment.class).putExtra("Id",Id));

    }
}