package com.acube.jims.presentation.Login.View;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.acube.jims.BaseActivity;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.CustomerManagment.View.CustomerBottomSheetFragment;
import com.acube.jims.presentation.CustomerManagment.View.CustomerViewfragment;
import com.acube.jims.presentation.HomePage.View.HomePageActivity;
import com.acube.jims.presentation.HomePage.ViewModel.HomeViewModel;
import com.acube.jims.presentation.Login.ViewModel.LoginViewModel;
import com.acube.jims.R;
import com.acube.jims.utils.AppUtility;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ActivityLoginBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Authentication.ResponseLogin;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.Invoice.KaratPrice;
import com.acube.jims.presentation.SettingsActivity;
import com.acube.jims.utils.OnSingleClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;
    LoginViewModel viewModel;
    private HomeViewModel mViewModel;
    boolean rememberme;
    boolean urlupdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        viewModel.init();

        //   binding.edEmail.setText("admin@suntech003.ae");
        //   binding.edPassword.setText("Admin@2022");

      binding.edEmail.setText("sales@acube.com");
      binding.edPassword.setText("Sales@2022");
        Log.d("onCreate", "onCreate: " + getMacAddr());
        urlupdated = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "urlupdated");


        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        mViewModel.init();
        binding.tvsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });

        binding.showPassBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (binding.edPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    ((ImageView) (v)).setImageResource(R.drawable.ic_baseline_visibility_24);

                    //Show Password
                    binding.edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    ((ImageView) (v)).setImageResource(R.drawable.ic_baseline_visibility_off_24);

                    //Hide Password
                    binding.edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });

        rememberme = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "rememberme");

        if (rememberme) {
            binding.rememberMeCheckBox.setChecked(true);
            binding.edEmail.setText(LocalPreferences.retrieveStringPreferences(getApplicationContext(), "LogUser"));
            binding.edPassword.setText(LocalPreferences.retrieveStringPreferences(getApplicationContext(), "LogPassword"));

        } else {
            binding.rememberMeCheckBox.setChecked(false);
        }
        binding.rememberMeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    LocalPreferences.storeBooleanPreference(getApplicationContext(), "rememberme", true);

                } else {
                    LocalPreferences.storeBooleanPreference(getApplicationContext(), "rememberme", false);
                }
            }
        });


        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!urlupdated){
                    showerror("Please configure Url ");

                }else{
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



            }
        });


        mViewModel.getLiveData().observe(this, new Observer<List<HomeData>>() {
            @Override
            public void onChanged(List<HomeData> homeData) {
                hideProgressDialog();
                if (homeData != null) {
                    new Thread(() -> {

                        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                                .homeMenuDao()
                                .deletehomemenus();
                        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                                .homeMenuDao()

                                .insert(homeData);
                        runOnUiThread(this::doLogin);
                    }).start();


                }
                LocalPreferences.storeBooleanPreference(getApplicationContext(), "salesman", true);
                String CustomerCode = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "GuestCustomerCode");
                if (CustomerCode.equalsIgnoreCase("")) {
                    startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                    finish();
                }
                // binding.recyvhomemenu.setAdapter(new HomeAdapter(getActivity(), homeData, HomeFragment.this::replaceFragment));
            }

            private void doLogin() {
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
                    LocalPreferences.storeStringPreference(getApplicationContext(), "Weburl", responseLogin.getWebappurl());
                    LocalPreferences.storeStringPreference(getApplicationContext(), "UserName", responseLogin.getUserName());
                    LocalPreferences.storeStringPreference(getApplicationContext(),"Prefix",responseLogin.getCompanyPrefix());
                    LocalPreferences.storeStringPreference(getApplicationContext(),"Suffix",responseLogin.getCompanySuffix());

                    LocalPreferences.storeIntegerPreference(getApplicationContext(), "CompanyCode", responseLogin.getCompanyCode());
                    if (binding.rememberMeCheckBox.isChecked()) {
                        LocalPreferences.storeStringPreference(getApplicationContext(), "LogUser", binding.edEmail.getText().toString());
                        LocalPreferences.storeStringPreference(getApplicationContext(), "LogPassword", binding.edPassword.getText().toString());

                    }

                    LocalPreferences.storeBooleanPreference(getApplicationContext(), "showlogout", true);
                    List<KaratPrice> dataset = responseLogin.getKaratPriceList();
                    new Thread(() -> {
                        // do background stuff here
                        DatabaseClient.getInstance(LoginActivity.this).getAppDatabase().homeMenuDao().insertgoldrate(dataset);
                        runOnUiThread(() -> {
                            // OnPostExecute stuff here
                        });
                    }).start();

                    mViewModel.getHomeMenu(LocalPreferences.getToken(getApplicationContext()), AppConstants.HomeMenuAppName, String.valueOf(responseLogin.getRoleName()), getApplicationContext());


                } else {
                    hideProgressDialog();
                    customSnackBar(binding.parent, getResources().getString(R.string.autherror));
                }
            }
        });

    }

    private void LoginCustomer(String vaEmail, String vaPassword) {
        showProgressDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("applicationID", AppConstants.applicationID);
        jsonObject.addProperty("userName", vaEmail);
        jsonObject.addProperty("password", vaPassword);
        viewModel.Login(jsonObject, LoginActivity.this);
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