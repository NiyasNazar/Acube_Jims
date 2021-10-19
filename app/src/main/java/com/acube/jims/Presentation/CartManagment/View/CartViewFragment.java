package com.acube.jims.Presentation.CartManagment.View;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.acube.jims.Presentation.CartManagment.adapter.CartItemAdapterForSharing;
import com.acube.jims.Presentation.HomePage.View.HomeFragment;
import com.acube.jims.Presentation.PdfGeneration.ShareItemsScreen;
import com.acube.jims.Presentation.Quotation.InvoiceFragment;
import com.acube.jims.Presentation.Quotation.SaleFragment;
import com.acube.jims.R;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.CartViewFragmentBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Cart.CartDetail;
import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.models.Cart.ResponseCart;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    List<CartDetail> dataset;

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
        dataset = new ArrayList<>();
        binding.recycartitems.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.toolbar.parentlayout.setOnClickListener(new View.OnClickListener() {
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
                showPopupWindow(v);

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
                    dataset = responseCart.getCartDetails();
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

                } else {
                    binding.emptycart.setVisibility(View.VISIBLE);
                    binding.nestedscrollview.setVisibility(View.GONE);
                    binding.bottomlayt.setVisibility(View.GONE);
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

    public void showPopupWindow(final View view) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.pop_up_layout_invoice, null);
        CardView checkout = alertLayout.findViewById(R.id.cdvcheckout);
        CardView cdvquote = alertLayout.findViewById(R.id.cdvcreatequote);
        CardView cdvshare = alertLayout.findViewById(R.id.cdvshare);


        //  final TextInputEditText etPassword = alertLayout.findViewById(R.id.tiet_password);


        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
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
                FragmentHelper.replaceFragment(getActivity(), R.id.content, new SaleFragment());
            }
        });
        cdvquote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentHelper.replaceFragment(getActivity(), R.id.content, new InvoiceFragment());
                dialog.dismiss();
            }
        });
        cdvshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShareItemsScreen.class));


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