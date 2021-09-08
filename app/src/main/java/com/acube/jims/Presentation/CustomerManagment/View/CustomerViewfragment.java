package com.acube.jims.Presentation.CustomerManagment.View;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.CustomerManagment.ViewModel.CustomerHistoryViewModel;
import com.acube.jims.Presentation.CustomerManagment.adapter.LastViewedAdapter;
import com.acube.jims.Presentation.HomePage.View.HomeFragment;
import com.acube.jims.Presentation.ProductDetails.View.ProductDetailsFragment;
import com.acube.jims.R;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.FragmentCustomerViewfragmentBinding;
import com.acube.jims.datalayer.models.CustomerManagment.ItemViewHistory;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerHistory;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerViewfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerViewfragment extends BaseFragment implements LastViewedAdapter.ReplaceFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CustomerViewfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerViewfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerViewfragment newInstance(String param1, String param2) {
        CustomerViewfragment fragment = new CustomerViewfragment();
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

    FragmentCustomerViewfragmentBinding binding;
    CustomerHistoryViewModel customerHistoryViewModel;
    String GuestCustomerName, GuestCustomerCode, GuestCustomerID, CustomerSessionStartTime,CustomerMobile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_customer_viewfragment, container, false);
        customerHistoryViewModel = ViewModelProviders.of(this).get(CustomerHistoryViewModel.class);
        customerHistoryViewModel.init();
        binding.toolbar.tvFragname.setText("Customer History");

        binding.btnSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCustomer();

            }
        });
        binding.recyvvieweditems.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        GuestCustomerID = LocalPreferences.retrieveStringPreferences(getActivity(), "GuestCustomerID");
        showProgressDialog();
        customerHistoryViewModel.CustomerHistory(LocalPreferences.getToken(getActivity()), GuestCustomerID);
        customerHistoryViewModel.getCustomerLiveData().observe(getActivity(), new Observer<ResponseCustomerHistory>() {
            @Override
            public void onChanged(ResponseCustomerHistory responseCustomerHistory) {
                hideProgressDialog();
                if (responseCustomerHistory != null) {

                    GuestCustomerName = responseCustomerHistory.getCustomerName();
                    GuestCustomerCode = responseCustomerHistory.getCustomerCode();
                    CustomerMobile=responseCustomerHistory.getCustomerContactNo();
                    Log.d("onChanged", "onChanged: " + GuestCustomerName);
                    binding.tvCustomername.setText(GuestCustomerName);
                    binding.tvcustomercode.setText(responseCustomerHistory.getCustomerCode());
                    binding.tvcustomercontact.setText(responseCustomerHistory.getCustomerContactNo());
                    binding.tvlastvisit.setText(ParseDate(responseCustomerHistory.getLastVisit()));
                    binding.tvcustomeremail.setText(responseCustomerHistory.getCustomerEmailID());
                    binding.tvtotalvisit.setText("" + responseCustomerHistory.getTotalVisit());
                    binding.tvtoalpurchase.setText("" + responseCustomerHistory.getTotalPurchase());
                    List<ItemViewHistory> itemViewHistory = responseCustomerHistory.getItemViewHistory();
                    binding.recyvvieweditems.setAdapter(new LastViewedAdapter(getActivity(), itemViewHistory, CustomerViewfragment.this));


                }

            }
        });

        return binding.getRoot();
    }

    public void SelectCustomer() {

        Date todaysdate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String starttime = format.format(todaysdate);
        System.out.println(starttime);
        LocalPreferences.storeStringPreference(getActivity(), "GuestCustomerName", GuestCustomerName);
        LocalPreferences.storeStringPreference(getActivity(), "GuestCustomerCode", GuestCustomerCode);
        LocalPreferences.storeStringPreference(getActivity(), "GuestCustomerID", GuestCustomerID);
        LocalPreferences.storeStringPreference(getActivity(), "CustomerSessionStartTime", starttime);
        LocalPreferences.storeStringPreference(getActivity(),"CustomerMobile",CustomerMobile);
        FragmentHelper.replaceFragment(getActivity(), R.id.content, new HomeFragment(), "");
    }

    @Override
    public void replacefragments(String Id) {
        FragmentHelper.replaceFragment(getActivity(), R.id.content, ProductDetailsFragment.newInstance(Id));
    }


    public String ParseDate(String lastvisitdate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(lastvisitdate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        return formattedDate;
    }
}