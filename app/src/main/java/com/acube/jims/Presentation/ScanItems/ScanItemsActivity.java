package com.acube.jims.Presentation.ScanItems;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.acube.jims.BaseActivity;
import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.CartManagment.ViewModel.AddtoCartViewModel;
import com.acube.jims.Presentation.Catalogue.adapter.CatalogItemAdapter;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.ActivityScanItemsBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class ScanItemsActivity extends BaseFragment implements CatalogItemAdapter.Datalist {
    ActivityScanItemsBinding binding;
    ScanItemsViewModel viewmodel;
    String status, serialno, AuthToken, CartId, GuestCustomerID, UserId;
    AddtoCartViewModel addtoCartViewModel;
    List<ResponseItems> dataset;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(
                inflater, R.layout.activity_scan_items, container, false);

        viewmodel = ViewModelProviders.of(this).get(ScanItemsViewModel.class);
        viewmodel.init();
        addtoCartViewModel = new ViewModelProvider(this).get(AddtoCartViewModel.class);
        addtoCartViewModel.init();
        binding.toolbar.tvFragname.setText("Scan Items");
        CartId = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.CartID);
        GuestCustomerID = LocalPreferences.retrieveStringPreferences(getActivity(), "GuestCustomerID");
        UserId = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.UserID);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("serialNumber", "10001,10002,202100000008,202100000009");
        AuthToken = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token);
        Log.d("onCreate", "onCreate: " + AuthToken);
        viewmodel.getcompareItems(AppConstants.Authorization + AuthToken, jsonObject);
        binding.recyvscanned.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        viewmodel.getLiveData().observe(getActivity(), new Observer<List<ResponseItems>>() {
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
                        dataset.setItemID(responseItems.get(i).getItemID());
                        dataset.setImagePath(responseItems.get(i).getImagePath());

                        SaveItems(dataset);

                    }
                    String resultjson = "[{\"Status\": \"1\",\"SerialNo\": \"10001\"},{\"Status\": \"1\",\"SerialNo\": \"10002\"},{\"Status\": \"1\",\"SerialNo\": \"202100000008\"},{\"Status\": \"1\",\"SerialNo\": \"202100000009\"}]";

                    try {
                        JSONArray jsonArray = new JSONArray(resultjson);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject explrObject = jsonArray.getJSONObject(i);
                            status = explrObject.getString("Status");
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


  /*  JSONObject Products = null;
    JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < datasetdb.size(); i++) {
        Long tsLongid = System.currentTimeMillis() / 1000;
        String tsid = tsLongid.toString();
        String uniqueIDid = UUID.randomUUID().toString();
        String offlineIDid = tsid + uniqueIDid;
        Products = new JSONObject();
        Products.put("offline_id", offlineIDid);
        Products.put("products_id", datasetdb.get(i).getPid());
        Products.put("quantity", datasetdb.get(i).getQuantity());
        Products.put("pname", datasetdb.get(i).getPname());
        Log.d("jsons", "Load: " + datasetdb.get(i).getPname());
        jsonArray.put(Products);
        ///  json += "{ \"products_id\": " + datasetdb.get(i).getPid() + " ,\"quantity\": " + datasetdb.get(i).getQuantity() + "}";
        //  if (i != datasetdb.size() - 1) json += ",";*/
        return binding.getRoot();
    }

    private void UpdateStatus(String serialno, String status) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DatabaseClient
                        .getInstance(getActivity())
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
                        .getInstance(getActivity())
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
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .scannedItemsDao().getAllbystatus("1");
                return taskList;
            }

            @Override
            protected void onPostExecute(List<ResponseItems> responseItems) {
                super.onPostExecute(responseItems);
                binding.recyvscanned.setAdapter(new CatalogItemAdapter(getActivity(), responseItems, ScanItemsActivity.this::datalist));

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    public void showPopupWindow(final View view) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.pop_up_layout_smarttool, null);
        CardView addtocart = alertLayout.findViewById(R.id.cdvaddtocart);
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
            }
        });


        //  final TextInputEditText etPassword = alertLayout.findViewById(R.id.tiet_password);


        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


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
}