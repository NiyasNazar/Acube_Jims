package com.acube.jims.presentation.Favorites.View;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;

import com.acube.jims.BaseActivity;
import com.acube.jims.presentation.Compare.CompareFragment;
import com.acube.jims.presentation.Favorites.ViewModel.AddtoFavoritesViewModel;
import com.acube.jims.presentation.Favorites.ViewModel.FavoritesViewModel;
import com.acube.jims.presentation.Favorites.adapter.FavoritesItemAdapter;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.FragmentFavoritesBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Favorites.ResponseFavorites;
import com.acube.jims.presentation.PdfGeneration.ShareScannedItems;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.List;


public class Favorites extends BaseActivity implements FavoritesItemAdapter.DeleteProduct, FavoritesItemAdapter.Comaparelist {


    FragmentFavoritesBinding binding;
    FavoritesViewModel favoritesViewModel;
    AddtoFavoritesViewModel addtoFavoritesViewModel;
    String AuthToken;
    List<String> compareitemlist;
    FavoritesItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment

        binding = DataBindingUtil.setContentView(
                this, R.layout.fragment_favorites);

        binding.recycartitems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initToolBar(binding.toolbarApp.toolbar, "Favorites", true);


        favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        addtoFavoritesViewModel = new ViewModelProvider(this).get(AddtoFavoritesViewModel.class);
        favoritesViewModel.init();
        addtoFavoritesViewModel.init();
        showProgressDialog();
        int customerId = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        AuthToken = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.Token);
        favoritesViewModel.ViewCart(AppConstants.Authorization + AuthToken, String.valueOf(customerId));
        favoritesViewModel.getLiveData().observe(this, new Observer<List<ResponseFavorites>>() {
            @Override
            public void onChanged(List<ResponseFavorites> responseFavorites) {
                hideProgressDialog();
                if (responseFavorites != null) {
                    adapter = new FavoritesItemAdapter(getApplicationContext(), responseFavorites, Favorites.this, Favorites.this::compareitems);
                    binding.recycartitems.setAdapter(adapter);

                }

            }
        });

        binding.btnSelectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btnSelectall.getText().toString().equalsIgnoreCase("Select all")) {
                    adapter.selectAll();
                    binding.btnSelectall.setText("Unselect all");
                } else {
                    adapter.unselectall();
                    binding.btnSelectall.setText("select all");
                }


            }
        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showPopupWindow(v);

            }
        });
        addtoFavoritesViewModel.getLiveData().observe(this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                hideProgressDialog();

                favoritesViewModel.ViewCart(AppConstants.Authorization + AuthToken, String.valueOf(customerId));
            }
        });


    }

    @Override
    public void removefromcart(String itemid, String serialno) {
        showProgressDialog();
        String UserId = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.UserID);
        int customerId = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        addtoFavoritesViewModel.AddtoFavorites(AppConstants.Authorization + AuthToken, String.valueOf(customerId), UserId, String.valueOf(itemid), "delete", "", serialno);

    }

    @Override
    public void compareitems(List<String> comparelist) {

        compareitemlist = comparelist;

    }

    public interface BackHandler {
        void backpress();
    }

    public List<String> getList(String name) {
        List<String> mMainCategory = null;
        String serializedObject = LocalPreferences.retrieveStringPreferences(getApplicationContext(), name);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            mMainCategory = gson.fromJson(serializedObject, type);
        }
        return mMainCategory;
    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(getApplicationContext(), key, json);
    }

    public void showPopupWindow(final View view) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.pop_up_layout_smarttoolfromscan, null);
        CardView addtocart = alertLayout.findViewById(R.id.cdvaddtocart);
        addtocart.setVisibility(View.GONE);
        CardView compare = alertLayout.findViewById(R.id.cdvcompare);
        CardView cdvshare = alertLayout.findViewById(R.id.cdvshare);


        //  final TextInputEditText etPassword = alertLayout.findViewById(R.id.tiet_password);


        AlertDialog.Builder alert = new AlertDialog.Builder(Favorites.this);
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (compareitemlist!=null&&compareitemlist.size() >= 2) {
                    setList("compare", compareitemlist);
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), CompareFragment.class));
                } else {
                    showerror("Please select one or more items for compare");

                }


            }
        });



        cdvshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (compareitemlist!=null&&compareitemlist.size()!=0) {

                    setList("compare", compareitemlist);
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), ShareScannedItems.class));
                } else {
                    showerror("Please select one or more items for sharing");

                }

                dialog.dismiss();
            }
        });

    }

}