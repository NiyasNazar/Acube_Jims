package com.acube.jims.presentation.CustomerManagment.View;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;

import com.acube.jims.BaseActivity;
import com.acube.jims.presentation.CustomerManagment.ViewModel.CreateCustomerViewModel;
import com.acube.jims.R;
import com.acube.jims.utils.AppUtility;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.FragmentAddCustomerBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.constants.BackHandler;
import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.acube.jims.presentation.HomePage.View.HomePageActivity;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AddCustomerFragment extends BaseActivity {
    BackHandler backHandler;

    private CreateCustomerViewModel createCustomerViewModel;


    FragmentAddCustomerBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Inflate the layout for this fragment
        binding = DataBindingUtil.setContentView(
                this, R.layout.fragment_add_customer);
        initToolBar(binding.toolbarApp.toolbar, "Add Customer", true);
        createCustomerViewModel = ViewModelProviders.of(this).get(CreateCustomerViewModel.class);
        createCustomerViewModel.init();

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                binding.edMobile.setError(null);
                binding.edEmail.setError(null);
                binding.edGuestname.setError(null);
                String vamobile = binding.edMobile.getText().toString();
                String vaemail = binding.edEmail.getText().toString();
                String vaguestname = binding.edGuestname.getText().toString();
                if (vamobile.equalsIgnoreCase("")) {
                    binding.edMobile.setError("Field Empty");
                } else if (vaemail.equalsIgnoreCase("")) {
                    binding.edEmail.setError("Field Empty");
                } else if (!new AppUtility(AddCustomerFragment.this).isValidMail(vaemail)) {
                    binding.edEmail.setError("Invalid Email");
                } else if (vaguestname.equalsIgnoreCase("")) {
                    binding.edGuestname.setError("Field Empty");
                } else if (new AppUtility(AddCustomerFragment.this).checkInternet()) {
                    createGuestCustomer(vamobile, vaemail, vaguestname);
                } else {
                    //  customSnackBar(binding.parent, getResources().getString(R.string.no_internet));
                }
            }


        });
        createCustomerViewModel.getCustomerLiveData().observe(this, new Observer<ResponseCreateCustomer>() {
            @Override
            public void onChanged(ResponseCreateCustomer responseCreateCustomer) {
                hideProgressDialog();
                if (responseCreateCustomer != null) {
                    if (responseCreateCustomer.getMessage().equalsIgnoreCase("Contact number already exist")) {
                        showerror(responseCreateCustomer.getMessage());
                    } else {
                        LocalPreferences.storeStringPreference(getApplicationContext(), "GuestCustomerName", responseCreateCustomer.getCustomerName());
                        LocalPreferences.storeStringPreference(getApplicationContext(), "GuestCustomerCode", responseCreateCustomer.getCustomerCode());
                        LocalPreferences.storeIntegerPreference(getApplicationContext(), "GuestCustomerID", responseCreateCustomer.getId());
                        Date todaysdate = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String starttime = format.format(todaysdate);
                        LocalPreferences.storeStringPreference(getApplicationContext(), "CustomerSessionStartTime", starttime);
                        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                    }

                } else {


                }
            }
        });


    }

    private void createGuestCustomer(String vamobile, String vaemail, String vaguestname) {
        showProgressDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("customerName", vaguestname);
        jsonObject.addProperty("emailID", vaemail);
        jsonObject.addProperty("contactNumber", vamobile);
        createCustomerViewModel.CreateCustomer(AppConstants.Authorization + LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.Token), jsonObject);

    }


}