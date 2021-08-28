package com.acube.jims.Presentation.HomePage.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.acube.jims.Presentation.CartManagment.View.CartViewFragment;
import com.acube.jims.Presentation.CustomerManagment.View.CustomerBottomSheetFragment;
import com.acube.jims.Presentation.CustomerManagment.View.CustomerSearch;
import com.acube.jims.Presentation.DeviceRegistration.View.DeviceRegistrationFragment;
import com.acube.jims.Presentation.Favorites.View.FavoritesFragment;
import com.acube.jims.Presentation.HomePage.ViewModel.HomeViewModel;
import com.acube.jims.Presentation.HomePage.adapter.ExpandableListAdapter;
import com.acube.jims.Presentation.Login.View.LoginActivity;
import com.acube.jims.Presentation.ProductDetails.View.ProductDetailsFragment;
import com.acube.jims.R;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.databinding.ActivityHomePageBinding;
import com.acube.jims.datalayer.constants.BackHandler;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.HomePage.NavMenuModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements ProductDetailsFragment.BackHandler, BackHandler, CustomerSearch.ReplacefromCustomerLogin,CartViewFragment.BackHandler,FavoritesFragment.BackHandler {
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
        populateExpandableList();
        replaceFragment(new CustomerBottomSheetFragment());
        binding.toolbar.imvcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentHelper.replaceFragment(HomePageActivity.this, R.id.content, new CartViewFragment());
            }
        });
        binding.toolbar.optionMenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if (binding.drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    binding.drawerLayout.closeDrawer(Gravity.RIGHT);

                } else {
                    binding.drawerLayout.openDrawer(Gravity.RIGHT);

                }
            }
        });
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
        menuModel = new NavMenuModel("Favorites", true, false, R.drawable.ic_baseline_favorite_24); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
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

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(HomePageActivity.this, headerList, childList);
        binding.expandableListView.setAdapter(expandableListAdapter);

        binding.expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {


                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {
                        if (headerList.get(groupPosition).menuName.equalsIgnoreCase("Logout")) {

                            showLogoutAlert();
                        } else if (headerList.get(groupPosition).menuName.equalsIgnoreCase("Home")) {
                            replaceFragment(new HomeFragment());
                        } else if (headerList.get(groupPosition).menuName.equalsIgnoreCase("Favorites")) {
                            FragmentHelper.replaceFragment(HomePageActivity.this, R.id.content, new FavoritesFragment());
                        }
                        onBackPressed();
                    }
                }

                return false;
            }
        });

        binding.expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    NavMenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    if (model.menuName.equalsIgnoreCase("Device Settings")) {


                        replaceFragment(new DeviceRegistrationFragment());
                    }
                    onBackPressed();

                }

                return false;
            }
        });
    }

    private void showLogoutAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.content);
        if (f instanceof HomeFragment) {


        }
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            Log.d("onBackPressed", ":onBackPressed" + getFragmentManager().getBackStackEntryCount());

        } else {
            super.onBackPressed();
            Log.d("onBackPressed", ":onBackPressedsuper " + getFragmentManager().getBackStackEntryCount());
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentHelper.replaceFragment(HomePageActivity.this, R.id.content, fragment);
    }

    @Override
    public void backpress() {
        onBackPressed();
    }

    @Override
    public void replacecatalogfragment() {

    }
}