package com.acube.jims.Presentation.Audit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.acube.jims.Presentation.Audit.adapter.AuditMenuAdapter;
import com.acube.jims.Presentation.Catalogue.View.CatalogueFragment;
import com.acube.jims.Presentation.CustomerManagment.View.CustomerBottomSheetFragment;
import com.acube.jims.Presentation.HomePage.View.HomeFragment;
import com.acube.jims.Presentation.HomePage.adapter.HomeAdapter;
import com.acube.jims.Presentation.LocateProduct.View.LocateProduct;
import com.acube.jims.Presentation.Report.ReportFragment;
import com.acube.jims.Presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.FilterPreference;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.ReportMenuBinding;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AuditMenuFragment extends Fragment implements AuditMenuAdapter.FragmentTransition {

    private ReportViewModel mViewModel;

    public static AuditMenuFragment newInstance() {
        return new AuditMenuFragment();
    }

    ReportMenuBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.report_menu, container, false);
        binding.toolbar.tvFragname.setText("Inventory Audit");
        binding.recyvhomemenu.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        binding.recyvhomemenu.setHasFixedSize(true);
        binding.recyvhomemenu.setAdapter(new AuditMenuAdapter(getActivity(), getList(), AuditMenuFragment.this));

        binding.toolbar.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);


        return binding.getRoot();

    }


    @Override
    public void replaceFragment(String menuname) {
        if (menuname.equalsIgnoreCase("Audit")) {
            FragmentHelper.replaceFragment(getActivity(), R.id.content, new AuditFragment());
            LocalPreferences.storeIntegerPreference(getActivity(), "totalstock", 0);
            LocalPreferences.storeIntegerPreference(getActivity(), "missing", 0);
            LocalPreferences.storeIntegerPreference(getActivity(), "found", 0);
            LocalPreferences.storeIntegerPreference(getActivity(), "locationmismatch", 0);
            LocalPreferences.storeStringPreference(getActivity(), "auditID", "");

        } else if (menuname.equalsIgnoreCase("Report")) {
            FragmentHelper.replaceFragment(getActivity(), R.id.content, new ReportFragment());
            FilterPreference.clearPreferences(getActivity());
        }else if (menuname.equalsIgnoreCase("Locate Product")) {
            FragmentHelper.replaceFragment(getActivity(), R.id.content, new LocateProduct());

        }
    }

    public List<HomeData> getList() {
        List<HomeData> mMainCategory = new ArrayList<>();
        HomeData homeData = new HomeData();
        homeData.setMenuText("Audit");
        homeData.setMenuicon(R.drawable.audit);
        HomeData homeData2 = new HomeData();
        homeData2.setMenuText("Report");
        homeData2.setMenuicon(R.drawable.report);
        HomeData homeData3 = new HomeData();
        homeData3.setMenuText("Locate Product");
        homeData3.setMenuicon(R.drawable.locateproduct);
        mMainCategory.add(homeData);
        mMainCategory.add(homeData2);
        mMainCategory.add(homeData3);

        return mMainCategory;
    }
}