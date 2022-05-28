package com.acube.jims.presentation.CartManagment.View;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.acube.jims.BaseActivity;
import com.acube.jims.presentation.CartManagment.ViewModel.AddtoCartViewModel;
import com.acube.jims.presentation.CartManagment.ViewModel.CartViewModel;
import com.acube.jims.presentation.CartManagment.adapter.CartItemAdapter;
import com.acube.jims.presentation.PdfGeneration.ShareItemsScreen;
import com.acube.jims.presentation.Quotation.InvoiceFragment;
import com.acube.jims.presentation.Quotation.SaleFragment;
import com.acube.jims.R;
import com.acube.jims.utils.FragmentHelper;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.CartViewFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Cart.CartDetail;
import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.models.Cart.ResponseCart;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class CartViewFragment extends BaseActivity implements CartItemAdapter.UpdateQuantity, CartItemAdapter.DeleteProduct {

    private CartViewModel mViewModel;
    CartViewFragmentBinding binding;
    String AuthToken;
    AddtoCartViewModel addtoCartViewModel;
    String EmployeeID;
    String CartId;
    String CustomerID;
    BackHandler backHandler;
    List<CartDetail> dataset;


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.cart_view_fragment);
        dataset = new ArrayList<>();
        binding.recycartitems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        initToolBar(binding.toolbars.toolbar, "Cart", true);

        Delete();
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        mViewModel.init();


        AuthToken = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.Token);
        int customerId = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        addtoCartViewModel = new ViewModelProvider(this).get(AddtoCartViewModel.class);
        addtoCartViewModel.init();
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);

            }
        });

        mViewModel.ViewCart(AppConstants.Authorization + AuthToken, String.valueOf(customerId));
        mViewModel.getLiveData().observe(this, new Observer<ResponseCart>() {
            @Override
            public void onChanged(ResponseCart responseCart) {
                if (responseCart != null) {

                    CustomerID = String.valueOf(responseCart.getCustomerID());
                    EmployeeID = String.valueOf(responseCart.getEmployeeID());
                    CartId = responseCart.getCartListNo();
                    dataset = responseCart.getCartDetails();
                    setList("cartitem", dataset);
                    Log.d("onChangedss", "onChanged: " + dataset.size());
                    binding.recycartitems.setAdapter(new CartItemAdapter(getApplicationContext(), dataset, CartViewFragment.this, CartViewFragment.this));
                    if (dataset != null && dataset.isEmpty()) {
                        binding.emptycart.setVisibility(View.VISIBLE);
                        binding.parentRelative.setVisibility(View.GONE);
                        binding.bottomlayt.setVisibility(View.GONE);
                    } else {
                        binding.emptycart.setVisibility(View.GONE);
                        binding.parentRelative.setVisibility(View.VISIBLE);
                        binding.bottomlayt.setVisibility(View.VISIBLE);
                    }

                } else {
                    binding.emptycart.setVisibility(View.VISIBLE);
                    binding.parentRelative.setVisibility(View.GONE);
                    binding.bottomlayt.setVisibility(View.GONE);
                }
            }
        });

        addtoCartViewModel.getLiveData().observe(this, new Observer<ResponseAddtoCart>() {
            @Override
            public void onChanged(ResponseAddtoCart responseAddtoCart) {
                hideProgressDialog();
                if (responseAddtoCart != null) {
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    mViewModel.ViewCart(AppConstants.Authorization + AuthToken, String.valueOf(customerId));
                    // LocalPreferences.storeStringPreference(getActivity(), AppConstants.CartID,responseAddtoCart.getCartListNo());
                } else {
                    Log.d(TAG, "onChangedsiz: ");
                }
            }
        });
    }


    @Override
    public void updatevalue(String itemid, String quantity) {
        showProgressDialog();
        // addtoCartViewModel.AddtoCart(CartId, AppConstants.Authorization + AuthToken, CustomerID, EmployeeID, itemid, "edit", quantity);


    }

    public void showPopupWindow(final View view) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.pop_up_layout_invoice, null);
        CardView checkout = alertLayout.findViewById(R.id.cdvcheckout);
        CardView cdvquote = alertLayout.findViewById(R.id.cdvcreatequote);
        CardView cdvshare = alertLayout.findViewById(R.id.cdvshare);


        //  final TextInputEditText etPassword = alertLayout.findViewById(R.id.tiet_password);


        AlertDialog.Builder alert = new AlertDialog.Builder(CartViewFragment.this);
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FragmentHelper.replaceFragment(CartViewFragment.this, R.id.parent, new SaleFragment());
            }
        });
        cdvquote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.replaceFragment(CartViewFragment.this, R.id.parent, new InvoiceFragment());
                dialog.dismiss();
            }
        });
        cdvshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartViewFragment.this, ShareItemsScreen.class));


                dialog.dismiss();


            }
        });


    }

    @Override
    public void removefromcart(String itemid, String quantity, String serialno) {
        showProgressDialog();
        JsonObject items = new JsonObject();

        items.addProperty("cartListNo", CartId);
        items.addProperty("customerID", CustomerID);
        items.addProperty("employeeID", EmployeeID);
        items.addProperty("serialNumber", serialno);
        items.addProperty("itemID", itemid);
        items.addProperty("qty", 0);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(items);
        addtoCartViewModel.AddtoCart(AppConstants.Authorization + AuthToken, "delete", jsonArray);

    }


    public interface BackHandler {
        void backpress();
    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(CartViewFragment.this, key, json);
    }

    private void Delete() {
        class SavePlan extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(CartViewFragment.this)
                        .getAppDatabase()
                        .discountItemsDao()
                        .deleteall();
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


}