package com.acube.jims.presentation.HomePage.View;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acube.jims.BaseActivity;
import com.acube.jims.datalayer.models.Invoice.KaratPrice;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.CartManagment.View.CartViewFragment;
import com.acube.jims.presentation.CustomerManagment.View.CustomerSearch;
import com.acube.jims.presentation.Favorites.View.Favorites;
import com.acube.jims.presentation.HomePage.ViewModel.HomeViewModel;
import com.acube.jims.presentation.HomePage.adapter.ExpandableListAdapter;
import com.acube.jims.presentation.HomePage.adapter.GoldRateDapter;
import com.acube.jims.presentation.Login.View.LoginActivity;
import com.acube.jims.presentation.ProductDetails.View.ProductDetailsFragment;
import com.acube.jims.R;
import com.acube.jims.utils.FragmentHelper;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ActivityHomePageBinding;
import com.acube.jims.datalayer.constants.BackHandler;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.HomePage.NavMenuModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomePageActivity extends BaseActivity implements ProductDetailsFragment.BackHandler, BackHandler, CustomerSearch.ReplacefromCustomerLogin, CartViewFragment.BackHandler, Favorites.BackHandler {
    ActivityHomePageBinding binding;
    List<HomeData> dataset;
    HomeData homeData;
    ExpandableListAdapter expandableListAdapter;
    HomeViewModel viewModel;
    List<NavMenuModel> headerList = new ArrayList<>();
    HashMap<NavMenuModel, List<NavMenuModel>> childList = new HashMap<>();
    PopupWindow mypopupWindow;
    Dialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_page);
        myDialog = new Dialog(HomePageActivity.this);

      /*  binding.toolbar.imvprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new CustomerBottomSheetFragment());
            }
        });*/
        initToolBar(binding.toolbarApp.toolbar, "", false);
        replaceFragment(new HomeFragment());

        init();
        prepareMenuData();
    binding.toolbarApp.pricetag.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ShowPopup(v);

        }
    });



        binding.toolbarApp.imvcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcart();

                //  FragmentHelper.replaceFragment(HomePageActivity.this, R.id.content, new CartViewFragment());
            }
        });

        binding.toolbarApp.favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showfavorites();
            }
        });

        String Customername = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "GuestCustomerName");
        String CustomerCode = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "GuestCustomerCode");


    }
    public void ShowPopup(View v) {
        RecyclerView recyclerView;
        Button btnFollow;
        TextView txtclose;
        myDialog.setContentView(R.layout.goldpopup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);

        recyclerView =(RecyclerView) myDialog.findViewById(R.id.recyvgoldrate);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DatabaseClient.getInstance(HomePageActivity.this).getAppDatabase().homeMenuDao().getGoldRate().observe(this, new Observer<List<KaratPrice>>() {
            @Override
            public void onChanged(List<KaratPrice> karatPrices) {
                recyclerView.setAdapter(new GoldRateDapter(getApplicationContext(),karatPrices));

            }
        });

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    private void setPopUpWindow() {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.filter_layout, null);
        ImageView morecat = view.findViewById(R.id.more_cat);
        ImageView morekarat = view.findViewById(R.id.imvkaratmore);
        ImageView morecolor = view.findViewById(R.id.imvmorecolor);
        Button btncancel = view.findViewById(R.id.btn_cancel);
        Button btnapply = view.findViewById(R.id.btn_apply);


        //  expandableListDetail = ExpandableListDataPump.getData();
        //  expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        //   expandableListAdapter = new FilterListAdapter(getActivity(), expandableListTitle, expandableListDetail);
        // expandableListView.setAdapter(expandableListAdapter);
        // List<String> dataset = new ArrayList<>();
        //  dataset.add("Category");
        //  dataset.add("Color");
        //  dataset.add("Karat");
        //  dataset.add("Shape");
        //  dataset.add("Certified by");
        //  expandableListView.setAdapter(new FilterParentAdapter(getActivity(), dataset));

        mypopupWindow = new PopupWindow(view, 700, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        mypopupWindow.setTouchable(true);
        mypopupWindow.setFocusable(false);
        mypopupWindow.setOutsideTouchable(false);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*  binding.parent.getForeground().setAlpha(0);*/
                mypopupWindow.dismiss();
            }
        });
        btnapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    binding.parent.getForeground().setAlpha(0);
                // LoadFirstPage();
                mypopupWindow.dismiss();
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


    public void replaceFragment(Fragment fragment) {
        FragmentHelper.replaceFragment(HomePageActivity.this, R.id.content, fragment, "");
    }

    @Override
    public void backpress() {
        onBackPressed();
    }

    @Override
    public void replacecatalogfragment() {

    }
}