package com.acube.jims.presentation.GuestHomePage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.acube.jims.presentation.Catalogue.View.GuestCatalogueFragment;
import com.acube.jims.presentation.HomePage.ViewModel.HomeViewModel;
import com.acube.jims.presentation.HomePage.adapter.ExpandableListAdapter;
import com.acube.jims.presentation.Login.View.LoginActivity;
import com.acube.jims.R;
import com.acube.jims.utils.FragmentHelper;
import com.acube.jims.databinding.ActivityHomePageBinding;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.HomePage.NavMenuModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GuestHomePageActivity extends AppCompatActivity {
    ActivityHomePageBinding binding;
    List<HomeData> dataset;
    HomeData homeData;
    ExpandableListAdapter expandableListAdapter;
    HomeViewModel viewModel;
    List<NavMenuModel> headerList = new ArrayList<>();
    HashMap<NavMenuModel, List<NavMenuModel>> childList = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page);
        init();
        prepareMenuData();
        replaceFragment(new GuestCatalogueFragment());

    }

    private void prepareMenuData() {
        NavMenuModel menuModel = new NavMenuModel("Home", true, true, R.drawable.ic_baseline_home_24); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        menuModel = new NavMenuModel("Settings", true, true, R.drawable.ic_icon_settings); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        List<NavMenuModel> childModelsList = new ArrayList<>();
        NavMenuModel childModel = new NavMenuModel("Server Settings", false, false, 0);
        childModelsList.add(childModel);
        childModel = new NavMenuModel("Field Settings", false, false, 0);
        childModelsList.add(childModel);
        childModel = new NavMenuModel("Power Settings", false, false, 0);
        childModelsList.add(childModel);
        childModel = new NavMenuModel("Device Settings", false, false, 0);
        childModelsList.add(childModel);
        if (menuModel.hasChildren) {
            Log.d("API123", "here");
            childList.put(menuModel, childModelsList);
        }
        menuModel = new NavMenuModel("About", true, false, R.drawable.ic_user); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }
        menuModel = new NavMenuModel("Logout", true, false, R.drawable.ic_sign_out); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }
    }

    public void init() {
        // binding.content.recyvhomemenu.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        //   binding.content.recyvhomemenu.setAdapter(new HomeAdapter(getApplicationContext(), setupHomeData()));

    }

 /*   public List<HomeData> setupHomeData() {
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

    }*/


    private void showLogoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GuestHomePageActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.logout_popup_custom, viewGroup, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        Button logout = dialogView.findViewById(R.id.signoutButton);
        Button cancel = dialogView.findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                onBackPressed();
            }
        });


    }



    public void replaceFragment(Fragment fragment) {
        FragmentHelper.replaceFragment(GuestHomePageActivity.this, R.id.content, fragment, "");
    }
}