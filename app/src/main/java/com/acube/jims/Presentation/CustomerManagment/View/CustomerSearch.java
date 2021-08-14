package com.acube.jims.Presentation.CustomerManagment.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.acube.jims.BaseActivity;
import com.acube.jims.Presentation.Catalogue.View.CatalogueFragment;
import com.acube.jims.Presentation.GuestHomePage.GuestHomePageActivity;
import com.acube.jims.Presentation.HomePage.ViewModel.CustomerViewModel;
import com.acube.jims.Presentation.HomePage.adapter.CustomerListAdapter;
import com.acube.jims.Presentation.Login.View.GuestLoginActivity;
import com.acube.jims.Presentation.Login.ViewModel.CreateCustomerViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.AppUtility;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.ActivityCustomerSearchBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerListing;
import com.google.gson.JsonObject;

import java.util.List;

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

                    FragmentHelper.replaceFragment(CustomerSearch.this, R.id.content, new CatalogueFragment());
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

        createCustomerViewModel.CreateCustomer(AppConstants.Authorization + LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.Token), jsonObject);

    }
   public interface ReplacefromCustomerLogin{
        void replacecatalogfragment();
    }

}