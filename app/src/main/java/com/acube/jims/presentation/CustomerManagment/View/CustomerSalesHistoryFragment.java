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

import com.acube.jims.BaseFragment;
import com.acube.jims.presentation.CustomerManagment.ViewModel.CustomerHistoryViewModel;
import com.acube.jims.presentation.CustomerManagment.adapter.SalesHistoryAdapter;
import com.acube.jims.presentation.ProductDetails.View.ProductDetailsFragment;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.FragmentCustomerViewedItemsBinding;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerHistory;
import com.acube.jims.datalayer.models.CustomerManagment.SalesHistory;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerSalesHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerSalesHistoryFragment extends BaseFragment implements SalesHistoryAdapter.ReplaceFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerSalesHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerViewedItemsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerSalesHistoryFragment newInstance(String param1, String param2) {
        CustomerSalesHistoryFragment fragment = new CustomerSalesHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FragmentCustomerViewedItemsBinding binding;
    CustomerHistoryViewModel customerHistoryViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_customer_viewed_items, container, false);
        showProgressDialog();
        binding.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        customerHistoryViewModel = ViewModelProviders.of(this).get(CustomerHistoryViewModel.class);
        customerHistoryViewModel.init();
        String Customername = LocalPreferences.retrieveStringPreferences(getContext(), "custname");
        String CustomerCode = LocalPreferences.retrieveStringPreferences(getContext(), "custcode");

        Integer GuestCustomerID = LocalPreferences.retrieveIntegerPreferences(getActivity(), "GuestCustomerID");
        customerHistoryViewModel.CustomerHistory(LocalPreferences.getToken(getActivity()), GuestCustomerID);
        customerHistoryViewModel.getCustomerLiveData().observe(getActivity(), new Observer<ResponseCustomerHistory>() {
            @Override
            public void onChanged(ResponseCustomerHistory responseCustomerHistory) {
                hideProgressDialog();
                if (responseCustomerHistory != null) {
                    List<SalesHistory> itemViewHistory = responseCustomerHistory.getSaleHistory();
                    binding.recyvvieweditems.setAdapter(new SalesHistoryAdapter(getActivity(), itemViewHistory, CustomerSalesHistoryFragment.this));
                }
            }
        });
        binding.recyvvieweditems.setLayoutManager(new GridLayoutManager(getActivity(), 3));


        return binding.getRoot();

    }

    @Override
    public void replacefragments(String Id) {
      //  FragmentHelper.replaceFragment(getActivity(), R.id.content, ProductDetailsFragment.newInstance(Id));
        startActivity(new Intent(getActivity(),ProductDetailsFragment.class).putExtra("Id",Id));

    }
}