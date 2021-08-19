package com.acube.jims.Presentation.HomePage.View;

import static android.app.Activity.RESULT_OK;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
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
import android.widget.Toast;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.Catalogue.View.CatalogueFragment;
import com.acube.jims.Presentation.Catalogue.View.FilterBottomSheetFragment;
import com.acube.jims.Presentation.CustomerManagment.View.CustomerBottomSheetFragment;
import com.acube.jims.Presentation.CustomerManagment.View.CustomerSearch;
import com.acube.jims.Presentation.GuestHomePage.GuestHomePageActivity;
import com.acube.jims.Presentation.HomePage.ViewModel.CustomerViewModel;
import com.acube.jims.Presentation.HomePage.ViewModel.HomeViewModel;
import com.acube.jims.Presentation.HomePage.adapter.CustomerListAdapter;
import com.acube.jims.Presentation.HomePage.adapter.HomeAdapter;
import com.acube.jims.Presentation.Login.ViewModel.CreateCustomerViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.FilterPreference;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.HomeFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerListing;
import com.acube.jims.datalayer.models.HomePage.HomeData;
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

        binding.recyvhomemenu.setLayoutManager(new GridLayoutManager(getActivity(), 3));
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
    public void replaceFragment(int pos) {


        if (pos == 3) {
            FragmentHelper.replaceFragment(getActivity(), R.id.content, new CatalogueFragment());
            FilterPreference.clearPreferences(getActivity());

        } else if (pos == 8) {
            FragmentHelper.replaceFragment(getActivity(), R.id.content, new CustomerBottomSheetFragment());

        }


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
                FragmentHelper.replaceFragment(getActivity(), R.id.content, new CatalogueFragment());
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
        FragmentHelper.replaceFragment(getActivity(), R.id.content, new CatalogueFragment());
    }
}