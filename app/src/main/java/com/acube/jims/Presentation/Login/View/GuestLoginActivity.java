package com.acube.jims.Presentation.Login.View;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.acube.jims.BaseActivity;
import com.acube.jims.Presentation.GuestHomePage.GuestHomePageActivity;
import com.acube.jims.Presentation.CustomerManagment.ViewModel.CheckCustomerViewModel;
import com.acube.jims.Presentation.CustomerManagment.ViewModel.CreateCustomerViewModel;
import com.acube.jims.Presentation.CustomerManagment.ViewModel.UpdateCustomerViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.AppUtility;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.ActivityGuestLoginBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Authentication.ResponseCheckCustomer;
import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.google.gson.JsonObject;

public class GuestLoginActivity extends BaseActivity {
    ActivityGuestLoginBinding binding;
    private CheckCustomerViewModel viewModel;
    private CreateCustomerViewModel createCustomerViewModel;
    Boolean vauserExists = false;
    UpdateCustomerViewModel updateCustomerViewModel;
    int UserId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_guest_login);
        viewModel = ViewModelProviders.of(this).get(CheckCustomerViewModel.class);
        createCustomerViewModel = ViewModelProviders.of(this).get(CreateCustomerViewModel.class);
        updateCustomerViewModel = ViewModelProviders.of(this).get(UpdateCustomerViewModel.class);

        initviewmodels();
        binding.tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
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
                } else if (!new AppUtility(GuestLoginActivity.this).isValidMail(vaemail)) {
                    binding.edEmail.setError("Invalid Email");
                } else if (vaguestname.equalsIgnoreCase("")) {
                    binding.edGuestname.setError("Field Empty");
                } else if (vauserExists) {
                    updateGuestCustomer(UserId, vamobile, vaemail, vaguestname);


                } else if (new AppUtility(GuestLoginActivity.this).checkInternet()) {
                    createGuestCustomer(vamobile, vaemail, vaguestname);
                } else {
                    customSnackBar(binding.parent, getResources().getString(R.string.no_internet));
                }

            }
        });

        ((EditText) findViewById(R.id.ed_mobile)).setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                // When focus is lost check that the text field has valid values.

                if (!hasFocus) {
                    {
                        // Validate youredittext
                        Log.d("onFocusChange", "onFocusChange: ");
                        String vaphone = binding.edMobile.getText().toString();
                        if (!vaphone.equalsIgnoreCase("")) {
                            viewModel.CheckUserExists(vaphone);
                            showProgressDialog();
                        }


                    }
                }
            }


        });
        viewModel.getCustomerLiveData().observe(this, new Observer<ResponseCheckCustomer>() {
            @Override
            public void onChanged(ResponseCheckCustomer responseCheckCustomer) {
                hideProgressDialog();
                if (responseCheckCustomer != null) {
                    vauserExists = true;
                    UserId = responseCheckCustomer.getId();
                    binding.edEmail.setText(responseCheckCustomer.getEmailID());
                    binding.edGuestname.setText(responseCheckCustomer.getCustomerName());
                } else {
                    vauserExists = false;

                }

            }
        });
        createCustomerViewModel.getCustomerLiveData().observe(this, new Observer<ResponseCreateCustomer>() {
            @Override
            public void onChanged(ResponseCreateCustomer responseCreateCustomer) {
                hideProgressDialog();
                if (responseCreateCustomer != null) {
                    startActivity(new Intent(getApplicationContext(), GuestHomePageActivity.class));

                } else {

                    customSnackBar(binding.parent, getResources().getString(R.string.something_wrong));
                }
            }
        });
        updateCustomerViewModel.getCustomerLiveData().observe(this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                if (jsonObject != null) {
                    startActivity(new Intent(getApplicationContext(), GuestHomePageActivity.class));

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
        jsonObject.addProperty("companyID", AppConstants.applicationID);
        createCustomerViewModel.CreateCustomer(AppConstants.Authorization + LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.Token),jsonObject);

    }

    private void updateGuestCustomer(int ID, String vamobile, String vaemail, String vaguestname) {
        showProgressDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("customerName", vaguestname);
        jsonObject.addProperty("emailID", vaemail);
        jsonObject.addProperty("contactNumber", vamobile);
        jsonObject.addProperty("companyID", AppConstants.applicationID);
        jsonObject.addProperty("id", ID);
        updateCustomerViewModel.UpdateCustomer(ID, jsonObject);

    }

    public void initviewmodels() {
        viewModel.init();
        createCustomerViewModel.init();
        updateCustomerViewModel.init();
    }
}