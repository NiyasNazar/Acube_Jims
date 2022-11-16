package com.acube.jims.presentation.CustomerManagment.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.acube.jims.BaseActivity;
import com.acube.jims.BaseFragment;
import com.acube.jims.presentation.CustomerManagment.ViewModel.CustomerHistoryViewModel;
import com.acube.jims.presentation.CustomerManagment.adapter.SalesHistoryAdapter;
import com.acube.jims.presentation.ProductDetails.View.ProductDetailsFragment;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.FragmentCustomerViewedItemsBinding;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerHistory;
import com.acube.jims.datalayer.models.CustomerManagment.SalesHistory;

import java.util.List;


public class CustomerSalesHistoryFragment extends BaseActivity implements SalesHistoryAdapter.ReplaceFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;






    FragmentCustomerViewedItemsBinding binding;
    CustomerHistoryViewModel customerHistoryViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(
                this, R.layout.fragment_customer_viewed_items );

        showProgressDialog();

        customerHistoryViewModel = ViewModelProviders.of(this).get(CustomerHistoryViewModel.class);
        customerHistoryViewModel.init();
        String Customername = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "custname");
        String CustomerCode = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "custcode");

        Integer GuestCustomerID = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        customerHistoryViewModel.CustomerHistory(LocalPreferences.getToken(getApplicationContext()), GuestCustomerID,getApplicationContext());
        customerHistoryViewModel.getCustomerLiveData().observe(this, new Observer<ResponseCustomerHistory>() {
            @Override
            public void onChanged(ResponseCustomerHistory responseCustomerHistory) {
                hideProgressDialog();
                if (responseCustomerHistory != null) {
                    List<SalesHistory> itemViewHistory = responseCustomerHistory.getSaleHistory();
                    binding.recyvvieweditems.setAdapter(new SalesHistoryAdapter(getApplicationContext(), itemViewHistory, CustomerSalesHistoryFragment.this));
                }
            }
        });
        binding.recyvvieweditems.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));



    }

    @Override
    public void replacefragments(String Id) {
      //  FragmentHelper.replaceFragment(getActivity(), R.id.content, ProductDetailsFragment.newInstance(Id));
        startActivity(new Intent(getApplicationContext(),ProductDetailsFragment.class).putExtra("Id",Id));

    }
}