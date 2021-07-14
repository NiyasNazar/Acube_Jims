package com.acube.jims.Presentation.HomePage.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.WindowManager;

import com.acube.jims.Presentation.HomePage.adapter.HomeAdapter;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityHomePageBinding;
import com.acube.jims.datalayer.models.HomePage.HomeData;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    ActivityHomePageBinding binding;
    List<HomeData> dataset;
    HomeData homeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page);
        init();
    }

    public void init() {
        binding.recyvhomemenu.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        binding.recyvhomemenu.setAdapter(new HomeAdapter(getApplicationContext(), setupHomeData()));

    }

    public List<HomeData> setupHomeData() {
        dataset = new ArrayList<>();
        homeData = new HomeData("DASHBOARD", R.drawable.ic_dashboard);
        dataset.add(homeData);
        homeData = new HomeData("SCAN ITEM", R.drawable.ic_scan);
        dataset.add(homeData);
        homeData = new HomeData("CATALOGUE", R.drawable.ic_catalogue);
        dataset.add(homeData);
        homeData = new HomeData("INVENTORY", R.drawable.ic_inventory);
        dataset.add(homeData);
        homeData = new HomeData("ITEM REQUEST", R.drawable.ic_itemrequest);
        dataset.add(homeData);
        homeData = new HomeData("ANALYTICS", R.drawable.ic_analytics);
        dataset.add(homeData);
        return dataset;

    }
}