package com.acube.jims.presentation.Consignment;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.databinding.ReportMenuBinding;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.presentation.Audit.AuditFragment;
import com.acube.jims.presentation.Audit.ZoneAssigning;
import com.acube.jims.presentation.Audit.adapter.AuditMenuAdapter;
import com.acube.jims.presentation.LocateProduct.View.LocateProduct;
import com.acube.jims.presentation.Report.ReportActivity;
import com.acube.jims.presentation.Report.ViewModel.ReportViewModel;
import com.acube.jims.utils.LocalPreferences;

import java.util.ArrayList;
import java.util.List;

public class Consignmentoptions extends BaseActivity implements AuditMenuAdapter.FragmentTransition {

    private ReportViewModel mViewModel;


    ReportMenuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.report_menu);
        initToolBar(binding.toolbarApp.toolbar, "Consignment", true);
        binding.recyvhomemenu.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        binding.recyvhomemenu.setHasFixedSize(true);
        binding.recyvhomemenu.setAdapter(new AuditMenuAdapter(getApplicationContext(), getList(), Consignmentoptions.this));
        mViewModel = new ViewModelProvider(this).get(ReportViewModel.class);


    }


    @Override
    public void replaceFragment(String menuname) {
        if (menuname.equalsIgnoreCase("Consignment Scan")) {
            startActivity(new Intent(getApplicationContext(), ConsignmentActivity.class));


        } else if (menuname.equalsIgnoreCase("Consignment Verification")) {
            startActivity(new Intent(getApplicationContext(), ConsignmentScanActivity.class));
            // FilterPreference.clearPreferences(getActivity());
        } else if (menuname.equalsIgnoreCase("Locate Product")) {
            // FragmentHelper.replaceFragment(getActivity(), R.id.content, new LocateProduct());
            startActivity(new Intent(getApplicationContext(), LocateProduct.class));

        } else if (menuname.equalsIgnoreCase("Assign Zone")) {
            // FragmentHelper.replaceFragment(getActivity(), R.id.content, new LocateProduct());
            startActivity(new Intent(getApplicationContext(), ZoneAssigning.class));

        }
    }

    public List<HomeData> getList() {
        List<HomeData> mMainCategory = new ArrayList<>();
        HomeData homeData = new HomeData();
        homeData.setMenuText("Consignment Scan");
        homeData.setMenuicon(R.drawable.barcode);
        HomeData homeData2 = new HomeData();
        homeData2.setMenuText("Consignment Verification");
        homeData2.setMenuicon(R.drawable.barcode);

        mMainCategory.add(homeData);
        mMainCategory.add(homeData2);
        return mMainCategory;
    }
}