package com.acube.jims.Presentation.HomePage.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ExpandableListView;

import com.acube.jims.Presentation.HomePage.adapter.ExpandableListAdapter;
import com.acube.jims.Presentation.HomePage.adapter.HomeAdapter;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityHomePageBinding;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.HomePage.NavMenuModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    ActivityHomePageBinding binding;
    List<HomeData> dataset;
    HomeData homeData;
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<NavMenuModel> headerList = new ArrayList<>();
    HashMap<NavMenuModel, List<NavMenuModel>> childList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page);
        init();
        prepareMenuData();
        binding.toolbar.optionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    private void prepareMenuData() {
        NavMenuModel menuModel = new NavMenuModel("Settings", true, true, R.drawable.ic_icon_settings); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        List<NavMenuModel> childModelsList = new ArrayList<>();
        NavMenuModel childModel = new NavMenuModel("Server Settings", false, false, 0);
        childModelsList.add(childModel);
        childModel = new NavMenuModel("Field Settings", false, false, 0);
        childModelsList.add(childModel);
        childModel = new NavMenuModel("Power Settings", false, false, 0);
        childModelsList.add(childModel);
        if (menuModel.hasChildren) {
            Log.d("API123", "here");
            childList.put(menuModel, childModelsList);
        }
        menuModel = new NavMenuModel("About", true, false, R.drawable.ic_icon_settings); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }
        menuModel = new NavMenuModel("Logout", true, false, R.drawable.ic_icon_settings); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }
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
    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(HomePageActivity.this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {
                        /*WebView webView = findViewById(R.id.webView);
                        webView.loadUrl(headerList.get(groupPosition).url);*/
                        onBackPressed();
                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    NavMenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    if (model.url.length() > 0) {
                       /* WebView webView = findViewById(R.id.webView);
                        webView.loadUrl(model.url);*/
                        onBackPressed();
                    }
                }

                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}