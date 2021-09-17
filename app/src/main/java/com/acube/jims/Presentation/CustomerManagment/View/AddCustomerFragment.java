package com.acube.jims.Presentation.CustomerManagment.View;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.Catalogue.View.CatalogueFragment;
import com.acube.jims.Presentation.CustomerManagment.ViewModel.CreateCustomerViewModel;
import com.acube.jims.Presentation.HomePage.View.HomeFragment;
import com.acube.jims.R;
import com.acube.jims.Utils.AppUtility;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.FragmentAddCustomerBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.constants.BackHandler;
import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.google.gson.JsonObject;


public class AddCustomerFragment extends BaseFragment {
    BackHandler backHandler;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddCustomerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            backHandler = (BackHandler) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FragmentAddCustomerBinding binding;
    private CreateCustomerViewModel createCustomerViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_customer, container, false);
        createCustomerViewModel = ViewModelProviders.of(this).get(CreateCustomerViewModel.class);
        createCustomerViewModel.init();
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backHandler.backpress();
            }
        });
        binding.imvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             backHandler.backpress();
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
                } else if (!new AppUtility(getActivity()).isValidMail(vaemail)) {
                    binding.edEmail.setError("Invalid Email");
                } else if (vaguestname.equalsIgnoreCase("")) {
                    binding.edGuestname.setError("Field Empty");
                } else if (new AppUtility(getActivity()).checkInternet()) {
                    createGuestCustomer(vamobile, vaemail, vaguestname);
                } else {
                  //  customSnackBar(binding.parent, getResources().getString(R.string.no_internet));
                }
            }


        });
        createCustomerViewModel.getCustomerLiveData().observe(getActivity(), new Observer<ResponseCreateCustomer>() {
            @Override
            public void onChanged(ResponseCreateCustomer responseCreateCustomer) {
                hideProgressDialog();
                if (responseCreateCustomer != null) {
                    LocalPreferences.storeStringPreference(getContext(),"GuestCustomerName",responseCreateCustomer.getCustomerName());
                    LocalPreferences.storeStringPreference(getContext(),"GuestCustomerCode",responseCreateCustomer.getCustomerCode());
                    LocalPreferences.storeStringPreference(getContext(), "GuestCustomerID", String.valueOf(responseCreateCustomer.getId()));

                    FragmentHelper.replaceFragment(getActivity(), R.id.content, new HomeFragment());
                } else {


                }
            }
        });
        return binding.getRoot();


    }
    private void createGuestCustomer(String vamobile, String vaemail, String vaguestname) {
        showProgressDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("customerName", vaguestname);
        jsonObject.addProperty("emailID", vaemail);
        jsonObject.addProperty("contactNumber", vamobile);
        createCustomerViewModel.CreateCustomer(AppConstants.Authorization + LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token), jsonObject);

    }
    public void ReplaceFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}