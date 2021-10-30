package com.acube.jims.Presentation.Login.View;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Network;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.acube.jims.BaseActivity;
import com.acube.jims.Presentation.DeviceRegistration.View.DeviceRegistrationFragment;
import com.acube.jims.Presentation.HomePage.View.HomePageActivity;
import com.acube.jims.Presentation.HomePage.ViewModel.HomeViewModel;
import com.acube.jims.Presentation.Login.ViewModel.LoginViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.AppUtility;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.ActivityLoginBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Authentication.ResponseLogin;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.Invoice.KaratPrice;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;
    LoginViewModel viewModel;
    private HomeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        viewModel.init();

        binding.edEmail.setText("Admin");
        binding.edPassword.setText("Admin@acube");

        Log.d("onCreate", "onCreate: " + getMacAddr());


        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        mViewModel.init();
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


        mViewModel.getLiveData().observe(this, new Observer<List<HomeData>>() {
            @Override
            public void onChanged(List<HomeData> homeData) {
                hideProgressDialog();
                if (homeData != null) {
                    setList("HomeMenu", homeData);
                 /*   DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                         .homeMenuDao()
                          .insert(homeData);*/


                    startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                    finish();
                }
                // binding.recyvhomemenu.setAdapter(new HomeAdapter(getActivity(), homeData, HomeFragment.this::replaceFragment));
            }
        });
        viewModel.getLiveData().observe(this, new Observer<ResponseLogin>() {
            @Override
            public void onChanged(ResponseLogin responseLogin) {

                if (responseLogin != null) {
                    LocalPreferences.storeAuthenticationToken(getApplicationContext(), responseLogin.getToken());
                    LocalPreferences.storeStringPreference(getApplicationContext(), AppConstants.UserRole, String.valueOf(responseLogin.getRoleName()));
                    LocalPreferences.storeStringPreference(getApplicationContext(), AppConstants.UserID, String.valueOf(responseLogin.getEmployeeID()));
                    LocalPreferences.storeStringPreference(getApplicationContext(), "CompanyID", responseLogin.getCompanyID());
                    LocalPreferences.storeStringPreference(getApplicationContext(), "warehouseId", String.valueOf(responseLogin.getWarehouseID()));
                    LocalPreferences.storeStringPreference(getApplicationContext(), "EmployeeName", responseLogin.getEmployeeName());
                    LocalPreferences.storeBooleanPreference(getApplicationContext(), "showlogout", true);
                    List<KaratPrice>dataset=responseLogin.getKaratPriceList();
                    String karatprice;
                    StringBuilder result = new StringBuilder();

                    for (int i=0;i<dataset.size();i++){
                        karatprice=dataset.get(i).getKaratCode()+"K: "+dataset.get(i).getKaratPrice();
                        result.append(karatprice);
                        result.append(",");

                    }
                    Log.d("TAG", "onChanged: "+result.toString());
                    LocalPreferences.storeStringPreference(getApplicationContext(),"GoldRate",result.toString());

                    mViewModel.getHomeMenu(LocalPreferences.getToken(getApplicationContext()), AppConstants.HomeMenuAppName, String.valueOf(responseLogin.getRoleName()));


                } else {
                    hideProgressDialog();
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

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(getApplicationContext(), key, json);
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            Log.d("TAG", "getMacAddr: " + ex);
        }
        return "02:00:00:00:00:00";
    }
}