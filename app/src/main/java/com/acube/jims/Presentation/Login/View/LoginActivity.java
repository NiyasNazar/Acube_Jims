package com.acube.jims.Presentation.Login.View;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.acube.jims.BaseActivity;
import com.acube.jims.Presentation.HomePage.View.HomePageActivity;
import com.acube.jims.Presentation.Login.ViewModel.LoginViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.AppUtility;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.ActivityLoginBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Authentication.ResponseLogin;
import com.google.gson.JsonObject;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;
    LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        viewModel.init();


        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                binding.edEmail.setError(null);
                binding.edPassword.setError(null);
                String vaEmail = binding.edEmail.getText().toString();
                String vaPassword = binding.edPassword.getText().toString();
                if (vaEmail.equalsIgnoreCase("")) {
                    binding.edEmail.setError("Field Empty");
                } else if (vaPassword.equalsIgnoreCase("")) {
                    binding.edPassword.setError("Field Empty");

                } else if (new AppUtility(LoginActivity.this).checkInternet()) {
                    LoginCustomer(vaEmail, vaPassword);
                } else {
                    customSnackBar(binding.parent, getResources().getString(R.string.no_internet));


                }


            }
        });


        viewModel.getLiveData().observe(this, new Observer<ResponseLogin>() {
            @Override
            public void onChanged(ResponseLogin responseLogin) {
                hideProgressDialog();
                if (responseLogin != null) {

                    startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                    finish();
                    LocalPreferences.storeStringPreference(getApplicationContext(), "Token", responseLogin.getToken());


                } else {
                    customSnackBar(binding.parent, getResources().getString(R.string.autherror));
                }
            }
        });
        binding.tvguestlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GuestLoginActivity.class));
            }
        });
    }

    private void LoginCustomer(String vaEmail, String vaPassword) {
        showProgressDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("companyID", AppConstants.companyID);
        jsonObject.addProperty("applicationID", AppConstants.applicationID);
        jsonObject.addProperty("userName", vaEmail);
        jsonObject.addProperty("password", vaPassword);
        viewModel.Login(jsonObject);
    }
}