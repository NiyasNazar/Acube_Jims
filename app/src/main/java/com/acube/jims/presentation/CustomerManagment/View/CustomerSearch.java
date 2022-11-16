package com.acube.jims.presentation.CustomerManagment.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.acube.jims.BaseActivity;
import com.acube.jims.presentation.Catalogue.View.CatalogueActivity;
import com.acube.jims.presentation.HomePage.ViewModel.CustomerViewModel;
import com.acube.jims.presentation.CustomerManagment.ViewModel.CreateCustomerViewModel;
import com.acube.jims.R;
import com.acube.jims.utils.AppUtility;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ActivityCustomerSearchBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.google.gson.JsonObject;

public class CustomerSearch extends BaseActivity {
    ActivityCustomerSearchBinding binding;
    private CreateCustomerViewModel createCustomerViewModel;
    CustomerViewModel customerViewModel;
    String AuthToken;
    ReplacefromCustomerLogin replacefromCustomerLoging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_search);
        createCustomerViewModel = ViewModelProviders.of(this).get(CreateCustomerViewModel.class);
        createCustomerViewModel.init();
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
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
                } else if (!new AppUtility(CustomerSearch.this).isValidMail(vaemail)) {
                    binding.edEmail.setError("Invalid Email");
                } else if (vaguestname.equalsIgnoreCase("")) {
                    binding.edGuestname.setError("Field Empty");
                } else if (new AppUtility(CustomerSearch.this).checkInternet()) {
                    createGuestCustomer(vamobile, vaemail, vaguestname);
                } else {
                    customSnackBar(binding.parent, getResources().getString(R.string.no_internet));
                }
            }
        });
        binding.imvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        createCustomerViewModel.getCustomerLiveData().observe(this, new Observer<ResponseCreateCustomer>() {
            @Override
            public void onChanged(ResponseCreateCustomer responseCreateCustomer) {
                hideProgressDialog();
                if (responseCreateCustomer != null) {
                    LocalPreferences.storeBooleanPreference(getApplicationContext(), "salesman", false);

                    //   FragmentHelper.replaceFragment(CustomerSearch.this, R.id.content, new CatalogueFragment());
                    startActivity(new Intent(getApplicationContext(), CatalogueActivity.class));
                    finish();
                } else {

                    customSnackBar(binding.parent, getResources().getString(R.string.something_wrong));
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

        createCustomerViewModel.CreateCustomer(AppConstants.Authorization + LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.Token), jsonObject,getApplicationContext());

    }

    public interface ReplacefromCustomerLogin {
        void replacecatalogfragment();
    }

}