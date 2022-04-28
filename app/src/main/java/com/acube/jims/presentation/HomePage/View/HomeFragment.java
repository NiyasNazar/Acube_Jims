package com.acube.jims.presentation.HomePage.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.acube.jims.BaseFragment;
import com.acube.jims.presentation.Analytics.AnalyticsActivity;
import com.acube.jims.presentation.Audit.AuditMenuFragment;
import com.acube.jims.presentation.Catalogue.View.CatalogueActivity;
import com.acube.jims.presentation.Catalogue.View.CatalogueSummaryActivity;
import com.acube.jims.presentation.CustomerManagment.View.CustomerBottomSheetFragment;
import com.acube.jims.presentation.CustomerManagment.View.CustomerViewfragment;
import com.acube.jims.presentation.DashBoard.DashBoardActivity;
import com.acube.jims.presentation.DeviceRegistration.View.DeviceRegistrationFragment;
import com.acube.jims.presentation.HomePage.ViewModel.CustomerViewModel;
import com.acube.jims.presentation.HomePage.ViewModel.HomeViewModel;
import com.acube.jims.presentation.CustomerManagment.adapter.CustomerListAdapter;
import com.acube.jims.presentation.HomePage.adapter.HomeAdapter;
import com.acube.jims.presentation.CustomerManagment.ViewModel.CreateCustomerViewModel;
import com.acube.jims.presentation.ItemRequest.ItemRequestActivity;
import com.acube.jims.presentation.ItemRequest.view.SalesmanItemRequestActivity;
import com.acube.jims.presentation.ScanItems.ScanItemsActivity;
import com.acube.jims.R;
import com.acube.jims.Utils.FilterPreference;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.HomeFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerListing;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.List;

public class HomeFragment extends BaseFragment implements HomeAdapter.FragmentTransition, CustomerListAdapter.ReplaceFragment {

    private HomeViewModel mViewModel;
    HomeFragmentBinding binding;
    CustomerViewModel customerViewModel;
    private CreateCustomerViewModel createCustomerViewModel;
    AlertDialog dialog;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    String AuthToken;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.home_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        customerViewModel.init();
        createCustomerViewModel = ViewModelProviders.of(this).get(CreateCustomerViewModel.class);
        createCustomerViewModel.init();
        LocalPreferences.storeBooleanPreference(getActivity(), "showlogout", false);

        binding.recyvhomemenu.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        binding.recyvhomemenu.setHasFixedSize(true);
        binding.tvgoldrate.setText("Gold Rate " + LocalPreferences.retrieveStringPreferences(getActivity(), "GoldRate"));

        binding.recyvhomemenu.setAdapter(new HomeAdapter(getActivity(), getList(), HomeFragment.this::replaceFragment));

        AuthToken = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token);

      /*  mViewModel.init();
        mViewModel.getHomeMenu(AppConstants.Authorization + LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token), AppConstants.HomeMenuAppName, LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.UserRole));
        mViewModel.getLiveData().observe(getActivity(), new Observer<List<HomeData>>() {
            @Override
            public void onChanged(List<HomeData> homeData) {
                if (homeData != null)
                   // binding.recyvhomemenu.setAdapter(new HomeAdapter(getActivity(), homeData, HomeFragment.this::replaceFragment));
            }
        });*/

        return binding.getRoot();

    }


    @Override
    public void replaceFragment(String value) {


        if (value.equalsIgnoreCase("ItemCatalog")) {
            //FragmentHelper.replaceFragment(getActivity(), R.id.content, new CatalogueFragment());
            startActivity(new Intent(requireActivity(), CatalogueSummaryActivity.class));

            FilterPreference.clearPreferences(getActivity());

        } else if (value.equalsIgnoreCase("Customer")) {

            String CustomerCode = LocalPreferences.retrieveStringPreferences(getActivity(), "GuestCustomerCode");

            if (CustomerCode.equalsIgnoreCase("")) {
                startActivity(new Intent(getActivity(), CustomerBottomSheetFragment.class));
            } else {
                startActivity(new Intent(getActivity(), CustomerViewfragment.class));
            }

        } else if (value.equalsIgnoreCase("Scan")) {
            //  startActivity(new Intent(getActivity(),));
            PerformScan();


        } else if (value.equalsIgnoreCase("InventoryAudit")) {
            //  startActivity(new Intent(getActivity(),));
            FragmentHelper.replaceFragment(getActivity(), R.id.content, new AuditMenuFragment());


        } else if (value.equalsIgnoreCase("AndroidDashboard")) {
            startActivity(new Intent(getActivity(), DashBoardActivity.class));


        } else if (value.equalsIgnoreCase("Analytics")) {
            startActivity(new Intent(getActivity(), AnalyticsActivity.class));

        } else if (value.equalsIgnoreCase("ItemRequest")) {
            FilterPreference.clearPreferences(requireActivity());

            int customerid = LocalPreferences.retrieveIntegerPreferences(getActivity(), "GuestCustomerID");
            if (customerid == 0) {
                startActivity(new Intent(getActivity(), SalesmanItemRequestActivity.class));
            } else {
                startActivity(new Intent(getActivity(), ItemRequestActivity.class));

            }

        }else if(value.equalsIgnoreCase("deviceregistration")){
            startActivity(new Intent(getActivity(), DeviceRegistrationFragment.class));

        }


    }

    private void PerformScan() {
        class SavePlan extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .scannedItemsDao()
                        .delete();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                String TrayMacAddress = LocalPreferences.retrieveStringPreferences(getActivity(), "TrayMacAddress");
                Log.d("TrayMacAddress", "onPostExecute: " + TrayMacAddress);
                super.onPostExecute(aVoid);
                try {
                    Log.d("TrayMacAddress", "onPostExecute: " + TrayMacAddress);
                    Intent res = new Intent();
                 //   String mPackage = "com.acube.smarttray";// package name
                 //   String mClass = ".SmartTrayReading";//the activity name which return results*/
                    String mPackage = "com.example.acubetest";// package name
                    String mClass = ".MainActivity";//the activity name which return results
                    res.putExtra("token", LocalPreferences.getToken(getActivity()));
                    res.putExtra("url", AppConstants.BASE_URL);
                    res.putExtra("macAddress", TrayMacAddress);
                    res.putExtra("jsonSerialNo", "json");
                    res.setComponent(new ComponentName(mPackage, mPackage + mClass));
                    someActivityResultLauncher.launch(res);

                } catch (Exception e) {

                    Log.d(TAG, "replaceFragment: " + e.getMessage());
                }
            }
        }

        SavePlan st = new SavePlan();
        st.execute();
    }

    private void showcustomerselectionDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_customer_dialog, null);
        final EditText etUsername = alertLayout.findViewById(R.id.ed_search);
        AppCompatButton btn_skip = alertLayout.findViewById(R.id.btn_skip);

        RecyclerView recyclerView = alertLayout.findViewById(R.id.recyvcustomerlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        customerViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseCustomerListing>>() {
            @Override
            public void onChanged(List<ResponseCustomerListing> responseCustomerListings) {
                if (responseCustomerListings != null) {
                    CustomerListAdapter adapter = new CustomerListAdapter(getActivity(), responseCustomerListings, HomeFragment.this::replacefragments);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                }
            }
        });

        createCustomerViewModel.getCustomerLiveData().observe(this, new Observer<ResponseCreateCustomer>() {
            @Override
            public void onChanged(ResponseCreateCustomer responseCreateCustomer) {
                hideProgressDialog();
                //FragmentHelper.replaceFragment(getActivity(), R.id.content, new CatalogueFragment());
                startActivity(new Intent(requireActivity(), CatalogueActivity.class));

            }
        });
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);

        dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                createGuestCustomer("", "", "");

            }
        });
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //  Toast.makeText(getActivity(),"exe", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Toast.makeText(getActivity(),"before text change", Toast.LENGTH_LONG).show();

            }

            @Override
            public void afterTextChanged(Editable keyword) {
                if (keyword.toString().length() > 3) {
                    customerViewModel.getCustomerSearch(AppConstants.Authorization + AuthToken, keyword.toString());
                }


            }
        });

    }

    public List<HomeData> getList() {
        List<HomeData> mMainCategory = null;
        String serializedObject = LocalPreferences.retrieveStringPreferences(getActivity(), "HomeMenu");
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<HomeData>>() {
            }.getType();
            mMainCategory = gson.fromJson(serializedObject, type);
        }
        return mMainCategory;
    }

    private void createGuestCustomer(String vamobile, String vaemail, String vaguestname) {
        showProgressDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("customerName", vaguestname);
        jsonObject.addProperty("emailID", vaemail);
        jsonObject.addProperty("contactNumber", vamobile);
        createCustomerViewModel.CreateCustomer(AppConstants.Authorization + LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token), jsonObject);

    }


    @Override
    public void replacefragments() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        //  FragmentHelper.replaceFragment(getActivity(), R.id.content, new CatalogueFragment());
        startActivity(new Intent(requireActivity(), CatalogueActivity.class));

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        Intent data = result.getData();
                        if (data != null) {
                            String json = data.getStringExtra("jsonSerialNo");
                            startActivity(new Intent(getActivity(), ScanItemsActivity.class).putExtra("jsonSerialNo", json));
                        }
                        Log.d("onActivityResult", "onActivityResult: " + data.getStringExtra("jsonSerialNo"));
                        //doSomeOperations();
                    }
                }
            });
}