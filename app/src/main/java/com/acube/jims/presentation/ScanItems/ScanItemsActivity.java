package com.acube.jims.presentation.ScanItems;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.acube.jims.BaseActivity;
import com.acube.jims.presentation.CartManagment.View.CartViewFragment;
import com.acube.jims.presentation.CartManagment.ViewModel.AddtoCartViewModel;
import com.acube.jims.presentation.Catalogue.adapter.CatalogItemAdapter;
import com.acube.jims.presentation.Compare.CompareFragment;
import com.acube.jims.presentation.PdfGeneration.ShareScannedItems;
import com.acube.jims.presentation.ProductDetails.View.ProductDetailsFragment;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ActivityScanItemsBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScanItemsActivity extends BaseActivity implements CatalogItemAdapter.Datalist {
    ActivityScanItemsBinding binding;
    ScanItemsViewModel viewmodel;
    String status, serialno, AuthToken, CartId, UserId;
    AddtoCartViewModel addtoCartViewModel;
    List<ResponseItems> dataset;
    List<String> comparelist;
    String jsonSerialNo;
    int GuestCustomerID;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(
                this, R.layout.activity_scan_items);

        viewmodel = ViewModelProviders.of(this).get(ScanItemsViewModel.class);
        initToolBar(binding.toolbarApp.toolbar, "Scan Items", true);
        viewmodel.init();
        addtoCartViewModel = new ViewModelProvider(this).get(AddtoCartViewModel.class);
        addtoCartViewModel.init();

        CartId = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.CartID);
        GuestCustomerID = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        UserId = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.UserID);
        if (getIntent() != null) {
            jsonSerialNo = getIntent().getStringExtra("jsonSerialNo");

        }


        StringBuilder result = new StringBuilder();

        try {
            JSONArray jsonArray = new JSONArray(jsonSerialNo);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject explrObject = jsonArray.getJSONObject(i);
                status = "1";
                serialno = explrObject.getString("SerialNo");
                result.append(serialno);
                result.append(",");

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("serialNumber", result.toString());
        AuthToken = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.Token);
        Log.d("onCreate", "onCreate: " + AuthToken);
        viewmodel.getcompareItems(AppConstants.Authorization + AuthToken, jsonObject);
        binding.recyvscanned.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        viewmodel.getLiveData().observe(this, new Observer<List<ResponseItems>>() {
            @Override
            public void onChanged(List<ResponseItems> responseItems) {
                if (responseItems != null) {
                    // binding.recyvscanned.setAdapter(new CatalogItemAdapter(getApplicationContext(), responseItems));
                    ResponseItems dataset;
                    for (int i = 0; i < responseItems.size(); i++) {
                        dataset = new ResponseItems();
                        dataset.setSerialNumber(responseItems.get(i).getSerialNumber());
                        dataset.setGrossWeight(responseItems.get(i).getGrossWeight());
                        dataset.setItemName(responseItems.get(i).getItemName());
                        dataset.setMrp(responseItems.get(i).getMrp());
                        dataset.setStoneWeight(responseItems.get(i).getStoneWeight());
                        dataset.setKaratCode(responseItems.get(i).getKaratCode());
                        dataset.setItemID(responseItems.get(i).getItemID());
                        dataset.setKaratName(responseItems.get(i).getKaratName());
                        dataset.setImagePath(responseItems.get(i).getImagePath());

                        SaveItems(dataset);

                    }

                    try {
                        JSONArray jsonArray = new JSONArray(jsonSerialNo);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject explrObject = jsonArray.getJSONObject(i);
                            status = "1";
                            serialno = explrObject.getString("SerialNo");
                            UpdateStatus(serialno, status);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        binding.layoutSmarttool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);

            }
        });


        addtoCartViewModel.getLiveData().observe(this, new Observer<ResponseAddtoCart>() {
            @Override
            public void onChanged(ResponseAddtoCart responseAddtoCart) {
                if (responseAddtoCart != null) {
                    showsuccess("Item added to cart successfully");
                    LocalPreferences.storeStringPreference(getApplicationContext(), AppConstants.CartID, responseAddtoCart.getCartListNo());
                    startActivity(new Intent(getApplicationContext(), CartViewFragment.class));
                }
            }
        });

    }

    private void UpdateStatus(String serialno, String status) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase().scannedItemsDao().update(status, serialno);
                getItems();
            }
        });
    }

    private void SaveItems(ResponseItems items) {
        class SavePlan extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .scannedItemsDao()
                        .insert(items);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }

        SavePlan st = new SavePlan();
        st.execute();
    }

    private void getItems() {
        class GetTasks extends AsyncTask<Void, Void, List<ResponseItems>> {

            @Override
            protected List<ResponseItems> doInBackground(Void... voids) {
                List<ResponseItems> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .scannedItemsDao().getAllbystatus("1");
                return taskList;
            }

            @Override
            protected void onPostExecute(List<ResponseItems> responseItems) {
                super.onPostExecute(responseItems);
                binding.recyvscanned.setAdapter(new CatalogItemAdapter(getApplicationContext(), responseItems, ScanItemsActivity.this));

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    public void showPopupWindow(final View view) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.pop_up_layout_smarttoolfromscan, null);
        CardView addtocart = alertLayout.findViewById(R.id.cdvaddtocart);
        CardView compare = alertLayout.findViewById(R.id.cdvcompare);
        CardView cdvshare = alertLayout.findViewById(R.id.cdvshare);


        //  final TextInputEditText etPassword = alertLayout.findViewById(R.id.tiet_password);


        AlertDialog.Builder alert = new AlertDialog.Builder(ScanItemsActivity.this);
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataset.size() >= 2) {
                    comparelist = new ArrayList<>();
                    for (int i = 0; i < dataset.size(); i++) {
                        comparelist.add(dataset.get(i).getSerialNumber());
                    }
                    setList("compare", comparelist);
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), CompareFragment.class));
                } else {
                    showerror("Please select one or more items for compare");

                }


            }
        });

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonObject items = null;
                JsonArray jsonArray = new JsonArray();
                for (int i = 0; i < dataset.size(); i++) {
                    items = new JsonObject();
                    items.addProperty("cartListNo", CartId);
                    items.addProperty("customerID", GuestCustomerID);
                    items.addProperty("employeeID", UserId);
                    items.addProperty("serialNumber", dataset.get(i).getSerialNumber());
                    items.addProperty("itemID", dataset.get(i).getItemID());
                    items.addProperty("qty", 0);
                    jsonArray.add(items);
                }
                addtoCartViewModel.AddtoCart(AppConstants.Authorization + AuthToken, "add", jsonArray);
                dialog.dismiss();
            }
        });

        cdvshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataset != null && dataset.size() != 0) {
                    comparelist = new ArrayList<>();
                    for (int i = 0; i < dataset.size(); i++) {
                        comparelist.add(dataset.get(i).getSerialNumber());
                    }
                    setList("compare", comparelist);
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), ShareScannedItems.class));
                } else {
                    showerror("Please select one or more items for sharing");

                }

                dialog.dismiss();
            }
        });

    }

    public void Addtocart() {
      /*  JsonObject items = new JsonObject();

        items.addProperty("cartListNo", CartId);
        items.addProperty("customerID", GuestCustomerID);
        items.addProperty("employeeID", UserId);
        items.addProperty("serialNumber", mSerialno);
        items.addProperty("itemID", Id);
        items.addProperty("qty", 0);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(items);*/
    }

    @Override
    public void datalist(List<ResponseItems> comparelist) {
        dataset = new ArrayList<>();
        dataset = comparelist;



     /*   JsonObject items ;
        for (int i=0;i<comparelist.size();i++){
            items = new JsonObject();
            items.addProperty("cartListNo", comparelist.get(i).CartId);
            items.addProperty("customerID", GuestCustomerID);
            items.addProperty("employeeID", UserId);
            items.addProperty("serialNumber", mSerialno);
            items.addProperty("itemID", Id);
            items.addProperty("qty", 0);
        }

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(items);*/
    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(getApplicationContext(), key, json);
    }

    @Override
    public void replace(String Id) {

        startActivity(new Intent(getApplicationContext(), ProductDetailsFragment.class).putExtra("Id", Id));

    }
}