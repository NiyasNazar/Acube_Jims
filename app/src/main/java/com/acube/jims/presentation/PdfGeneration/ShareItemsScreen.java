package com.acube.jims.presentation.PdfGeneration;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.acube.jims.BaseActivity;
import com.acube.jims.presentation.CartManagment.ViewModel.CartViewModel;
import com.acube.jims.presentation.CartManagment.adapter.CartItemAdapterForSharing;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.ActivityShareItemsScreenBinding;
import com.acube.jims.datalayer.models.Cart.CartDetail;
import com.acube.jims.datalayer.models.Cart.ResponseCart;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;

import java.util.List;

public class ShareItemsScreen extends BaseActivity {
    CartViewModel mViewModel;
    ActivityShareItemsScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_items_screen);
        showProgressDialog();
        initToolBar(binding.toolbarApp.toolbar, "Share", true);
        int customerId = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        binding.recycartitems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        mViewModel.init();
        mViewModel.ViewCart(LocalPreferences.getToken(getApplicationContext()), String.valueOf(customerId),getApplicationContext());
        mViewModel.getLiveData().observe(this, new Observer<ResponseCart>() {
            @Override
            public void onChanged(ResponseCart responseCart) {
                if (responseCart != null) {
                    hideProgressDialog();
                    List<CartDetail> dataset = responseCart.getCartDetails();
                    binding.recycartitems.setAdapter(new CartItemAdapterForSharing(getApplicationContext(), dataset));


                }
            }
        });

    }

    public void share(View view) {
        PdfGenerator.getBuilder()
                .setContext(ShareItemsScreen.this)
                .fromViewSource()
                .fromView(binding.scrollview)
                .setFileName("Test-PDF")
                .setFolderName("Test-PDF-folder")
                .openPDFafterGeneration(true)
                .build(new PdfGeneratorListener() {
                    @Override
                    public void onFailure(FailureResponse failureResponse) {
                        super.onFailure(failureResponse);
                    }

                    @Override
                    public void showLog(String log) {
                        super.showLog(log);
                    }

                    @Override
                    public void onStartPDFGeneration() {
                        /*When PDF generation begins to start*/
                    }

                    @Override
                    public void onFinishPDFGeneration() {
                        /*When PDF generation is finished*/
                    }

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        super.onSuccess(response);
                        hideProgressDialog();
                    }
                });
    }
}