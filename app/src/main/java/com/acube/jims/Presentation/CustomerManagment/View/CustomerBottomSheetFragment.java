package com.acube.jims.Presentation.CustomerManagment.View;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.Catalogue.View.CatalogueFragment;
import com.acube.jims.Presentation.Catalogue.adapter.FilterMasterAdapter;
import com.acube.jims.Presentation.Filters.View.CategoryFilterFragment;
import com.acube.jims.Presentation.Filters.View.ColorFilterFragment;
import com.acube.jims.Presentation.Filters.View.KaratFragment;
import com.acube.jims.Presentation.HomePage.View.HomeFragment;
import com.acube.jims.Presentation.HomePage.ViewModel.CustomerViewModel;
import com.acube.jims.Presentation.HomePage.adapter.CustomerListAdapter;
import com.acube.jims.Presentation.Login.ViewModel.CreateCustomerViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.BottomSheetCustomerBinding;
import com.acube.jims.databinding.BottomSheetFilterBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerListing;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomerBottomSheetFragment extends BaseFragment implements CustomerListAdapter.ReplaceFragment {
    BottomSheetCustomerBinding binding;
    BottomSheetBehavior bottomSheetBehavior;
    private CreateCustomerViewModel createCustomerViewModel;
    CustomerViewModel customerViewModel;
    String AuthToken;
    LinearLayoutManager linearLayoutManager;
    CustomerListAdapter adapter;
    List<ResponseCustomerListing> dataset;

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.bottom_sheet_customer, container, false);
        AuthToken = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token);


        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        customerViewModel.init();
        createCustomerViewModel = ViewModelProviders.of(this).get(CreateCustomerViewModel.class);
        createCustomerViewModel.init();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyvcustomerlist.setLayoutManager(linearLayoutManager);

        binding.btnAddcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentHelper.replaceFragment(getActivity(), R.id.content, new AddCustomerFragment());


                // startActivity(new Intent(getActivity(), CustomerSearch.class));
            }
        });
        createCustomerViewModel.getCustomerLiveData().observe(getActivity(), new Observer<ResponseCreateCustomer>() {
            @Override
            public void onChanged(ResponseCreateCustomer responseCreateCustomer) {
                hideProgressDialog();
                if (responseCreateCustomer != null) {
                    LocalPreferences.storeStringPreference(getContext(), "GuestCustomerName", responseCreateCustomer.getCustomerName());
                    LocalPreferences.storeStringPreference(getContext(), "GuestCustomerCode", responseCreateCustomer.getCustomerCode());
                    LocalPreferences.storeStringPreference(getContext(), "GuestCustomerID", String.valueOf(responseCreateCustomer.getId()));
                    FragmentHelper.replaceFragment(getActivity(), R.id.content, new CatalogueFragment());

                } else {


                }
            }
        });
        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalPreferences.storeStringPreference(getContext(), "GuestCustomerName", "");
                LocalPreferences.storeStringPreference(getContext(), "GuestCustomerCode", "");
                LocalPreferences.storeStringPreference(getContext(), "GuestCustomerID","");

                replacefragments();

            }
        });

      /*  DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyvcustomerlist.getContext(),
                linearLayoutManager.getOrientation());
        binding.recyvcustomerlist.addItemDecoration(dividerItemDecoration);*/
        binding.btbguestlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGuestCustomer("", "", "");
            }
        });
        binding.edSearch.addTextChangedListener(new TextWatcher() {
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
                } else if (keyword.toString().length() <= 3) {
                    binding.recyvcustomerlist.setVisibility(View.GONE);

                    // binding.tvNodatafound.setVisibility(View.VISIBLE);
                }


            }
        });
        customerViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseCustomerListing>>() {
            @Override
            public void onChanged(List<ResponseCustomerListing> responseCustomerListings) {
                dataset = new ArrayList<>();
                dataset=responseCustomerListings;
                if (dataset != null && dataset.size() != 0) {
                    binding.recyvcustomerlist.setVisibility(View.VISIBLE);
                    binding.tvNodatafound.setVisibility(View.GONE);
                    adapter = new CustomerListAdapter(getActivity(), dataset, CustomerBottomSheetFragment.this::replacefragments);
                    binding.recyvcustomerlist.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                } else {
                    adapter = new CustomerListAdapter(getActivity(), dataset, CustomerBottomSheetFragment.this::replacefragments);
                    binding.recyvcustomerlist.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    binding.tvNodatafound.setVisibility(View.VISIBLE);

                }
            }
        });

        return binding.getRoot();
    }

 /*   @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dia) {
                BottomSheetDialog dialog = (BottomSheetDialog) dia;
                FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
                BottomSheetBehavior.from(bottomSheet).setHideable(true);
            }
        });
        return bottomSheetDialog;
    }*/

    private void createGuestCustomer(String vamobile, String vaemail, String vaguestname) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("customerName", vaguestname);
        jsonObject.addProperty("emailID", vaemail);
        jsonObject.addProperty("contactNumber", vamobile);
        createCustomerViewModel.CreateCustomer(AppConstants.Authorization + LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token), jsonObject);

    }

    @Override
    public void replacefragments() {
        FragmentHelper.replaceFragment(getActivity(), R.id.content, new CatalogueFragment());
    }


}

