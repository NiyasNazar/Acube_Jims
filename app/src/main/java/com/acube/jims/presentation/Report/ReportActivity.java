package com.acube.jims.presentation.Report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityReportBinding;
import com.acube.jims.datalayer.api.ResponseAuditReport;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Audit.ResponseLiveStore;
import com.acube.jims.datalayer.models.Audit.ResponseReportList;
import com.acube.jims.datalayer.models.Audit.Store;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.datalayer.remote.db.LocalAudit;
import com.acube.jims.presentation.Audit.AuditFragment;
import com.acube.jims.presentation.Audit.AuditScanActivity;
import com.acube.jims.presentation.Audit.adapter.AuditAdapter;
import com.acube.jims.presentation.Audit.adapter.AuditSummaryReportAdapter;
import com.acube.jims.presentation.Report.adapter.GraphAuditItemReportAdapter;
import com.acube.jims.presentation.Report.adapter.GraphReportCount;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.utils.OnSingleClickListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends BaseActivity {
    ActivityReportBinding binding;
    String AuidtFilter = "NA";
    int systemLocationID = 0;
    int categoryId = 0;
    int storeID = 0;
    int itemID = 0;
    GraphAuditItemReportAdapter adapter;
    List<ResponseLiveStore> storesdatset;
    private int year, month, day;
    private DatePicker datePicker;
    Calendar calendar;
    String fromdate, todate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report);
        initToolBar(binding.toolbarApp.toolbar, "Stock Check Report", true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);


        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Store Report"));
        tabLayout.addTab(tabLayout.newTab().setText("Category Report"));
        tabLayout.addTab(tabLayout.newTab().setText("Zone Report"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final SampleFragmentPagerAdapter adapter = new SampleFragmentPagerAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}