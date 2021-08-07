package com.acube.jims.Presentation.HomePage.View;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.HomePage.ViewModel.CustomerViewModel;
import com.acube.jims.Presentation.HomePage.ViewModel.HomeViewModel;
import com.acube.jims.Presentation.HomePage.adapter.CustomerListAdapter;
import com.acube.jims.Presentation.HomePage.adapter.HomeAdapter;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.HomeFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerListing;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

public class HomeFragment extends BaseFragment implements HomeAdapter.FragmentTransition {

    private HomeViewModel mViewModel;
    HomeFragmentBinding binding;
    CustomerViewModel customerViewModel;

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
    public void replaceFragment() {
        //  FragmentHelper.replaceFragment(getActivity(), R.id.content, new CatalogueFragment());
        LocalPreferences.storeStringPreference(getActivity(), "subcatid", "");
        LocalPreferences.storeStringPreference(getContext(), "colorid", "");
        showcustomerselectionDialog();


    }

    private void showcustomerselectionDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.layout_customer_dialog, null);
        final EditText etUsername = alertLayout.findViewById(R.id.ed_search);
        RecyclerView recyclerView = alertLayout.findViewById(R.id.recyvcustomerlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        customerViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseCustomerListing>>() {
            @Override
            public void onChanged(List<ResponseCustomerListing> responseCustomerListings) {
                if (responseCustomerListings != null) {
                    CustomerListAdapter adapter=new CustomerListAdapter(getActivity(),responseCustomerListings);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                }
            }
        });


        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
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
                    Toast.makeText(getActivity(), "call api", Toast.LENGTH_LONG).show();
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
}