package com.acube.jims.Presentation.CartManagment.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.CartManagment.ViewModel.AddtoCartViewModel;
import com.acube.jims.Presentation.CartManagment.ViewModel.CartViewModel;
import com.acube.jims.Presentation.CartManagment.adapter.CartItemAdapter;
import com.acube.jims.Presentation.HomePage.View.HomeFragment;
import com.acube.jims.Presentation.ProductDetails.View.ProductDetailsFragment;
import com.acube.jims.Presentation.Quotation.InvoiceFragment;
import com.acube.jims.Presentation.Quotation.adapter.DiscountItem;
import com.acube.jims.R;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.CartViewFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Cart.CartDetail;
import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.models.Cart.ResponseCart;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.math.BigInteger;
import java.util.List;

public class CartViewFragment extends BaseFragment implements CartItemAdapter.UpdateQuantity, CartItemAdapter.DeleteProduct {

    private CartViewModel mViewModel;
    CartViewFragmentBinding binding;
    String AuthToken;
    AddtoCartViewModel addtoCartViewModel;
    String EmployeeID;
    String CartId;
    String CustomerID;
    BackHandler backHandler;

    public static CartViewFragment newInstance() {
        return new CartViewFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            backHandler = (BackHandler) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = DataBindingUtil.inflate(
                inflater, R.layout.cart_view_fragment, container, false);
        binding.toolbar.tvFragname.setText("Cart");
        binding.recycartitems.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.toolbar.dashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.replaceFragment(getActivity(), R.id.content, new HomeFragment());
            }
        });
        Delete();
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        mViewModel.init();
        binding.btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backHandler.backpress();
            }
        });

        AuthToken = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token);
        String customerId = LocalPreferences.retrieveStringPreferences(getActivity(), "GuestCustomerID");
        addtoCartViewModel = new ViewModelProvider(this).get(AddtoCartViewModel.class);
        addtoCartViewModel.init();
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.replaceFragment(getActivity(), R.id.content, new InvoiceFragment());
            }
        });

        mViewModel.ViewCart(AppConstants.Authorization + AuthToken, customerId);
        mViewModel.getLiveData().observe(getActivity(), new Observer<ResponseCart>() {
            @Override
            public void onChanged(ResponseCart responseCart) {
                if (responseCart != null) {

                    CustomerID = String.valueOf(responseCart.getCustomerID());
                    EmployeeID = String.valueOf(responseCart.getEmployeeID());
                    CartId = responseCart.getCartListNo();
                    List<CartDetail> dataset = responseCart.getCartDetails();
                    setList("cartitem", dataset);
                    Log.d("onChangedss", "onChanged: " + dataset.size());
                    binding.recycartitems.setAdapter(new CartItemAdapter(getActivity(), dataset, CartViewFragment.this, CartViewFragment.this));
                    if (dataset != null && dataset.isEmpty()) {
                        binding.emptycart.setVisibility(View.VISIBLE);
                        binding.nestedscrollview.setVisibility(View.GONE);
                        binding.bottomlayt.setVisibility(View.GONE);
                    } else {
                        binding.emptycart.setVisibility(View.GONE);
                        binding.nestedscrollview.setVisibility(View.VISIBLE);
                        binding.bottomlayt.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        addtoCartViewModel.getLiveData().observe(getActivity(), new Observer<ResponseAddtoCart>() {
            @Override
            public void onChanged(ResponseAddtoCart responseAddtoCart) {
                hideProgressDialog();
                if (responseAddtoCart != null) {
                    Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                    mViewModel.ViewCart(AppConstants.Authorization + AuthToken, customerId);
                    // LocalPreferences.storeStringPreference(getActivity(), AppConstants.CartID,responseAddtoCart.getCartListNo());
                } else {
                    Log.d(TAG, "onChangedsiz: ");
                }
            }
        });
        return binding.getRoot();
    }


    @Override
    public void updatevalue(String itemid, String quantity) {
        showProgressDialog();
        // addtoCartViewModel.AddtoCart(CartId, AppConstants.Authorization + AuthToken, CustomerID, EmployeeID, itemid, "edit", quantity);


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
        LocalPreferences.storeStringPreference(getActivity(), key, json);
    }

    private void Delete() {
        class SavePlan extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(getActivity())
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