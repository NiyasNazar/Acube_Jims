package com.acube.jims.Presentation.CustomerManagment.View;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.CartManagment.ViewModel.CartViewModel;
import com.acube.jims.Presentation.CustomerManagment.ViewModel.CustomerHistoryViewModel;
import com.acube.jims.Presentation.CustomerManagment.ViewModel.CustomerLogoutViewModel;
import com.acube.jims.Presentation.HomePage.View.HomeFragment;
import com.acube.jims.Presentation.HomePage.ViewModel.CustomerViewModel;
import com.acube.jims.Presentation.CustomerManagment.adapter.CustomerListAdapter;
import com.acube.jims.Presentation.CustomerManagment.ViewModel.CreateCustomerViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.BottomSheetCustomerBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.acube.jims.datalayer.models.Cart.ResponseCart;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerListing;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.datalayer.remote.dbmodel.CustomerHistory;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerBottomSheetFragment extends BaseFragment implements CustomerListAdapter.ReplaceFragment {
    BottomSheetCustomerBinding binding;
    BottomSheetBehavior bottomSheetBehavior;
    private CreateCustomerViewModel createCustomerViewModel;
    CustomerViewModel customerViewModel;
    String AuthToken;
    LinearLayoutManager linearLayoutManager;
    CustomerListAdapter adapter;
    List<ResponseCustomerListing> dataset;
    private CartViewModel mViewModel;
    CustomerLogoutViewModel customerLogoutViewModel;
    String Starttime;
    CustomerHistoryViewModel customerHistoryViewModel;

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.bottom_sheet_customer, container, false);
        AuthToken = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token);


        customerLogoutViewModel = new ViewModelProvider(this).get(CustomerLogoutViewModel.class);
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        customerHistoryViewModel = new ViewModelProvider(this).get(CustomerHistoryViewModel.class);
        customerHistoryViewModel.init();
        customerViewModel.init();
        customerLogoutViewModel.init();
        createCustomerViewModel = ViewModelProviders.of(this).get(CreateCustomerViewModel.class);
        createCustomerViewModel.init();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyvcustomerlist.setLayoutManager(linearLayoutManager);
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        mViewModel.init();

        String GuestCustomerID = LocalPreferences.retrieveStringPreferences(getActivity(), "GuestCustomerID");
        String Customername = LocalPreferences.retrieveStringPreferences(getContext(), "GuestCustomerName");
        String CustomerCode = LocalPreferences.retrieveStringPreferences(getContext(), "GuestCustomerCode");
        Starttime = LocalPreferences.retrieveStringPreferences(getActivity(), "CustomerSessionStartTime");

        customerHistoryViewModel.CustomerHistory(LocalPreferences.getToken(getActivity()), GuestCustomerID);
        if (CustomerCode.equalsIgnoreCase("")) {
            binding.laytCustomerdetails.setVisibility(View.GONE);
            binding.parent.setVisibility(View.VISIBLE);
        } else {
            binding.laytCustomerdetails.setVisibility(View.VISIBLE);
            binding.tvLoggedcustomername.setText("Customer Name: " + Customername);
            binding.tvLoggedcustomercode.setText("Customer Code: " + CustomerCode);
            binding.tvLoggedtime.setText("Time & Date: " + Starttime);
            binding.parent.setVisibility(View.GONE);
        }

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();


            }
        });
        customerLogoutViewModel.getCustomerLiveData().observe(getActivity(), new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                if (jsonObject!=null){
                    FragmentHelper.replaceFragment(getActivity(), R.id.content, new CustomerBottomSheetFragment());
                }
            }
        });

        binding.btnAddcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentHelper.replaceFragment(getActivity(), R.id.content, new AddCustomerFragment());


                // startActivity(new Intent(getActivity(), CustomerSearch.class));
            }
        });
        createCustomerViewModel.getCustomerLiveData().observe(getActivity(), new Observer<ResponseCreateCustomer>() {
            @Override
            public void onChanged(ResponseCreateCustomer responseCreateCustomer) {
                hideProgressDialog();
                if (responseCreateCustomer != null) {
                    LocalPreferences.storeStringPreference(getContext(), "GuestCustomerName", responseCreateCustomer.getCustomerName());
                    LocalPreferences.storeStringPreference(getContext(), "GuestCustomerCode", responseCreateCustomer.getCustomerCode());
                    LocalPreferences.storeStringPreference(getContext(), "GuestCustomerID", String.valueOf(responseCreateCustomer.getId()));
                    FragmentHelper.replaceFragment(getActivity(), R.id.content, new HomeFragment());

                } else {


                }
            }
        });
        binding.btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalPreferences.storeStringPreference(getContext(), "GuestCustomerName", "");
                LocalPreferences.storeStringPreference(getContext(), "GuestCustomerCode", "");
                LocalPreferences.storeStringPreference(getContext(), "GuestCustomerID", "");

                FragmentHelper.replaceFragment(getActivity(), R.id.content, new HomeFragment());

            }
        });

      /*  DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyvcustomerlist.getContext(),
                linearLayoutManager.getOrientation());
        binding.recyvcustomerlist.addItemDecoration(dividerItemDecoration);*/
        binding.btbguestlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGuestCustomer("", "", "");
            }
        });
        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //  Toast.makeText(getActivity(),"exe", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Toast.makeText(getActivity(),"before text change", Toast.LENGTH_LONG).show();

            }

            @Override
            public void afterTextChanged(Editable keyword) {
                if (keyword.toString().length() > 3) {
                    customerViewModel.getCustomerSearch(AppConstants.Authorization + AuthToken, keyword.toString());
                } else if (keyword.toString().length() <= 3) {
                    binding.recyvcustomerlist.setVisibility(View.GONE);

                    // binding.tvNodatafound.setVisibility(View.VISIBLE);
                }


            }
        });
        mViewModel.getLiveData().observe(getActivity(), new Observer<ResponseCart>() {
            @Override
            public void onChanged(ResponseCart responseCart) {
                if (responseCart != null) {
                    LocalPreferences.storeStringPreference(getActivity(), AppConstants.CartID, responseCart.getCartListNo());
                    FragmentHelper.replaceFragment(getActivity(), R.id.content, new CustomerViewfragment());
                } else {
                    LocalPreferences.storeStringPreference(getActivity(), AppConstants.CartID, "");
                    FragmentHelper.replaceFragment(getActivity(), R.id.content, new CustomerViewfragment());
                }
            }
        });
        customerViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseCustomerListing>>() {
            @Override
            public void onChanged(List<ResponseCustomerListing> responseCustomerListings) {
                dataset = new ArrayList<>();
                dataset = responseCustomerListings;
                if (dataset != null && dataset.size() != 0) {
                    binding.recyvcustomerlist.setVisibility(View.VISIBLE);
                    binding.tvNodatafound.setVisibility(View.GONE);
                    adapter = new CustomerListAdapter(getActivity(), dataset, CustomerBottomSheetFragment.this::replacefragments);
                    binding.recyvcustomerlist.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                } else {
                    adapter = new CustomerListAdapter(getActivity(), dataset, CustomerBottomSheetFragment.this::replacefragments);
                    binding.recyvcustomerlist.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    binding.tvNodatafound.setVisibility(View.VISIBLE);

                }
            }
        });

        return binding.getRoot();
    }

 /*   @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dia) {
                BottomSheetDialog dialog = (BottomSheetDialog) dia;
                FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
                BottomSheetBehavior.from(bottomSheet).setHideable(true);
            }
        });
        return bottomSheetDialog;
    }*/

    private void createGuestCustomer(String vamobile, String vaemail, String vaguestname) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("customerName", vaguestname);
        jsonObject.addProperty("emailID", vaemail);
        jsonObject.addProperty("contactNumber", vamobile);
        createCustomerViewModel.CreateCustomer(AppConstants.Authorization + LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token), jsonObject);

    }

    @Override
    public void replacefragments() {
        mViewModel.ViewCart(AppConstants.Authorization + AuthToken, LocalPreferences.retrieveStringPreferences(getActivity(), "GuestCustomerID"));

        //LocalPreferences.retrieveStringPreferences(getActivity(), "GuestCustomerID");



    }

    public void showDialog() {
        FancyAlertDialog.Builder
                .with(getActivity())
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
        String GuestCustomerID = LocalPreferences.retrieveStringPreferences(getActivity(), "GuestCustomerID");
        Log.d("onPostExecute", "onPostExecute: " + GuestCustomerID);

        class GetTasks extends AsyncTask<Void, Void, List<CustomerHistory>> {
            @Override
            protected List<CustomerHistory> doInBackground(Void... voids) {
                List<CustomerHistory> taskList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .customerHistoryDao().getAll(GuestCustomerID);
                return taskList;
            }

            @Override
            protected void onPostExecute(List<CustomerHistory> responseItems) {
                super.onPostExecute(responseItems);
            String  warehouseId=  LocalPreferences.retrieveStringPreferences(getActivity(),"warehouseId");
            String employeeID=    LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.UserID);

                Log.d("onPostExecute", "onPostExecute: " + responseItems.size());
                JSONArray itemViewLIst = new JSONArray();
                JSONObject jsonObject = null;
                for (int i = 0; i < responseItems.size(); i++) {
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("customerID", responseItems.get(i).getCustomerID());
                        jsonObject.put("itemID", responseItems.get(i).getItemID());
                        jsonObject.put("serialNumber",responseItems.get(i).getSerialNo());
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

                    customerLogoutViewModel.CustomerLogout(LocalPreferences.getToken(getActivity()), gsonObject);
                    LocalPreferences.removePreferences(getContext(), "GuestCustomerName");
                    LocalPreferences.removePreferences(getContext(), "GuestCustomerCode");
                    LocalPreferences.removePreferences(getContext(), "GuestCustomerID");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }


}

