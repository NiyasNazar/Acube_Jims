package com.acube.jims.presentation.Audit;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.acube.jims.BaseActivity;
import com.acube.jims.presentation.Audit.adapter.AuditMenuAdapter;
import com.acube.jims.presentation.LocateProduct.View.LocateProduct;
import com.acube.jims.presentation.Report.ReportFragment;
import com.acube.jims.presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ReportMenuBinding;
import com.acube.jims.datalayer.models.HomePage.HomeData;

import java.util.ArrayList;
import java.util.List;

public class AuditMenuFragment extends BaseActivity implements AuditMenuAdapter.FragmentTransition {

    private ReportViewModel mViewModel;


    ReportMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.report_menu);
        initToolBar(binding.toolbarApp.toolbar, "Inventory Audit", true);
        binding.recyvhomemenu.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        binding.recyvhomemenu.setHasFixedSize(true);
        binding.recyvhomemenu.setAdapter(new AuditMenuAdapter(getApplicationContext(), getList(), AuditMenuFragment.this));
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);


    }


    @Override
    public void replaceFragment(String menuname) {
        if (menuname.equalsIgnoreCase("Audit")) {
            startActivity(new Intent(getApplicationContext(), AuditFragment.class));
            LocalPreferences.storeIntegerPreference(getApplicationContext(), "totalstock", 0);
            LocalPreferences.storeIntegerPreference(getApplicationContext(), "missing", 0);
            LocalPreferences.storeIntegerPreference(getApplicationContext(), "found", 0);
            LocalPreferences.storeIntegerPreference(getApplicationContext(), "locationmismatch", 0);
            LocalPreferences.storeStringPreference(getApplicationContext(), "auditID", "");

        } else if (menuname.equalsIgnoreCase("Report")) {
            startActivity(new Intent(getApplicationContext(), ReportFragment.class));
            // FilterPreference.clearPreferences(getActivity());
        } else if (menuname.equalsIgnoreCase("Locate Product")) {
            // FragmentHelper.replaceFragment(getActivity(), R.id.content, new LocateProduct());
            startActivity(new Intent(getApplicationContext(), LocateProduct.class));

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