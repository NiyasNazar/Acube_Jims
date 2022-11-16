package com.acube.jims.presentation.CustomerManagment.View;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;

import com.acube.jims.BaseActivity;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.datalayer.remote.dbmodel.CustomerHistory;
import com.acube.jims.presentation.CustomerManagment.ViewModel.CustomerHistoryViewModel;
import com.acube.jims.presentation.CustomerManagment.ViewModel.CustomerLogoutViewModel;
import com.acube.jims.presentation.CustomerManagment.adapter.LastViewedAdapter;
import com.acube.jims.presentation.CustomerManagment.adapter.SalesHistoryAdapter;
import com.acube.jims.presentation.HomePage.View.HomePageActivity;
import com.acube.jims.presentation.ProductDetails.View.ProductDetailsFragment;
import com.acube.jims.R;
import com.acube.jims.utils.FragmentHelper;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.FragmentCustomerViewfragmentBinding;
import com.acube.jims.datalayer.models.CustomerManagment.ItemViewHistory;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerHistory;
import com.acube.jims.datalayer.models.CustomerManagment.SalesHistory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CustomerViewfragment extends BaseActivity implements LastViewedAdapter.ReplaceFragment, SalesHistoryAdapter.ReplaceFragment {


    FragmentCustomerViewfragmentBinding binding;
    CustomerHistoryViewModel customerHistoryViewModel;
    String GuestCustomerName, GuestCustomerCode, CustomerSessionStartTime, CustomerMobile;
    int GuestCustomerID;
    String Starttime;
    CustomerLogoutViewModel customerLogoutViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(
                this, R.layout.fragment_customer_viewfragment);
        initToolBar(binding.toolbarApp.toolbar, "Customer History", true);
        customerHistoryViewModel = ViewModelProviders.of(this).get(CustomerHistoryViewModel.class);
        customerHistoryViewModel.init();
        customerLogoutViewModel = new ViewModelProvider(this).get(CustomerLogoutViewModel.class);
        customerLogoutViewModel.init();
        customerLogoutViewModel.getCustomerLiveData().observe(this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                hideProgressDialog();
                if (jsonObject != null) {
                   startActivity(new Intent(getApplicationContext(),CustomerBottomSheetFragment.class));
                   finish();
                }
            }
        });

        binding.btnSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCustomer();

            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });


        binding.recyvvieweditems.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyvsaleshistory.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        Starttime = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "CustomerSessionStartTime");

        GuestCustomerID = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        showProgressDialog();
        customerHistoryViewModel.CustomerHistory(LocalPreferences.getToken(getApplicationContext()), GuestCustomerID,getApplicationContext());
        customerHistoryViewModel.getCustomerLiveData().observe(this, new Observer<ResponseCustomerHistory>() {
            @Override
            public void onChanged(ResponseCustomerHistory responseCustomerHistory) {
                hideProgressDialog();
                if (responseCustomerHistory != null) {

                    GuestCustomerName = responseCustomerHistory.getCustomerName();
                    GuestCustomerCode = responseCustomerHistory.getCustomerCode();
                    CustomerMobile = responseCustomerHistory.getCustomerContactNo();
                    Log.d("onChanged", "onChanged: " + GuestCustomerName);
                    binding.tvCustomername.setText(GuestCustomerName);
                    binding.tvcustomercode.setText(responseCustomerHistory.getCustomerCode());
                    binding.tvcustomercontact.setText(responseCustomerHistory.getCustomerContactNo());
                    if (responseCustomerHistory.getLastVisit() != null) {
                        binding.tvlastvisit.setText(ParseDate(responseCustomerHistory.getLastVisit()));

                    }
                    binding.tvcustomeremail.setText(responseCustomerHistory.getCustomerEmailID());
                    binding.tvtotalvisit.setText("" + responseCustomerHistory.getTotalVisit());
                    binding.tvtoalpurchase.setText("" + responseCustomerHistory.getTotalPurchase());
                    List<ItemViewHistory> itemViewHistory = responseCustomerHistory.getItemViewHistory();
                    List<SalesHistory> datasetsaleshistory = responseCustomerHistory.getSaleHistory();
                    if (datasetsaleshistory != null && datasetsaleshistory.size() != 0) {
                        binding.recyvsaleshistory.setAdapter(new SalesHistoryAdapter(getApplicationContext(), datasetsaleshistory, CustomerViewfragment.this));
                        binding.laytsaleshistory.setVisibility(View.VISIBLE);
                    } else {
                        binding.laytsaleshistory.setVisibility(View.GONE);
                    }
                    binding.recyvvieweditems.setAdapter(new LastViewedAdapter(getApplicationContext(), itemViewHistory, CustomerViewfragment.this));


                }

            }
        });

        binding.viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), CustomerViewedItemsFragment.class));
            }
        });
        binding.viewAllsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CustomerSalesHistoryFragment.class));
            }
        });


    }

    public void SelectCustomer() {
        LocalPreferences.storeBooleanPreference(getApplicationContext(), "salesman", false);

        Date todaysdate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String starttime = format.format(todaysdate);
        System.out.println(starttime);
        LocalPreferences.storeStringPreference(getApplicationContext(), "GuestCustomerName", GuestCustomerName);
        LocalPreferences.storeStringPreference(getApplicationContext(), "GuestCustomerCode", GuestCustomerCode);
        LocalPreferences.storeIntegerPreference(getApplicationContext(), "GuestCustomerID", GuestCustomerID);
        LocalPreferences.storeStringPreference(getApplicationContext(), "CustomerSessionStartTime", starttime);
        LocalPreferences.storeStringPreference(getApplicationContext(), "CustomerMobile", CustomerMobile);
        //  FragmentHelper.replaceFragment(CustomerViewfragment.this, R.id.content, new HomeFragment(), "");
        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
    }

    @Override
    public void replacefragments(String Id) {
        startActivity(new Intent(getApplicationContext(), ProductDetailsFragment.class).putExtra("Id", Id));
    }


    public String ParseDate(String lastvisitdate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(lastvisitdate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        return formattedDate;
    }

    public void showDialog() {
        FancyAlertDialog.Builder
                .with(CustomerViewfragment.this)
                .setTitle("Logout Customer")
                .setBackgroundColorRes(R.color.appmaincolor)  // for @ColorRes use setBackgroundColorRes(R.color.colorvalue)
                .setMessage("Do you really want to Logout customer ?")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackgroundRes(R.color.appmaincolor)  // for @ColorRes use setPositiveBtnBackgroundRes(R.color.colorvalue)
                .setPositiveBtnText("Yes")
                .setNegativeBtnBackground(Color.parseColor("#f64e60"))  // for @ColorRes use setNegativeBtnBackgroundRes(R.color.colorvalue)
                .setAnimation(Animation.POP)
                .isCancellable(true)

                .onPositiveClicked(dialog -> LogoutExistingCustomer())
                .onNegativeClicked(dialog -> dialog.dismiss())
                .build()
                .show();
    }

    private void LogoutExistingCustomer() {

        FetchCustomerHistory();

    }
    private void FetchCustomerHistory() {
        showProgressDialog();
        int GuestCustomerID = LocalPreferences.retrieveIntegerPreferences(CustomerViewfragment.this, "GuestCustomerID");
        Log.d("onPostExecute", "onPostExecute: " + GuestCustomerID);

        class GetTasks extends AsyncTask<Void, Void, List<CustomerHistory>> {
            @Override
            protected List<CustomerHistory> doInBackground(Void... voids) {
                List<CustomerHistory> taskList = DatabaseClient
                        .getInstance(CustomerViewfragment.this)
                        .getAppDatabase()
                        .customerHistoryDao().getAll(String.valueOf(GuestCustomerID));
                return taskList;
            }

            @Override
            protected void onPostExecute(List<CustomerHistory> responseItems) {
                super.onPostExecute(responseItems);
                String warehouseId = LocalPreferences.retrieveStringPreferences(CustomerViewfragment.this, "warehouseId");
                String employeeID = LocalPreferences.retrieveStringPreferences(CustomerViewfragment.this, AppConstants.UserID);

                Log.d("onPostExecute", "onPostExecute: " + responseItems.size());
                JSONArray itemViewLIst = new JSONArray();
                JSONObject jsonObject = null;
                for (int i = 0; i < responseItems.size(); i++) {
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("customerID", responseItems.get(i).getCustomerID());
                        jsonObject.put("itemID", responseItems.get(i).getItemID());
                        jsonObject.put("serialNumber", responseItems.get(i).getSerialNo());
                        jsonObject.put("trayID", 3);
                        jsonObject.put("employeeID", employeeID);
                        jsonObject.put("WarehouseID", warehouseId);
                        itemViewLIst.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                JSONObject jsonObject1 = new JSONObject();
                try {
                    jsonObject1.put("employeeID", employeeID);
                    jsonObject1.put("customerID", GuestCustomerID);
                    jsonObject1.put("startTime", Starttime);
                    jsonObject1.put("trayID", 3);
                    jsonObject1.put("WarehouseID", warehouseId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject parent = new JSONObject();
                try {
                    parent.put("staffEngagementData", jsonObject1);
                    parent.put("itemViewList", itemViewLIst);
                    JsonParser jsonParser = new JsonParser();
                    JsonObject gsonObject = new JsonObject();
                    gsonObject = (JsonObject) jsonParser.parse(parent.toString());

                    Log.d("gsonObject", "onPostExecute: " + gsonObject);

                    customerLogoutViewModel.CustomerLogout(LocalPreferences.getToken(CustomerViewfragment.this), gsonObject,CustomerViewfragment.this);
                    LocalPreferences.removePreferences(CustomerViewfragment.this, "GuestCustomerName");
                    LocalPreferences.removePreferences(CustomerViewfragment.this, "GuestCustomerCode");
                    LocalPreferences.storeIntegerPreference(CustomerViewfragment.this, "GuestCustomerID",0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

}