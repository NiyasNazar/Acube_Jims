package com.acube.jims.Presentation.PdfGeneration;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acube.jims.BaseActivity;
import com.acube.jims.Presentation.CartManagment.ViewModel.CartViewModel;
import com.acube.jims.Presentation.CartManagment.adapter.CartItemAdapterForSharing;
import com.acube.jims.Presentation.Compare.CompareViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.ActivityShareItemsScreenBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Cart.CartDetail;
import com.acube.jims.datalayer.models.Cart.ResponseCart;
import com.acube.jims.datalayer.models.Compare.ResponseCompare;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ShareScannedItems extends BaseActivity {
    CompareViewModel mViewModel;
    ActivityShareItemsScreenBinding binding;
    String commaseparatedlist;
    List<String> compareparams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_items_screen);
        showProgressDialog();
        String customerId = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "GuestCustomerID");
        binding.recycartitems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        mViewModel = new ViewModelProvider(this).get(CompareViewModel.class);
        mViewModel.init();
        compareparams = new ArrayList<>();
        compareparams = getList("compare");
        if (compareparams != null) {
            StringBuilder str = new StringBuilder("");
            for (String eachstring : compareparams) {
                str.append(eachstring).append(",");
            }
            commaseparatedlist = str.toString();
            if (commaseparatedlist.length() > 0)
                commaseparatedlist
                        = commaseparatedlist.substring(
                        0, commaseparatedlist.length() - 1);


        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("serialNumber", commaseparatedlist);
        mViewModel.getcompareItems(LocalPreferences.getToken(getApplicationContext()), jsonObject);
        mViewModel.getLiveData().observe(this, new Observer<List<ResponseCompare>>() {
            @Override
            public void onChanged(List<ResponseCompare> responseCompares) {
                hideProgressDialog();
                if (responseCompares!=null){
                    binding.recycartitems.setAdapter(new ScannedAdapterForSharing(getApplicationContext(),responseCompares));
                }
            }
        });


    }

    public void share(View view) {
        PdfGenerator.getBuilder()
                .setContext(ShareScannedItems.this)
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

}